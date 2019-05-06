# collab-forum

This forum was created due to a number of pressing problems we saw in
available open-source Java based (Servlet/JSP) Forum Engines.

- Using the database for everything, including searching and indexing
- No free forum that allows sub-forums to any depth
- No free ones having all the capabilities we wanted in one package, especially:

We directly leverage a bunch of open-source products:

- Apache Lucene - [http://jakarta.apache.org/lucene] (for indexing and searching)
- FreeMarker - [http://freemarker.org/] (for [templates|/oxywiki/p/view/help/Templates] we evaluate at runtime)
- Apache Commons (logging, fileupload) - [http://jakarta.apache.org/commons]
- EhCache - [http://ehcache.sourceforge.net/]
- Hibernate - [http://www.hibernate.org]

## Dependencies

This repository is part of a multi-project `gradle` build.

It has the following dependencies:

- [java-common](https://github.com/ugorji/java-common)
- [java-markup](https://github.com/ugorji/java-markup)
- [java-web](https://github.com/ugorji/java-web)
- [web-common](https://github.com/ugorji/web-common)

Before building:

- clone the dependencies into adjacent folders directly under same parent folder
- download [`settings.gradle`](https://gist.githubusercontent.com/ugorji/2a338462e63680d117016793989847fa/raw/settings.gradle) into the parent folder

## Building

```sh
gradle clean
gradle build
```

# Running

```sh
gradle appRunWar
```

# Initialization

To start, you must have access to a database.

You can have your admin create a database instance using the commands below:

```
CREATE USER 'collab'@'localhost' IDENTIFIED BY 'pw4collab';
CREATE DATABASE collab;
GRANT ALL ON collab.* TO 'collab'@'localhost';
```

Thereafter, you can login to use it locally using

```
/usr/local/mysql/bin/mysql -u collab -ppw4collab collab
```

Once in, you can do the following:

```
source src/main/resources/net.ugorji.oxygen/forum/database/mysql.sql
```

# Usage Information

*The user manual is available at src/main/resources/net.ugorji.oxygen/forum/help.txt.*
*We store it there since it has to be loaded up by the forum engine to show bundled help.*

# Software dependencies

- Mandatory
  1. oxy-jdk-extensions.jar
  1. oxy-common.jar
  1. oxy-markup.jar
  1. oxy-forum-classes.jar
  1. oxy-forum-resources.jar
  1. commons-logging.jar,
  1. commons-fileupload.jar, (1.1 has a bad bug, causing all filenames to be lowercase)
  1. commons-io.jar
  1. lucene-core.jar,
  1. freemarker.jar,
  1. org.suigeneris.jrcs.diff.jar
  1. jpa.jar 
  1. jta.jar 
- Mandatory (required if using hibernate)
  1. hibernate.jar
  1. hibernate-annotations.jar
  1. hibernate-entitymanager.jar
  1. hibernate-commons-annotations.jar
  1. jboss-archive-browsing.jar
  1. antlr.jar 
  1. javassist.jar 
  1. dom4j.jar 
  1. ehcache.jar 
- Mandatory (required if using openjpa)
  1. openjpa.jar
  1. commons-lang.jar
  1. jca.jar
  1. serp.jar
- Optional
  1. ehcache.jar (if using ehcache as markup caching provider.)
  1. radeox_base.jar (only if leveraging radeox macros)
  1. bsf.jar (only if using the script macro - some people disable this for security reasons)
  1. bsh.jar (only if using the script macro - and leveraging beanshell as your scripting engine)
  1. jhlabs-imaging.jar (only if using captcha, to prevent spam, and ensure humans post entries to the forum)
  1. rome.jar (only if using rss macro)
  1. jdom.jar (only if using rss macro - rss macro uses rome.jar which internally uses jdom.jar)

# Misc

## Locale Management

In the forum, there is only one big *sandbox*. Consequently, a locale must only be set
administratively, and applies to the whole forum. The locale set is the same one which is
used as the language for creating topics and posts.

