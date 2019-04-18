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
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.forum.data.ForumDAO;
import net.ugorji.oxygen.forum.data.Topic;
import net.ugorji.oxygen.forum.data.User;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class SubscribeTopicAction extends GenericWebAction {

  {
    setFlag(FLAG_WRITE_ACTION);
  }

  protected boolean unsubscribe = false;

  public int processAction() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    ViewContext vctx = WebLocal.getViewContext();
    ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
    Long id = (Long) vctx.getAttribute(ForumConstants.ID_KEY);
    Topic t = (Topic) dao.get(Topic.class, id, true);
    User user = dao.getUser(wctx.getUserName());
    // System.out.println(user.getName());
    dao.setWatchTopic(user, t, !unsubscribe);

    vctx.setAction("topic");
    vctx.setAttribute(ForumConstants.ID_KEY, t.getId());
    vctx.setAttribute(ForumConstants.FORUMID_KEY, t.getForum().getId());
    vctx.setAttribute(ForumConstants.TOPICID_KEY, t.getId());
    return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
  }
}
