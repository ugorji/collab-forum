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
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

// Not implemented or supported ... so make it abstract for now
public abstract class LockOrUnlockTopicAction extends GenericWebAction {

  {
    setFlag(FLAG_WRITE_ACTION);
  }

  public int processAction() throws Exception {
    if (true) throw new UnsupportedOperationException();
    ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();

    Long topicid = new Long(wctx.getParameter("topicid"));

    Topic t = (Topic) dao.get(Topic.class, topicid, true);
    t.setWrite("true".equals(wctx.getParameter("write")));

    dao.saveOrUpdate(t);

    ViewContext vctx = WebLocal.getViewContext();
    vctx.setAction("topic");
    vctx.setAttribute(ForumConstants.ID_KEY, t.getId());
    vctx.setAttribute(ForumConstants.TOPICID_KEY, t.getId());
    vctx.setAttribute(ForumConstants.FORUMID_KEY, t.getForum().getId());
    return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
  }
}
