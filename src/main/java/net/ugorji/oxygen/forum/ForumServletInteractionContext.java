/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ugorji.oxygen.util.StringUtils;
import net.ugorji.oxygen.web.ServletInteractionContext;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebConstants;
import net.ugorji.oxygen.web.WebUtils;

public class ForumServletInteractionContext extends ServletInteractionContext {
  private static Long ZERO = new Long(0);

  // url = ctxPath + prefix + / + action [+ / + id]
  public ForumServletInteractionContext(
      ServletContext sctx, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    super(sctx, request, response);
  }

  public ViewContext toViewContext() throws Exception {
    String s = null;
    Long id = null;
    String action = null;
    String rpath = request.getParameter("path");
    if ((s = request.getParameter("action")) != null) {
      action = s;
    }
    if ((s = request.getParameter("id")) != null) {
      id = getLong(s);
    }
    ViewContext vctx = new ViewContext();
    String path = request.getRequestURI();
    // System.out.println("request uri: " + path);
    // path = URLDecoder.decode(path, "UTF-8");
    path = StringUtils.decodeURLEncodedPercentHexHex(path);
    String ctxpath = request.getContextPath();
    path = path.substring(ctxpath.length());
    String urlprefix =
        ForumLocal.getForumEngine().getProperty(ForumConstants.SERVLET_MAPPING_PREFIX_KEY);
    path = path.substring(urlprefix.length());
    // System.out.println("path: " + path); // /forums or /topic/17 or /files/0/a/b/c.txt
    int j = 0;
    int k = 0;
    j = k + 1;
    k = path.indexOf("/", j);
    if (k < 0) {
      action = path.substring(j);
    } else {
      action = path.substring(j, k);
      j = k + 1;
      k = path.indexOf("/", j);
      if (k < 0) {
        id = getLong(path.substring(j));
      } else {
        id = getLong(path.substring(j, k));
        j = k + 1;
        rpath = path.substring(j);
      }
    }
    vctx.setLocale(request.getLocale());
    vctx.setAction(action);
    vctx.setAttribute(ForumConstants.ID_KEY, id);
    vctx.setAttribute(ForumConstants.PATH_KEY, rpath);
    // System.out.println("action: " + action + " id: " + id);
    // vctx.setAttribute(ForumConstants.QUERYPARAMETERS_KEY, new
    // HashMap(request.getParameterMap()));
    return vctx;
  }

  public String toURLString(ViewContext v, Map extraparams) {
    StringBuffer sbuf =
        new StringBuffer()
            .append(getBaseURL())
            .append(
                ForumLocal.getForumEngine().getProperty(ForumConstants.SERVLET_MAPPING_PREFIX_KEY))
            .append("/")
            .append(v.getAction());
    Long id = (Long) v.getAttribute(ForumConstants.ID_KEY);
    if (id != null) {
      sbuf.append("/").append(id);
    }

    Map m = extraparams;
    if (m != null && m.size() > 0) {
      sbuf.append("?1=1");
      WebUtils.appendQueryParameters(sbuf, m);
    }
    return sbuf.toString();
  }

  public String getBaseURL() {
    String s = ForumLocal.getForumEngine().getProperty(WebConstants.BASE_URL_KEY);
    if (StringUtils.isBlank(s)) s = super.getBaseURL();
    return s;
  }

  // handle NumberFormatException gracefully
  private Long getLong(String s) throws Exception {
    if (s.equals("-")) return ZERO;
    return new Long(s);
    // try {
    //  return new Long(s);
    // } catch(NumberFormatException nfe) { }
    // return ForumConstants.LONG_1;
  }
}
