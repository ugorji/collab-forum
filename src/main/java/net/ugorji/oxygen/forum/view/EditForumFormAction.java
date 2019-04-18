/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.forum.data.Forum;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebLocal;

public class EditForumFormAction extends ForumViewAction {
  {
    setFlag(FLAG_WRITE_ACTION);
  }

  public int render() throws Exception {
    ViewContext tctx = WebLocal.getViewContext();
    EditForumFormModel model = new EditForumFormModel();
    tctx.setModel("editforumform", model);
    return super.render();
  }

  public static class EditForumFormModel {
    public Forum getForum() {
      Forum f = BaseForumModel.getForum();
      if (f == null || f.getId() < 0) {
        f = new Forum();
        f.setParentForum(ForumLocal.getForumEngine().getForumDAO().getRootForum());
        f.setTitle("");
        f.setDetails("");
      }
      return f;
    }
  }
}
