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
import net.ugorji.oxygen.forum.ForumEvent;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.forum.data.ForumDAO;
import net.ugorji.oxygen.forum.data.Topic;
import net.ugorji.oxygen.web.*;

public class ViewTopicAction extends GenericWebAction {
  public int processAction() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    String s = wctx.getParameter("topicview");
    if (s != null) {
      ForumLocal.getForumUserSession()
          .setChronologicalTopicView(ForumConstants.CHRONOLOGICAL_VIEW.equals(s));
    }
    s = wctx.getParameter("showmessagethread");
    if (s != null) {
      ForumLocal.getForumUserSession().setShowMessageThread("true".equals(s));
    }
    return ACTION_PROCESSING_COMPLETED;
  }

  public int render() throws Exception {
    ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
    ViewContext vctx = WebLocal.getViewContext();
    Long id = (Long) vctx.getAttribute(ForumConstants.ID_KEY);

    Topic t = (Topic) dao.get(Topic.class, id, true);
    vctx.setAttribute(ForumConstants.FORUMID_KEY, t.getForum().getId());
    vctx.setAttribute(ForumConstants.TOPICID_KEY, t.getId());

    TemplateHandler thdlr = WebLocal.getTemplateHandler();
    thdlr.render();

    // throw a Topic READ Event
    ForumEvent fevent = new ForumEvent(t, ForumEvent.TOPIC, ForumEvent.READ);
    ForumLocal.getForumEngine().getForumEventManager().publish(fevent);
    return RENDER_COMPLETED;
  }
}
