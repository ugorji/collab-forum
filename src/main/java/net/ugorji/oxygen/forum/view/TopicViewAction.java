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
import net.ugorji.oxygen.forum.data.Topic;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.TemplateHandler;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebLocal;

public class TopicViewAction extends GenericWebAction {
  public int render() throws Exception {
    ViewContext vctx = WebLocal.getViewContext();
    Long id = (Long) vctx.getAttribute(ForumConstants.ID_KEY);

    vctx.setAttribute(ForumConstants.TOPICID_KEY, id);

    Topic t = BaseForumModel.getTopic();
    vctx.setAttribute(ForumConstants.FORUMID_KEY, t.getForum().getId());
    vctx.setAttribute(ForumConstants.TOPICID_KEY, t.getId());

    TemplateHandler thdlr = WebLocal.getTemplateHandler();
    thdlr.render();
    return super.render();
  }
}
