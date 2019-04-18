/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.TemplateHandler;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebLocal;

public class ForumViewAction extends GenericWebAction {

  public int render() throws Exception {
    ViewContext vctx = WebLocal.getViewContext();
    Long id = (Long) vctx.getAttribute(ForumConstants.ID_KEY);
    if (id != null) {
      vctx.setAttribute(ForumConstants.FORUMID_KEY, id);
      vctx.setAttribute(ForumConstants.TOPICID_KEY, null);
    }

    TemplateHandler thdlr = WebLocal.getTemplateHandler();
    thdlr.render();
    return super.render();
  }
}
