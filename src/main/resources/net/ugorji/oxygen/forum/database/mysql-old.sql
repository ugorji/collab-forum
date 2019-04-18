show tables;

DROP TABLE IF EXISTS oxyforum_topic_watch CASCADE;
DROP TABLE IF EXISTS oxyforum_post CASCADE;
DROP TABLE IF EXISTS oxyforum_topic CASCADE;
DROP TABLE IF EXISTS oxyforum_forum CASCADE;
DROP TABLE IF EXISTS oxyforum_user CASCADE;
DROP TABLE IF EXISTS oxyforum_misc_info CASCADE;

commit;

show tables;

CREATE TABLE oxyforum_user (
   user_id bigint(20)  UNSIGNED NOT NULL auto_increment,
   user_active tinyint(1) DEFAULT '1' NOT NULL,
   user_name varchar(25) NOT NULL UNIQUE,
   user_regdate timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
   user_lastvisit timestamp NULL,
   user_posts mediumint(8) UNSIGNED DEFAULT '0' NOT NULL,
   PRIMARY KEY (user_id)
) TYPE=INNODB;

CREATE TABLE oxyforum_forum (
   forum_id bigint(20)  UNSIGNED NOT NULL auto_increment,
   forum_parent_id bigint(20)  UNSIGNED NULL,
   forum_last_post_id bigint(20) UNSIGNED NULL,
   forum_name varchar(150) NOT NULL,
   forum_desc text NOT NULL,
   forum_status tinyint(4) DEFAULT '0' NOT NULL,
   forum_order mediumint(8) UNSIGNED DEFAULT '1' NOT NULL,
   forum_posts mediumint(8) UNSIGNED DEFAULT '0' NOT NULL,
   forum_topics mediumint(8) UNSIGNED DEFAULT '0' NOT NULL,
   forum_prune_days smallint(5) UNSIGNED DEFAULT '0' NOT NULL,
   PRIMARY KEY (forum_id),
   KEY forum_order (forum_order),
   KEY forum_last_post_id (forum_last_post_id),
   FOREIGN KEY (forum_parent_id) REFERENCES oxyforum_forum(forum_id) ON DELETE RESTRICT ON UPDATE CASCADE
) TYPE=INNODB;

CREATE TABLE oxyforum_topic (
   topic_id bigint(20)  UNSIGNED NOT NULL auto_increment,
   forum_id bigint(20) UNSIGNED NOT NULL,
   topic_poster_id bigint(20)  UNSIGNED NOT NULL,
   topic_first_post_id bigint(20) UNSIGNED NULL,
   topic_last_post_id bigint(20) UNSIGNED NULL,
   topic_title varchar(60) NOT NULL,
   topic_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
   topic_views mediumint(8) UNSIGNED DEFAULT '0' NOT NULL,
   topic_replies mediumint(8) UNSIGNED DEFAULT '0' NOT NULL,
   topic_status tinyint(3) DEFAULT '0' NOT NULL,
   topic_type tinyint(3) DEFAULT '0' NOT NULL,
   PRIMARY KEY (topic_id),
   KEY forum_id (forum_id),
   KEY topic_status (topic_status),
   KEY topic_type (topic_type),
   FOREIGN KEY (forum_id) REFERENCES oxyforum_forum(forum_id) ON DELETE RESTRICT  ON UPDATE CASCADE,
   FOREIGN KEY (topic_poster_id) REFERENCES oxyforum_user(user_id) ON DELETE RESTRICT ON UPDATE CASCADE
) TYPE=INNODB;


CREATE TABLE oxyforum_post (
   post_id bigint(20) UNSIGNED NOT NULL auto_increment,
   post_parent_id bigint(20)  UNSIGNED NULL,
   topic_id bigint(20)  UNSIGNED NOT NULL,
   poster_id bigint(20)  UNSIGNED NOT NULL,
   post_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
   poster_ip varchar(15) NOT NULL,
   post_subject varchar(60),
   post_text text,
   post_attach_sig tinyint(1) DEFAULT '1' NOT NULL,
   PRIMARY KEY (post_id),
   KEY topic_id (topic_id),
   KEY poster_id (poster_id),
   KEY post_date (post_date),
   FOREIGN KEY (topic_id) REFERENCES oxyforum_topic(topic_id) ON DELETE RESTRICT ON UPDATE CASCADE,
   FOREIGN KEY (poster_id) REFERENCES oxyforum_user(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
   FOREIGN KEY (post_parent_id) REFERENCES oxyforum_post(post_id) ON DELETE RESTRICT ON UPDATE CASCADE
) TYPE=INNODB;

CREATE TABLE oxyforum_misc_info (
   misc_id      bigint(20) UNSIGNED NOT NULL auto_increment,
   misc_key     varchar(60) NOT NULL UNIQUE,
   misc_value   text NOT NULL,
   PRIMARY KEY (misc_id)
) TYPE=INNODB;

CREATE TABLE oxyforum_topic_watch (
   watch_id bigint(20) UNSIGNED NOT NULL auto_increment,
   topic_id bigint(20)  UNSIGNED NOT NULL,
   user_id bigint(20)  UNSIGNED NOT NULL,
   PRIMARY KEY (watch_id),
   KEY (user_id, topic_id),
   FOREIGN KEY (topic_id) REFERENCES oxyforum_topic(topic_id) ON DELETE RESTRICT ON UPDATE CASCADE,
   FOREIGN KEY (user_id) REFERENCES oxyforum_user(user_id) ON DELETE RESTRICT ON UPDATE CASCADE
) TYPE=INNODB;


commit;

insert into oxyforum_user (user_active, user_name, user_regdate, user_lastvisit) values(1, "admin", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into oxyforum_user (user_active, user_name, user_regdate, user_lastvisit) values(1, "anonymous", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into oxyforum_forum (forum_name, forum_desc) values("Top", "Top");
insert into oxyforum_topic (forum_id, topic_title, topic_poster_id, topic_date, topic_first_post_id, topic_last_post_id) values(1, "Admin Notice", 1, CURRENT_TIMESTAMP, 1, 1);
insert into oxyforum_post (topic_id, poster_id, post_date, poster_ip, post_subject, post_text) values(1, 1, CURRENT_TIMESTAMP, '255.255.255.255', "Admin Notice", "Admin Notice");

commit;

