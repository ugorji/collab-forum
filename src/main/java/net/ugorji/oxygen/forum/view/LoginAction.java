/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import java.io.Serializable;
import net.ugorji.oxygen.web.*;

public class LoginAction extends GenericWebAction {
  public int render() throws Exception {
    ViewContext tctx = WebLocal.getViewContext();
    tctx.setModel("login", new LoginModel());
    TemplateHandler thdlr = WebLocal.getTemplateHandler();
    thdlr.render();
    return RENDER_COMPLETED;
  }

  public static class LoginModel implements Serializable {
    private boolean errorOccurred;

    public LoginModel() throws Exception {
      WebInteractionContext wctx = WebLocal.getWebInteractionContext();
      errorOccurred = "true".equals(wctx.getParameter("loginerror"));
    }

    public boolean isErrorOccurred() {
      return errorOccurred;
    }
  }
}
