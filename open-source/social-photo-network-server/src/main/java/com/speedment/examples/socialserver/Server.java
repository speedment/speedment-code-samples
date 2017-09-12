package com.speedment.examples.socialserver;

import com.speedment.examples.generated.socialnetwork.SocialnetworkApplication;
import com.speedment.examples.generated.socialnetwork.SocialnetworkApplicationBuilder;
import com.speedment.examples.generated.socialnetwork.db0.socialnetwork.image.Image;
import com.speedment.examples.generated.socialnetwork.db0.socialnetwork.image.ImageImpl;
import com.speedment.examples.generated.socialnetwork.db0.socialnetwork.image.ImageManager;
import com.speedment.examples.generated.socialnetwork.db0.socialnetwork.link.Link;
import com.speedment.examples.generated.socialnetwork.db0.socialnetwork.link.LinkImpl;
import com.speedment.examples.generated.socialnetwork.db0.socialnetwork.link.LinkManager;
import com.speedment.examples.generated.socialnetwork.db0.socialnetwork.user.User;
import com.speedment.examples.generated.socialnetwork.db0.socialnetwork.user.UserImpl;
import com.speedment.examples.generated.socialnetwork.db0.socialnetwork.user.UserManager;
import com.speedment.plugins.json.JsonBundle;
import com.speedment.plugins.json.JsonComponent;
import com.speedment.plugins.json.JsonEncoder;
import com.speedment.runtime.core.exception.SpeedmentException;
import com.speedment.runtime.field.method.BackwardFinder;
import fi.iki.elonen.ServerRunner;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;

/**
 *
 * @author Emil Forslund
 */
public class Server extends ServerBase {

    protected final Random random = new SecureRandom();
	private final Map<String, Long> sessions = new HashMap<>();
    
    private final SocialnetworkApplication speed;
    
    private final UserManager users;
    private final ImageManager images;
    private final LinkManager links;
    
    private final JsonEncoder<User> jsonUserEncoder;
    private final JsonEncoder<Image> jsonImageEncoder;
    
	public Server() {
		speed  = new SocialnetworkApplicationBuilder()
            .withBundle(JsonBundle.class)
            .withUsername("root")
            .withPassword("password")
            .build();
        
        users  = speed.getOrThrow(UserManager.class);
        images = speed.getOrThrow(ImageManager.class);
        links  = speed.getOrThrow(LinkManager.class);
        
        final JsonComponent json = speed.getOrThrow(JsonComponent.class);
        
        jsonUserEncoder = json.allOf(users)
            .remove(User.PASSWORD);
        
        jsonImageEncoder = json
            .allOf(images)
            .put(Image.UPLOADER, 
                json.allOf(users)
                    .remove(User.AVATAR)
                    .remove(User.PASSWORD)
            );
	}
    
    private String createSession(User user) {
		final String key = nextSessionId();
		sessions.put(key, user.getId());
		return key;
	}
	
	private Optional<User> getLoggedIn(String key) {
        final Optional<Long> userId = Optional.ofNullable(sessions.get(key));
        
        return userId.flatMap(id ->
            users.stream()
                .filter(User.ID.equal(id))
                .findAny()
        );
	}

    @Override
    public String onRegister(String mail, String password) {
        try {
            return createSession(
                users.persist(new UserImpl()
                    .setMail(mail)
                    .setPassword(password)
                )
            );
        } catch (SpeedmentException ex) {
            return "false";
        }
    }

    @Override
    public String onLogin(String mail, String password) {
        return users.stream()
            .filter(User.MAIL.equalIgnoreCase(mail))
            .filter(User.PASSWORD.equal(password))
            .findAny()
            .map(this::createSession)
            .orElse("false")
        ;
    }

    @Override
    public String onSelf(String sessionKey) {
        return getLoggedIn(sessionKey)
            .map(jsonUserEncoder::apply)
            .orElse("false");
    }

    @Override
    public String onUpload(String title, String description, String imgData, String sessionKey) {
        final Optional<User> user = getLoggedIn(sessionKey);
        
        if (user.isPresent()) {
            try {
                images.persist(new ImageImpl()
                    .setTitle(title)
                    .setDescription(description)
                    .setImgData(imgData)
                    .setUploader(user.get().getId())
                    .setUploaded(Timestamp.from(Instant.now()))
                );
                
                return "true";
            } catch (SpeedmentException ex) {
                return "false";
            }
        }
        
        return "false";
    }

    @Override
    public String onFind(String freeText, String sessionKey) {
        final Optional<User> user = getLoggedIn(sessionKey);
        
        if (user.isPresent()) {
            final User me = user.get();
            
            final Stream<User> found = users.stream()
                // If the freetext matches any field.
                .filter(
                    User.FIRST_NAME.startsWith(freeText).or(
                    User.LAST_NAME.startsWith(freeText)).or(
                    User.MAIL.startsWith(freeText))
                )

                // And this is not us.
                .filter(User.ID.notEqual(me.getId()))

                // Remove people we already follow
                .filter(them -> !links.findBackwardsBy(Link.FOLLOWER, me)
                    .anyMatch(Link.FOLLOWS.equal(them.getId()))
                )

                // Limit result to 10 persons.
                .limit(10);
            
            final String result = found
                .map(jsonUserEncoder::apply)
                .collect(joining(", "));
            
            return "{\"users\":[" + result + "]}";
        }
        
        return "false";
    }

    @Override
    public String onFollow(long userId, String sessionKey) {
        final Optional<User> user = getLoggedIn(sessionKey);
        
        if (user.isPresent()) {
            final User me = user.get();
            
            try {
                links.persist(new LinkImpl()
                    .setFollower(me.getId())
                    .setFollows(userId)
                );
                
                return "true";
            } catch (SpeedmentException ex) {
                return "false";
            }
        }
        
        return "false";
    }

    @Override
    public String onBrowse(String sessionKey, Optional<Timestamp> from, Optional<Timestamp> to) {
        final Optional<User> user = getLoggedIn(sessionKey);
        
        if (user.isPresent()) {
            final User me = user.get();
            
            final Stream<User> visibleUsers = Stream.concat(
                Stream.of(me),
                links.findBackwardsBy(Link.FOLLOWER, me)
                    .map(Link.FOLLOWS.finder(users.getTableIdentifier(), users::stream))
            );
            
            final BackwardFinder<User, Image> imagesByUser = 
                Image.UPLOADER.backwardFinder(
                    images.getTableIdentifier(), 
                    images::stream
                );
            
            final Stream<Image> imgs;
            if (from.isPresent() && to.isPresent()) {
                imgs = visibleUsers.flatMap(imagesByUser)
                    .filter(Image.UPLOADED.between(from.get(), to.get()));
            } else if (from.isPresent()) {
                imgs = visibleUsers.flatMap(imagesByUser)
                    .filter(Image.UPLOADED.greaterOrEqual(from.get()));
            } else if (to.isPresent()) {
                imgs = visibleUsers.flatMap(imagesByUser)
                    .filter(Image.UPLOADED.lessThan(to.get()));
            } else {
                imgs = visibleUsers.flatMap(imagesByUser);
            }
            
            final String result = imgs.limit(10)
                .map(jsonImageEncoder::apply)
                .collect(joining(","));
            
            return "{\"images\":[" + result + "]}";
        }
        
        return "false";
    }

    @Override
    public String onUpdate(String mail, String firstname, String lastName, 
        Optional<String> avatar, String sessionKey) {
        
        final Optional<User> user = getLoggedIn(sessionKey);
        
        if (user.isPresent()) {
            final User me = user.get();
            
            final User copy = new UserImpl()
                .setId(me.getId())
                .setMail(mail)
                .setPassword(me.getPassword())
                .setFirstName(firstname)
                .setLastName(lastName);
            
            if (avatar.isPresent()) {
                copy.setAvatar(avatar.get());
            } else {
                me.getAvatar().ifPresent(copy::setAvatar);
            }
            
            try {
                final User updated = users.update(copy);
                return jsonUserEncoder.apply(updated);
            } catch (SpeedmentException ex) {
                return "false";
            }
        }
        
        return "false";
    }

    protected String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String... args) {
        ServerRunner.run(Server.class);
    }
}