/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

import java.io.Reader;
import java.io.Writer;
import net.ugorji.oxygen.forum.render.DefaultForumRenderEngine;
import net.ugorji.oxygen.markup.MarkupRenderContext;
import net.ugorji.oxygen.markup.MarkupRenderEngine;
import net.ugorji.oxygen.util.CloseUtils;
import net.ugorji.oxygen.util.Closeable;

public class ForumRenderEngine implements Closeable {

  private DefaultForumRenderEngine re;

  public ForumRenderEngine() throws Exception {
    re = new DefaultForumRenderEngine();
  }

  public void render(Writer w, Reader in) throws Exception {
    re.render(w, in);
  }

  public void close() {
    CloseUtils.close(re);
  }

  public MarkupRenderEngine getRenderEngine() {
    return re;
  }

  public MarkupRenderContext newRenderContext() {
    return re.newRenderContext();
  }
}
