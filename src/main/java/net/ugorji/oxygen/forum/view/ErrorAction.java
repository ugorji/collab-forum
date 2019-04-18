/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import net.ugorji.oxygen.forum.ForumEngine;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.web.*;

public class ErrorAction extends GenericWebAction {
  public int render() throws Exception {
    ViewContext tctx = WebLocal.getViewContext();
    ForumEngine fe = ForumLocal.getForumEngine();
    boolean showMessageOnly0 =
        "true".equals(fe.getProperty(WebConstants.SHOW_ERROR_MESSAGE_ONLY_KEY));
    tctx.setModel("error", new WebErrorModel(showMessageOnly0, false));
    TemplateHandler thdlr = WebLocal.getTemplateHandler();
    thdlr.render();
    return RENDER_COMPLETED;
  }
}
