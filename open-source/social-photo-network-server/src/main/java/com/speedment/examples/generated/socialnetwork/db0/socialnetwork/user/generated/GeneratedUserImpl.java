package com.speedment.examples.generated.socialnetwork.db0.socialnetwork.user.generated;

import com.speedment.common.annotation.GeneratedCode;
import com.speedment.examples.generated.socialnetwork.db0.socialnetwork.user.User;
import com.speedment.runtime.core.util.OptionalUtil;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * The generated base implementation of the {@link
 * com.speedment.examples.generated.socialnetwork.db0.socialnetwork.user.User}-interface.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@GeneratedCode("Speedment")
public abstract class GeneratedUserImpl implements User {
    
    private long id;
    private String mail;
    private String password;
    private String firstName;
    private String lastName;
    private String avatar;
    
    protected GeneratedUserImpl() {}
    
    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public String getMail() {
        return mail;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public Optional<String> getFirstName() {
        return Optional.ofNullable(firstName);
    }
    
    @Override
    public Optional<String> getLastName() {
        return Optional.ofNullable(lastName);
    }
    
    @Override
    public Optional<String> getAvatar() {
        return Optional.ofNullable(avatar);
    }
    
    @Override
    public User setId(long id) {
        this.id = id;
        return this;
    }
    
    @Override
    public User setMail(String mail) {
        this.mail = mail;
        return this;
    }
    
    @Override
    public User setPassword(String password) {
        this.password = password;
        return this;
    }
    
    @Override
    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    
    @Override
    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    
    @Override
    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }
    
    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner(", ", "{ ", " }");
        sj.add("id = "        + Objects.toString(getId()));
        sj.add("mail = "      + Objects.toString(getMail()));
        sj.add("password = "  + Objects.toString(getPassword()));
        sj.add("firstName = " + Objects.toString(OptionalUtil.unwrap(getFirstName())));
        sj.add("lastName = "  + Objects.toString(OptionalUtil.unwrap(getLastName())));
        sj.add("avatar = "    + Objects.toString(OptionalUtil.unwrap(getAvatar())));
        return "UserImpl " + sj.toString();
    }
    
    @Override
    public boolean equals(Object that) {
        if (this == that) { return true; }
        if (!(that instanceof User)) { return false; }
        final User thatUser = (User)that;
        if (this.getId() != thatUser.getId()) { return false; }
        if (!Objects.equals(this.getMail(), thatUser.getMail())) { return false; }
        if (!Objects.equals(this.getPassword(), thatUser.getPassword())) { return false; }
        if (!Objects.equals(this.getFirstName(), thatUser.getFirstName())) { return false; }
        if (!Objects.equals(this.getLastName(), thatUser.getLastName())) { return false; }
        if (!Objects.equals(this.getAvatar(), thatUser.getAvatar())) { return false; }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Long.hashCode(getId());
        hash = 31 * hash + Objects.hashCode(getMail());
        hash = 31 * hash + Objects.hashCode(getPassword());
        hash = 31 * hash + Objects.hashCode(OptionalUtil.unwrap(getFirstName()));
        hash = 31 * hash + Objects.hashCode(OptionalUtil.unwrap(getLastName()));
        hash = 31 * hash + Objects.hashCode(OptionalUtil.unwrap(getAvatar()));
        return hash;
    }
}