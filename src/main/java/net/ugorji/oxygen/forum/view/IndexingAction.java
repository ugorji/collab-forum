/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import net.ugorji.oxygen.forum.ForumIndexingManager;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class IndexingAction extends GenericWebAction {
  {
    setFlag(FLAG_ADMIN_ACTION);
  }

  public int processAction() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    boolean indexAll = "true".equals(wctx.getParameter("all"));
    ForumIndexingManager fim = ForumLocal.getForumEngine().getForumIndexingManager();
    if (indexAll) {
      fim.deleteIndexFiles();
      fim.indexAll();
    } else {
      fim.incrementalIndexFiles();
    }

    ViewContext vctx = WebLocal.getViewContext();
    vctx.setAction("admin");
    return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
  }
}
