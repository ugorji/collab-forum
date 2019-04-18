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
import java.nio.file.Files;
import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class ViewFileAction extends GenericWebAction {

  public int render() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    ViewContext vctx = WebLocal.getViewContext();
    String path = (String) vctx.getAttribute(ForumConstants.PATH_KEY);
    // just serve the file at uploadDir/<path> - copy it to outputstream.
    File f = ForumLocal.getForumEngine().getUploadDirectory();
    Files.copy(f.toPath().resolve(path), wctx.getOutputStream());
    return RENDER_COMPLETED;
  }

  public String getContentType() {
    return "application/octet-stream";
  }
}
