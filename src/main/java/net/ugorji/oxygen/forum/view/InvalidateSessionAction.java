/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class InvalidateSessionAction extends GenericWebAction {

  public int processAction() throws Exception {
    WebInteractionContext request = WebLocal.getWebInteractionContext();
    request.invalidateSession();
    // re-initializa the session
    request.getSessionAttribute("whatever");

    ViewContext vctx = WebLocal.getViewContext();
    vctx.setAction("forums");
    return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
  }
}
