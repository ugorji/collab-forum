/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import java.io.PrintWriter;
import java.io.StringReader;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class MarkupToHTMLAction extends GenericWebAction {
  public int render() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    PrintWriter w = wctx.getWriter();
    try {
      StringReader r = new StringReader(wctx.getParameter("text"));
      ForumLocal.getForumEngine().getForumRenderEngine().render(w, r);
    } catch (Exception exc) {
      w.print("<pre>");
      exc.printStackTrace(w);
      w.print("</pre>");
    } finally {
      w.flush();
    }
    return RENDER_COMPLETED;
  }
}
