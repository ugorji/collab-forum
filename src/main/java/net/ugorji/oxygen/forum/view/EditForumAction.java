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
import net.ugorji.oxygen.forum.data.Forum;
import net.ugorji.oxygen.forum.data.ForumDAO;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

// keep a temp forum, and configure that guy
public class EditForumAction extends GenericWebAction {
  {
    setFlag(FLAG_WRITE_ACTION);
  }

  public int processAction() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
    String s = null;
    Long id = null;
    if ((s = wctx.getParameter("forumid")) != null) {
      id = new Long(s);
    }
    Forum f = null;
    if (id == null || id < 0) {
      f = new Forum();
    } else {
      f = (Forum) dao.get(Forum.class, id, true);
    }
    f.setTitle(wctx.getParameter("name"));
    f.setParentForum(
        (Forum) dao.get(Forum.class, new Long(wctx.getParameter("parentforum")), true));
    f.setDetails((wctx.getParameter("description")));
    f.setPrunedays(Integer.parseInt(wctx.getParameter("prunedays")));
    f.setHighvisibility("true".equals(wctx.getParameter("highvisibility")));
    f.setWrite("true".equals(wctx.getParameter("write")));
    f.setRead("true".equals(wctx.getParameter("read")));
    dao.saveOrUpdate(f);

    ForumEvent fevent = new ForumEvent(f, ForumEvent.FORUM, ForumEvent.UPDATE);
    ForumLocal.getForumEngine().getForumEventManager().publish(fevent);

    ViewContext vctx = WebLocal.getViewContext();
    vctx.setAction("manageforums");
    vctx.setAttribute(ForumConstants.ID_KEY, f.getParentForum().getId());
    vctx.setAttribute(ForumConstants.FORUMID_KEY, f.getParentForum().getId());
    vctx.setAttribute(ForumConstants.TOPICID_KEY, null);
    return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
  }
}
