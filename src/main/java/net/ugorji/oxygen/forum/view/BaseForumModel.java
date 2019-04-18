/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import java.io.*;
import java.util.*;
import net.ugorji.oxygen.forum.*;
import net.ugorji.oxygen.forum.data.*;
import net.ugorji.oxygen.util.CloseUtils;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebConstants;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class BaseForumModel {
  private static final FileFilter FILE_ONLY_FILTER;
  private static final List ADMIN_ACTIONS;
  private static final List VIEW_FORUM_TOPIC_ACTIONS;

  static {
    String[] sa =
        new String[] {"admin", "manageforums", "editforumform", "edittopicform", "indexing"};
    ADMIN_ACTIONS = Arrays.asList(sa);

    sa = new String[] {};
    VIEW_FORUM_TOPIC_ACTIONS = Arrays.asList(sa);

    FILE_ONLY_FILTER =
        new FileFilter() {
          public boolean accept(File f) {
            return (f != null && f.isFile());
          }
        };
  }

  public static String i18n(String s) throws Exception {
    return WebLocal.getI18n().str(s);
  }

  public static String i18n(String s, String v1) throws Exception {
    return WebLocal.getI18n().str(s, new String[] {v1});
  }

  public static String i18n(String s, String[] v1) throws Exception {
    return WebLocal.getI18n().str(s, v1);
  }

  public static String i18n(String s, List v1) throws Exception {
    // for(int i = 0; i < v1.size(); i++) { System.out.print("i18n: " +
    // v1.get(i).getClass().getName() + " | " + v1.get(i) + " "); System.out.println(""); }
    return WebLocal.getI18n().str(s, (String[]) v1.toArray(new String[0]));
  }

  public static String getAction() {
    return WebLocal.getViewContext().getAction();
  }

  public static String getURL(String action) throws Exception {
    return getURL(action, null, null);
  }

  public static String getURL(String action, Map queryParams) throws Exception {
    return getURL(action, null, queryParams);
  }

  public static String getURL(String action, Number id) throws Exception {
    return getURL(action, id, null);
  }

  public static String getURL(String action, Number id, Map queryParams) throws Exception {
    ViewContext vctx = new ViewContext();
    vctx.setAction(action);
    Number ll = null;
    if (id != null && id instanceof Number && (ll = (Number) id).longValue() > 0) {
      vctx.setAttribute(ForumConstants.ID_KEY, ll);
    }
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    return wctx.toURLString(vctx, queryParams);
  }

  public static Forum getTopForum() {
    return (Forum) dao().getRootForum();
  }

  public static Forum getForum() {
    ViewContext vctx = WebLocal.getViewContext();
    Long id = (Long) vctx.getAttribute(ForumConstants.FORUMID_KEY);
    Forum f = null;
    if (id != null) {
      f = (Forum) dao().get(Forum.class, id, true);
    }
    return f;
  }

  public static Topic getTopic() {
    ViewContext vctx = WebLocal.getViewContext();
    Long id = (Long) vctx.getAttribute(ForumConstants.TOPICID_KEY);
    Topic t = null;
    if (id != null) {
      t = (Topic) dao().get(Topic.class, id, true);
    }
    return t;
  }

  public static Post findPost(Long id) throws Exception {
    return ((id == null || id < 0) ? null : (Post) dao().get(Post.class, id, true));
  }

  public static User getUser() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    ViewContext vctx = WebLocal.getViewContext();
    Long id = (Long) vctx.getAttribute(ForumConstants.ID_KEY);
    String s = wctx.getParameter("userid");
    if (s != null) {
      id = new Long(s);
    }
    return (User) dao().get(User.class, id, true);
  }

  public static Forum[] getLineage(Forum forum) {
    if (forum == null) {
      return new Forum[0];
    }
    LinkedList list = new LinkedList();
    Forum forum2 = forum;
    list.addFirst(forum2);
    while ((forum2 = forum2.getParentForum()) != null) {
      list.addFirst(forum2);
    }
    Forum[] lineage = (Forum[]) list.toArray(new Forum[0]);
    return lineage;
  }

  public static String getAbsoluteName(Forum forum) {
    StringBuffer buf = new StringBuffer();
    Forum[] line = getLineage(forum);
    if (line.length > 0) {
      buf.append(line[0].getTitle());
      for (int i = 1; i < line.length; i++) {
        buf.append("/").append(line[i].getTitle());
      }
    }
    return buf.toString();
  }

  public static List getAllForums() {
    return dao().getAllForums();
  }

  public static String getLineageAction() {
    ViewContext vctx = WebLocal.getViewContext();
    if (ADMIN_ACTIONS.contains(vctx.getAction())) {
      return "manageforums";
    } else if (VIEW_FORUM_TOPIC_ACTIONS.contains(vctx.getAction())) {
      return "forum";
    } else {
      return "forums";
    }
  }

  public static Topic getLineageTopic() {
    ViewContext vctx = WebLocal.getViewContext();
    if (!(ADMIN_ACTIONS.contains(vctx.getAction()))) {
      return getTopic();
    }
    return null;
  }

  public static String getContextPath() {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    return wctx.getContextPath();
  }

  public static ForumEngine getForumEngine() {
    return ForumLocal.getForumEngine();
  }

  public static String getUserpref(String username, String key) throws Exception {
    return getUserpref(username, key, null);
  }

  public static boolean getBooleanUserpref(String username, String key) throws Exception {
    return bool(getUserpref(username, key, null));
  }

  public static String getUserpref(String username, String key, String def) throws Exception {
    return string(
        ForumLocal.getForumEngine().getUserPreferencesManager().getForUser(username, key), def);
  }

  public static String string(String[] args, String nonExistingValue) {
    if (args == null || args.length == 0 || args[0] == null) {
      return nonExistingValue;
    }
    return args[0];
  }

  public static String string(String[] args) {
    return string(args, "");
  }

  public static boolean bool(String s) {
    return "true".equals(s);
  }

  public static Long getViewId() {
    ViewContext vctx = WebLocal.getViewContext();
    Long id = (Long) vctx.getAttribute(ForumConstants.ID_KEY);
    return id;
  }

  public static String getUserName() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    return wctx.getUserName();
  }

  public static boolean isUsernameOverrideAllowed() throws Exception {
    return ForumLocal.getForumEngine().isUsernameOverrideAllowed();
  }

  public static File[] getFiles(String s) {
    File f = ForumLocal.getForumEngine().getUploadDirectory();
    f = new File(f, s);
    File[] fa = new File[0];
    if (f.exists() && f.isDirectory()) {
      fa = f.listFiles(FILE_ONLY_FILTER);
    }
    return fa;
  }

  public static String getURLForInternalResource(String s) {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    s = wctx.getContextPath() + "/" + s;
    return wctx.encodeURL(s, false);
  }

  public static String getURLForUploadedResource(String s) {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    s = ForumLocal.getForumEngine().getProperty(WebConstants.UPLOAD_BASE_URL_KEY) + "/" + s;
    return wctx.encodeURL(s, true);
  }

  public static String getCurrentURL() {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    return wctx.toURLString(WebLocal.getViewContext(), null);
  }

  public static String getConfigProperty(String s) {
    return ForumLocal.getForumEngine().getProperty(s);
  }

  public static Date longToDate(long l) {
    return new Date(l);
  }

  public static Map getEmptyMap() {
    return Collections.EMPTY_MAP;
  }

  public static boolean isUserWatching(Topic t) throws Exception {
    ForumDAO dao = dao();
    User u = dao.getUser(getUserName());
    return u.getWatchedTopics().contains(t);
  }

  public static void writeTextAsHtmlMarkup(String s, Writer w) throws Exception {
    ForumUtil.writeTextAsHtmlMarkup(s, w);
  }

  public static void writeTextAsHtmlMarkupFromResource(String s, Writer w) throws Exception {
    InputStream is = null;
    try {
      is = Thread.currentThread().getContextClassLoader().getResourceAsStream(s);
      InputStreamReader isr = new InputStreamReader(is);
      ForumLocal.getForumEngine().getForumRenderEngine().render(w, isr);
      w.flush();
    } finally {
      CloseUtils.close(is);
    }
  }

  public static String getRecentTopicsFromDateAsString() throws Exception {
    int numdays =
        Integer.parseInt(
            ForumLocal.getForumEngine()
                .getProperty(ForumConstants.RECENT_TOPICS_INTERVAL_DAYS_KEY));
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, (-1 * numdays));
    Date d = cal.getTime();
    return SearchResultsAction.dateformat.format(d);
  }

  public static User[] getUsers() throws Exception {
    ForumDAO dao = dao();
    List list = dao.getAll(User.class);
    return (User[]) list.toArray(new User[0]);
  }

  public static boolean allowCreateTopic(DataEntity f) throws Exception {
    return ForumUtil.checkAccessConsideringLockedStatusAndRoles(f, false);
  }

  public static boolean isTopicViewSpartan() throws Exception {
    return "true"
        .equals(ForumLocal.getForumEngine().getProperty(ForumConstants.TOPIC_VIEW_SPARTAN_KEY));
  }

  public static boolean showLogoutLink() throws Exception {
    return ("true"
            .equals(ForumLocal.getForumEngine().getProperty(ForumConstants.TEMPLATE_SHOWLOGOUT_KEY))
        && (WebLocal.getWebInteractionContext().getUserPrincipal() != null));
  }

  public static boolean isEngineLocked() throws Exception {
    return ForumLocal.getForumEngine().getLongTermLock().isHeld();
  }

  public static boolean isSupportThreadView() throws Exception {
    return "true"
        .equals(ForumLocal.getForumEngine().getProperty(ForumConstants.SUPPORT_THREAD_VIEW_KEY));
  }

  public static boolean isThreadedView() throws Exception {
    return ("true"
            .equals(ForumLocal.getForumEngine().getProperty(ForumConstants.SUPPORT_THREAD_VIEW_KEY))
        && !(ForumLocal.getForumUserSession().isChronologicalTopicView()));
  }

  public static boolean isShowMessageThread() throws Exception {
    return ForumLocal.getForumUserSession().isShowMessageThread();
  }

  public static void throwAnError() throws Exception {
    throw new RuntimeException("Ugorji is still cool");
  }

  public static ForumStatisticsManager stats() throws Exception {
    return ForumLocal.getForumEngine().getForumStatisticsManager();
  }

  public static ForumDAO dao() {
    return ForumLocal.getForumEngine().getForumDAO();
  }

  public static Post getLastPost(Topic t) throws Exception {
    long i = stats().getLastPostIdForTopic(t.getId());
    return findPost(i);
  }

  public static Post getLastPost(Forum f) throws Exception {
    long i = stats().getLastPostIdForForum(f.getId());
    return findPost(i);
  }
}
