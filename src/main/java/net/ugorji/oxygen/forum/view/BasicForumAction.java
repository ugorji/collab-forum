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
import net.ugorji.oxygen.web.TemplateHandler;
import net.ugorji.oxygen.web.WebLocal;

public class BasicForumAction extends GenericWebAction {

  public int render() throws Exception {
    TemplateHandler thdlr = WebLocal.getTemplateHandler();
    thdlr.render();
    return super.render();
  }
}
