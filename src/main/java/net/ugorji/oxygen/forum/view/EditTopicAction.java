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
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class EditTopicAction extends GenericWebAction {
  {
    setFlag(FLAG_WRITE_ACTION);
  }

  public int processAction() throws Exception {
    ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();

    Long topicid = new Long(wctx.getParameter("topicid"));
    String title = wctx.getParameter("title");
    Topic t = (Topic) dao.get(Topic.class, topicid, true);
    t.setTitle(title);

    t.setHighvisibility("true".equals(wctx.getParameter("highvisibility")));
    t.setWrite("true".equals(wctx.getParameter("write")));
    t.setRead("true".equals(wctx.getParameter("read")));

    dao.saveOrUpdate(t);

    ForumEvent fevent = new ForumEvent(t, ForumEvent.TOPIC, ForumEvent.UPDATE);
    ForumLocal.getForumEngine().getForumEventManager().publish(fevent);

    ViewContext vctx = WebLocal.getViewContext();
    vctx.setAction("topic");
    vctx.setAttribute(ForumConstants.ID_KEY, t.getId());
    vctx.setAttribute(ForumConstants.TOPICID_KEY, t.getId());
    vctx.setAttribute(ForumConstants.FORUMID_KEY, t.getForum().getId());
    return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
  }
}
