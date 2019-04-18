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
import net.ugorji.oxygen.web.WebLocal;

public class DeleteForumAction extends GenericWebAction {
  {
    setFlag(FLAG_WRITE_ACTION);
  }

  public int processAction() throws Exception {

    ViewContext vctx = WebLocal.getViewContext();
    Long id = (Long) vctx.getAttribute(ForumConstants.ID_KEY);
    ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
    Forum forum = (Forum) dao.get(Forum.class, id, true);
    Forum forum2 = forum.getParentForum();
    dao.delete(forum);

    ForumEvent fevent = new ForumEvent(forum, ForumEvent.FORUM, ForumEvent.DELETE);
    ForumLocal.getForumEngine().getForumEventManager().publish(fevent);

    vctx.setAction("manageforums");
    vctx.setAttribute(ForumConstants.ID_KEY, forum2.getId());
    vctx.setAttribute(ForumConstants.FORUMID_KEY, forum2.getId());
    vctx.setAttribute(ForumConstants.TOPICID_KEY, null);
    return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
  }
}
