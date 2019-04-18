/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.ugorji.oxygen.util.FreemarkerTemplateHelper;
import net.ugorji.oxygen.util.StringUtils;
import net.ugorji.oxygen.web.TemplateHandler;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class ForumTemplateHandler extends FreemarkerTemplateHelper implements TemplateHandler {
  private String basefile;

  public ForumTemplateHandler() {
    ForumEngine engine = ForumLocal.getForumEngine();
    basefile = engine.getProperty(ForumConstants.TEMPLATE_BASEFILE_KEY);
    List l =
        StringUtils.tokens(engine.getProperty(ForumConstants.TEMPLATE_NAMES_KEY), ", ", true, true);
    l.add(0, null);
    String[] largs = (String[]) l.toArray(new String[0]);
    largs[0] = "/net/ugorji/oxygen/web";
    for (int i = 1; i < largs.length; i++) {
      largs[i] = "/net/ugorji/oxygen/forum/templates/" + largs[i];
    }
    Map staticModelStrings = new HashMap();
    staticModelStrings.put("ForumConstants", "net.ugorji.oxygen.forum.ForumConstants");
    staticModelStrings.put("ForumUtil", "net.ugorji.oxygen.forum.ForumUtil");
    staticModelStrings.put("t", "net.ugorji.oxygen.forum.view.BaseForumModel");
    staticModelStrings.put("StringUtils", "net.ugorji.oxygen.util.StringUtils");

    init(largs, staticModelStrings);
  }

  public void render() throws Exception {
    render(basefile);
  }

  public void render(String file) throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    ViewContext tctx = WebLocal.getViewContext();
    tctx.setModel("writer", wctx.getWriter());
    write(file, tctx.getModels(), wctx.getWriter());
  }
}
