/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

/* REMOVE THIS CLASS - make it abstract and package-private to show it's deletion status*/
abstract class SessionPrefs {}
/*
  public static boolean showMessageThread() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    UserPreferencesManager prefsmgr = ForumLocal.getForumEngine().getUserPreferencesManager();
    String username = wctx.getUserName();

    Map map = getSessionMap();
    Boolean b = (Boolean)map.get("show.message.thread");
    if(b == null) {
      String s = StringUtils.getSingleValue(prefsmgr.getForUser(username, "forum.show_message_thread"));
      if(!StringUtils.isBlank(s)) {
        b = Boolean.valueOf("true".equals(s));
      }
    }
    if(b == null) {
      b = Boolean.TRUE;
    }
    map.put("show.message.thread", b);
    storeSessionMap(map);
    return b.booleanValue();
  }

  //return thread or chronological
  public static String getTopicView() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    UserPreferencesManager prefsmgr = ForumLocal.getForumEngine().getUserPreferencesManager();
    String username = wctx.getUserName();

    Map map = getSessionMap();
    String s = (String)map.get("topic.view");
    if(StringUtils.isBlank(s)) {
      s = StringUtils.getSingleValue(prefsmgr.getForUser(username, "forum.topic_view"));
      if(StringUtils.isBlank(s)) {
        s = "chronological";
      }
      map.put("topic.view", s);
    }
    storeSessionMap(map);
    return s;
  }

  public static void setShowMessageThread(boolean b) throws Exception {
    UserPreferencesManager prefsmgr = ForumLocal.getForumEngine().getUserPreferencesManager();
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    String username = wctx.getUserName();
    Map map = getSessionMap();
    map.put("show.message.thread", Boolean.valueOf(b));
    storeSessionMap(map);
    prefsmgr.setForUser(username, "forum.show_message_thread", new String[]{String.valueOf(b)});
    savePrefs();
  }

  public static void setTopicView(String s) throws Exception {
    if(s == null || (!("thread".equals(s) || "chronological".equals(s)))) {
      return;
    }
    Map map = getSessionMap();
    map.put("topic.view", s);
    storeSessionMap(map);
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    UserPreferencesManager prefsmgr = ForumLocal.getForumEngine().getUserPreferencesManager();
    String username = wctx.getUserName();
    prefsmgr.setForUser(username, "forum.topic_view", new String[]{s});
    savePrefs();
  }

  private static Map getSessionMap() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    Map map = (Map)wctx.getSessionAttribute(ForumConstants.SESSION_PREFS_KEY);
    if(map == null) {
      map = new HashMap();
      wctx.setSessionAttribute(ForumConstants.SESSION_PREFS_KEY, map);
    }
    return map;
  }

  private static void storeSessionMap(Map map) {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    wctx.setSessionAttribute(ForumConstants.SESSION_PREFS_KEY, map);
  }

  private static void savePrefs() throws Exception {
    UserPreferencesManager prefsmgr = ForumLocal.getForumEngine().getUserPreferencesManager();
    String username = WebLocal.getWebInteractionContext().getUserName();
    Properties metadata = new Properties();
    metadata.setProperty("username", username);
    prefsmgr.save(metadata);
  }

*/
