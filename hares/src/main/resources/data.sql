INSERT INTO `hares`.`hare` (`id`,`name`,`color`,`age` ) VALUES (1,'Harry','Gray',3);
INSERT INTO `hares`.`hare` (`id`,`name`,`color`,`age` ) VALUES (2,'Henrietta','White',2);
INSERT INTO `hares`.`hare` (`id`,`name`,`color`,`age` ) VALUES (3,'Henry','Black',9);

INSERT INTO `hares`.`carrot` (`id`,`name`,`owner`, `rival`) VALUES (1,'The big one',1,3);
INSERT INTO `hares`.`carrot` (`id`,`name`,`owner`, `rival`) VALUES (2,'Orange',1,2);
INSERT INTO `hares`.`carrot` (`id`,`name`,`owner`, `rival`) VALUES (3,'The small',2,null);
INSERT INTO `hares`.`carrot` (`id`,`name`,`owner`, `rival`) VALUES (4,'The old and rotten',3,null);

INSERT INTO `hares`.`human` (`id`,`name`) VALUES (1,'Alice');
INSERT INTO `hares`.`human` (`id`,`name`) VALUES (2,'Bob');

INSERT INTO `hares`.`friend` (`hare`,`human`) VALUES (1,1);
INSERT INTO `hares`.`friend` (`hare`,`human`) VALUES (2,1);
INSERT INTO `hares`.`friend` (`hare`,`human`) VALUES (3,1);
INSERT INTO `hares`.`friend` (`hare`,`human`) VALUES (3,2);
