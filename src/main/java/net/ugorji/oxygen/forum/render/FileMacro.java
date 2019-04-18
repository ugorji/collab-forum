/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.render;

import java.io.Writer;
import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumEngine;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.markup.MarkupMacro;
import net.ugorji.oxygen.markup.MarkupMacroParameters;
import net.ugorji.oxygen.markup.MarkupRenderContext;
import net.ugorji.oxygen.util.StringUtils;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebConstants;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class FileMacro implements MarkupMacro {
  // - $uploaddir/users/username/$uploadedFile
  // - $uploaddir/net.ugorji.oxygen/forum/topic/$topicId/$attachment
  // {file:user|img=true|file=/}
  // {file:topic|img=true|file=/}
  public void close() {}

  public void execute(Writer writer, MarkupRenderContext rc, MarkupMacroParameters params)
      throws Exception {
    boolean image = false;
    boolean userType = false;
    boolean topicType = false;
    String file = "null";
    String s = null;
    if (!(StringUtils.isBlank(s = params.get("file")))) {
      file = s;
    }
    if (!(StringUtils.isBlank(s = params.get("img"))) && s.equals("true")) {
      image = true;
    }
    if (!(StringUtils.isBlank(s = params.get(0))) && s.equals("user")) {
      userType = true;
    }
    if (!(StringUtils.isBlank(s = params.get(0))) && s.equals("topic")) {
      topicType = true;
    }

    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    ViewContext vctx = WebLocal.getViewContext();
    ForumEngine engine = ForumLocal.getForumEngine();
    String user = wctx.getUserName();
    String uploadPath = engine.getProperty(WebConstants.UPLOAD_BASE_URL_KEY);
    if (image) {
      if (userType) {
        s = "<img src=\"" + uploadPath + "/users/" + user + "/" + file + "\" " + file + " />";
      } else if (topicType) {
        s =
            "<img src=\""
                + uploadPath
                + "/oxygen/forum/topic/"
                + vctx.getAttribute(ForumConstants.ID_KEY)
                + "/"
                + file
                + "\" "
                + file
                + " />";
      }
    } else {
      if (userType) {
        s =
            "<a href=\""
                + uploadPath
                + "/users/"
                + user
                + "/"
                + file
                + "\" "
                + file
                + " >"
                + file
                + "</a>";
      } else if (topicType) {
        s =
            "<a href=\""
                + uploadPath
                + "/oxygen/forum/topic/"
                + vctx.getAttribute(ForumConstants.ID_KEY)
                + "/"
                + file
                + "\" "
                + file
                + " >"
                + file
                + "</a>";
      }
    }
    writer.write(s);
  }
}
