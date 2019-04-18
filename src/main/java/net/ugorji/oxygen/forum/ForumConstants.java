/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

public interface ForumConstants {
  // Long LONG_1 = new Long(1);
  Long LONG_MINUS_1 = new Long(-1);
  long ONE_DAY = (1000l * 60 * 60 * 24);

  int STATUS_UNLOCKED = 0;
  int STATUS_LOCKED = 1;
  int TYPE_NORMAL = 0;
  int TYPE_STICKY = 1;
  int TYPE_ANNOUNCEMENT = 2;

  String CHRONOLOGICAL_VIEW = "chronological";
  String THREAD_VIEW = "thread";

  String TITLE_KEY = "net.ugorji.oxygen.forum.title";

  String AUTHENTICATION_MANAGER_KEY = "net.ugorji.oxygen.forum.manager.authentication";
  String AUTHORIZATION_MANAGER_KEY = "net.ugorji.oxygen.forum.manager.authorization";
  String USERPREFERENCES_MANAGER_KEY = "net.ugorji.oxygen.forum.manager.userpreferences";
  String ENGINE_KEY = "net.ugorji.oxygen.forum.engine";

  String DEFAULT_USER_KEY = "net.ugorji.oxygen.forum.user.default";

  String UNKNOWN_IP_ADDRESS = "255.255.255.255";

  String CONFIG_INIT_OVERRIDE_FILE = "oxygen.forum.config.override.properties";
  String REDIRECT_AFTER_POST_SUFFIX_KEY = "net.ugorji.oxygen.forum.redirect.after.post.suffix";
  String SERVLET_MAPPING_PREFIX_KEY = "net.ugorji.oxygen.forum.servlet.prefix";
  String ENCODING_KEY = "net.ugorji.oxygen.forum.encoding";
  String VIEW_CONTEXT_KEY = "net.ugorji.oxygen.forum.viewcontext";
  String ID_KEY = "net.ugorji.oxygen.forum.id";
  String FORUMID_KEY = "net.ugorji.oxygen.forum.forumid";
  String TOPICID_KEY = "net.ugorji.oxygen.forum.topicid";
  String PATH_KEY = "net.ugorji.oxygen.forum.path";

  // String QUERYPARAMETERS_KEY = "net.ugorji.oxygen.forum.queryparameters";

  String SEARCH_INDEX_TOPIC_AUTHOR = "TOPIC_AUTHOR";
  String SEARCH_INDEX_TOPIC_CONTENTS = "TOPIC_CONTENTS";
  String SEARCH_INDEX_TOPIC_LAST_POST_DATE = "TOPIC_LAST_POST_DATE";
  String SEARCH_INDEX_POSTER = "POSTER";
  String SEARCH_INDEX_TITLE = "TITLE";
  String SEARCH_INDEX_TOPIC_ID = "TOPIC_ID";
  String SEARCH_INDEX_FORUM_ID = "FORUM_ID";

  String SEARCH_MAX_NUM_HITS_KEY = "net.ugorji.oxygen.forum.search.hits.max";
  String LISTENER_KEY_PREFIX = "net.ugorji.oxygen.forum.listener.";

  String MACRO_KEY_PREFIX = "net.ugorji.oxygen.forum.macro.";

  String ALLOW_OVERRIDE_USERNAME_KEY = "net.ugorji.oxygen.forum.allow.override.username";
  String LAST_INDEX_DATETIME_KEY = "last_index_datetime";

  String TEMPLATE_NAMES_KEY = "net.ugorji.oxygen.forum.template.names";
  String TEMPLATE_BASEFILE_KEY = "net.ugorji.oxygen.forum.template.basefile";
  String TEMPLATE_SHOWLOGOUT_KEY = "net.ugorji.oxygen.forum.template.showlogout";

  String EMAIL_ENABLED_KEY = "net.ugorji.oxygen.forum.email.enabled";
  String EMAIL_SENDER_KEY = "net.ugorji.oxygen.forum.email.sender";
  String EMAIL_SMTP_HOST_KEY = "net.ugorji.oxygen.forum.email.smtp.host";
  String STATISTICS_UPDATE_INTERVAL_KEY = "net.ugorji.oxygen.forum.statistics.update.interval";

  String ADMIN_ROLE_KEY = "net.ugorji.oxygen.forum.admin.role";
  String SECURITY_CHECKING_ENABLED_KEY = "net.ugorji.oxygen.forum.security.checking.enabled";

  String RECENT_TOPICS_INTERVAL_DAYS_KEY = "net.ugorji.oxygen.forum.recent.topics.interval.days";

  String SESSION_PREFS_KEY = "oxyforum.session.prefs";
  String CAPTCHA_ENABLED_KEY = "net.ugorji.oxygen.forum.captcha.enabled";

  String INDEX_AT_STARTUP_KEY = "net.ugorji.oxygen.forum.startup.index";
  String RECALCULATE_PERSISTED_STATISTICS_AT_STARTUP_KEY =
      "net.ugorji.oxygen.forum.startup.recalculate.statistics";
  String TOPIC_VIEW_SPARTAN_KEY = "net.ugorji.oxygen.forum.view.topic.spartan";

  String INDEXING_ANALYZER_CLASSNAME_KEY = "net.ugorji.oxygen.forum.indexing.analyzer";

  String FORUM_USER_SESSION_SESSION_KEY = "net.ugorji.oxygen.forum.user.session";
  String RSS_FORMAT_KEY = "net.ugorji.oxygen.forum.rss.format";

  String DB_INIT_ENABLED_KEY = "net.ugorji.oxygen.forum.dbinit";
  String DB_INIT_CLEAR_TABLES_KEY = "net.ugorji.oxygen.forum.dbinit.clear_tables";
  String DB_INIT_LOAD_RESOURCE_KEY = "net.ugorji.oxygen.forum.dbinit.load_resource";

  String SUPPORT_THREAD_VIEW_KEY = "net.ugorji.oxygen.forum.support_thread_view";
  String DAO_PROVIDER_PROPERTIES_PREFIX_KEY = "net.ugorji.oxygen.forum.dao_provider_properties_prefix";

  String IDENTITY_SEQUENCE_CLEAR_SQL_KEY = "net.ugorji.oxygen.forum.identity_sequence_clear_sql";
}
