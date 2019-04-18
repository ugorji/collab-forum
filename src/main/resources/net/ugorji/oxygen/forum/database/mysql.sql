show tables;

DROP TABLE IF EXISTS oxyforum_topic_watch CASCADE;
DROP TABLE IF EXISTS oxyforum_topic CASCADE;
DROP TABLE IF EXISTS oxyforum_post CASCADE;
DROP TABLE IF EXISTS oxyforum_forum CASCADE;
DROP TABLE IF EXISTS oxyforum_user CASCADE;
DROP TABLE IF EXISTS oxyforum_misc CASCADE;
DROP TABLE IF EXISTS openjpa_sequence_table CASCADE;

commit;

show tables;

CREATE TABLE oxyforum_misc (
    f_key VARCHAR(255) NOT NULL, 
    f_value VARCHAR(255) NOT NULL, 
    PRIMARY KEY (f_key)
) ENGINE = INNODB;

CREATE TABLE oxyforum_user (
    f_id BIGINT NOT NULL AUTO_INCREMENT, 
    f_active BIT NOT NULL, 
    f_lastvisit DATETIME NOT NULL, 
    f_name VARCHAR(255) NOT NULL, 
    f_regdate DATETIME NOT NULL, 
    f_version INTEGER, 
    PRIMARY KEY (f_id)
) ENGINE = INNODB;

CREATE TABLE oxyforum_forum (
    f_id BIGINT NOT NULL AUTO_INCREMENT, 
    f_date DATETIME NOT NULL, 
    f_details TEXT NOT NULL, 
    f_highvisibility BIT NOT NULL, 
    f_order INTEGER NOT NULL, 
    f_read BIT NOT NULL, 
    f_title VARCHAR(255) NOT NULL, 
    f_write BIT NOT NULL, 
    f_prunedays INTEGER NOT NULL, 
    f_version INTEGER, 
    f_author BIGINT NOT NULL, 
    f_parent_id BIGINT, 
    PRIMARY KEY (f_id), 
    FOREIGN KEY (f_parent_id) REFERENCES oxyforum_forum(f_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = INNODB;

CREATE TABLE oxyforum_post (
    f_id BIGINT NOT NULL AUTO_INCREMENT, 
    f_date DATETIME NOT NULL, 
    f_details TEXT NOT NULL, 
    f_highvisibility BIT NOT NULL, 
    f_order INTEGER NOT NULL, 
    f_read BIT NOT NULL, 
    f_title VARCHAR(255) NOT NULL, 
    f_write BIT NOT NULL, 
    f_ip VARCHAR(255) DEFAULT '255.255.255.255', 
    f_version INTEGER, 
    f_author BIGINT NOT NULL, 
    f_parent_post_id BIGINT, 
    f_topic_id BIGINT NOT NULL, 
    PRIMARY KEY (f_id),
    FOREIGN KEY (f_author) REFERENCES oxyforum_user(f_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (f_parent_post_id) REFERENCES oxyforum_post(f_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = INNODB;

CREATE TABLE oxyforum_topic (
    f_post_id BIGINT NOT NULL, 
    f_forum_id BIGINT NOT NULL, 
    f_numviews INTEGER NOT NULL, 
    FOREIGN KEY (f_forum_id) REFERENCES oxyforum_forum(f_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (f_post_id) REFERENCES oxyforum_post(f_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = INNODB;

CREATE TABLE oxyforum_topic_watch (
    f_topic_id BIGINT NOT NULL, 
    f_user_id BIGINT NOT NULL, 
    FOREIGN KEY (f_topic_id) REFERENCES oxyforum_topic(f_post_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (f_user_id) REFERENCES oxyforum_user(f_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = INNODB;

-- This table is needed for the GenerationType.SEQUENCE entities
CREATE TABLE openjpa_sequence_table (
    ID tinyint(4) NOT NULL,
    SEQUENCE_VALUE bigint(20) DEFAULT NULL,
    PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

commit;

show tables;

