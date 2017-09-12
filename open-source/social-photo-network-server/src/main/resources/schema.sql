CREATE TABLE `socialnetwork`.`user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `mail` varchar(128) NOT NULL,
  `password` varchar(128) NOT NULL,
  `first_name` varchar(128),
  `last_name` varchar(128),
  `avatar` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=100;

CREATE TABLE `socialnetwork`.`image` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uploader` bigint(20) unsigned NOT NULL,
  `title` varchar(128) NOT NULL,
  `description` text NOT NULL,
  `img_data` text NOT NULL,
  `uploaded` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=100;

CREATE TABLE `socialnetwork`.`link` (
  `follower` bigint(20) unsigned NOT NULL,
  `follows` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`follower`,`follows`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=100;

ALTER TABLE `socialnetwork`.`image`
  ADD CONSTRAINT `image_uploader_to_user_id` FOREIGN KEY (`uploader`) REFERENCES `user` (`id`);

ALTER TABLE `socialnetwork`.`link`
  ADD CONSTRAINT `follow_follower_to_user_id` FOREIGN KEY (`follower`) REFERENCES `user` (`id`);
ALTER TABLE `socialnetwork`.`link`
  ADD CONSTRAINT `follow_follows_to_user_id` FOREIGN KEY (`follows`) REFERENCES `user` (`id`);
