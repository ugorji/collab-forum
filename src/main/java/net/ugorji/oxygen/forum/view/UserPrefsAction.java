/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import java.util.Enumeration;
import java.util.Properties;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.manager.UserPreferencesManager;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class UserPrefsAction extends GenericWebAction {
  {
    setFlag(FLAG_ADMIN_ACTION);
  }

  public int processAction() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    UserPreferencesManager prefsmgr = ForumLocal.getForumEngine().getUserPreferencesManager();
    String username = wctx.getUserName();
    if (ForumLocal.getForumEngine().isUsernameOverrideAllowed()
        && !(username.equals(wctx.getParameter("username")))) {
      username = wctx.getParameter("username");
      wctx.setUserName(username);
    }
    for (Enumeration enum0 = wctx.getParameterNames(); enum0.hasMoreElements(); ) {
      String s = (String) enum0.nextElement();
      String s2 = null;
      if (s.startsWith("forum.")) {
        s2 = s.substring(6);
        prefsmgr.setForUser(username, s2, wctx.getParameterValues(s));
      }
    }

    Properties metadata = new Properties();
    metadata.setProperty("username", username);
    prefsmgr.save(metadata);

    ViewContext vctx = WebLocal.getViewContext();
    vctx.setAction("userprefsform");
    return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
  }
}
