/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import java.io.File;
import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumEngine;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class UploadFileAction extends GenericWebAction {

  {
    setFlag(FLAG_WRITE_ACTION);
  }

  public int processAction() throws Exception {
    // System.out.println("in upload");
    ForumEngine engine = ForumLocal.getForumEngine();
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    // int maxUploadSize = 1000 * 1000 * 50; // 50MB
    File uploadDir = engine.getUploadDirectory();
    // upload.setRepositoryPath(uploadDir.getAbsolutePath());
    File[] files = wctx.getUploadedFiles();
    // System.out.println("items: " + items);
    String context = wctx.getParameter("upload.context");
    String subpath = wctx.getParameter("upload.subpath");
    String nextview = wctx.getParameter("upload.nextview");
    File fbase = new File(uploadDir, subpath);
    fbase.mkdirs();
    for (int i = 0; i < files.length; i++) {
      File f = new File(fbase, files[i].getName());
      files[i].renameTo(f);
    }

    ViewContext vctx = WebLocal.getViewContext();
    vctx.setAttribute(ForumConstants.PATH_KEY, context);
    // TODO: if a user is uploading, shouldn't we know the userid and store it?
    if (nextview.equals("topic")) {
      vctx.setAttribute(ForumConstants.ID_KEY, new Long(context));
    }

    vctx.setAction(nextview);
    return ACTION_PROCESSING_COMPLETED | REDIRECT_AFTER_POST;
  }
}
