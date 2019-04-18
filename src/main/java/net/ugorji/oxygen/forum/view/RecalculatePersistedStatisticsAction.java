/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.forum.ForumStatisticsManager;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebLocal;

public class RecalculatePersistedStatisticsAction extends GenericWebAction {
  {
    setFlag(FLAG_ADMIN_ACTION);
  }

  public int processAction() throws Exception {
    ForumStatisticsManager fsm = ForumLocal.getForumEngine().getForumStatisticsManager();
    fsm.recalculatePersistedStatistics();

    ViewContext vctx = WebLocal.getViewContext();
    vctx.setAction("admin");
    return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
  }
}
