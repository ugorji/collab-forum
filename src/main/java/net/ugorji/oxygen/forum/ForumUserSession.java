/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

import java.util.Properties;
import net.ugorji.oxygen.manager.UserPreferencesManager;
import net.ugorji.oxygen.util.StringUtils;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;
import net.ugorji.oxygen.web.WebUserSession;

public class ForumUserSession extends WebUserSession {
  private boolean showMessageThread;
  private boolean chronologicalTopicView;

  public ForumUserSession() throws Exception {
    String username = getUserName();
    UserPreferencesManager prefsmgr = ForumLocal.getForumEngine().getUserPreferencesManager();

    String s = StringUtils.getSingleValue(prefsmgr.getForUser(username, "forum.topic_view"));
    chronologicalTopicView = ForumConstants.CHRONOLOGICAL_VIEW.equals(s);

    s = StringUtils.getSingleValue(prefsmgr.getForUser(username, "forum.show_message_thread"));
    showMessageThread = "true".equals(s);
  }

  public boolean isChronologicalTopicView() {
    return chronologicalTopicView;
  }

  public void setChronologicalTopicView(boolean b) throws Exception {
    chronologicalTopicView = b;
    UserPreferencesManager prefsmgr = ForumLocal.getForumEngine().getUserPreferencesManager();
    String username = getUserName();
    String s = (b ? ForumConstants.CHRONOLOGICAL_VIEW : ForumConstants.THREAD_VIEW);
    prefsmgr.setForUser(username, "forum.topic_view", new String[] {s});
    savePrefs(prefsmgr, username);
  }

  public boolean isShowMessageThread() {
    return showMessageThread;
  }

  public void setShowMessageThread(boolean b) throws Exception {
    showMessageThread = b;
    String username = getUserName();
    UserPreferencesManager prefsmgr = ForumLocal.getForumEngine().getUserPreferencesManager();
    prefsmgr.setForUser(username, "forum.show_message_thread", new String[] {String.valueOf(b)});
    savePrefs(prefsmgr, username);
  }

  private static void savePrefs(UserPreferencesManager prefsmgr, String username) throws Exception {
    Properties metadata = new Properties();
    metadata.setProperty("username", username);
    prefsmgr.save(metadata);
  }

  private static String getUserName() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    String username = wctx.getUserName();
    return username;
  }
}
