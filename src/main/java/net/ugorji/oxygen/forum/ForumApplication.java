/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

import java.util.Properties;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ugorji.oxygen.forum.data.Forum;
import net.ugorji.oxygen.forum.data.ForumDAO;
import net.ugorji.oxygen.forum.data.Topic;
import net.ugorji.oxygen.util.OxygenUtils;
import net.ugorji.oxygen.web.*;

public class ForumApplication extends BaseWebApplicationImpl {

  public WebInteractionContext newWebInteractionContext(
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    return new ForumServletInteractionContext(sctx, request, response);
  }

  public WebInteractionContext newWebInteractionContext(
      PortletRequest request, PortletResponse response) throws Exception {
    throw new UnsupportedOperationException();
  }

  public String getRedirectAfterPostSuffix() {
    return engine().getProperty(ForumConstants.SERVLET_MAPPING_PREFIX_KEY)
        + engine().getProperty(ForumConstants.REDIRECT_AFTER_POST_SUFFIX_KEY);
  }

  public String getEncoding() {
    return engine().getProperty(ForumConstants.ENCODING_KEY);
  }

  public void atInitOfRequest() throws Exception {
    super.atInitOfRequest();
    ForumLocal.setForumEngine(engine());
  }

  public void atStartOfRequest() throws Exception {
    super.atStartOfRequest();
    WebErrorModel.setFreemarkerTemplateErrorHandler(true);
    ViewContext vctx = WebLocal.getViewContext();
    WebInteractionContext webctx = WebLocal.getWebInteractionContext();
    ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();

    Forum ff = null;
    Topic ft = null;
    Long lt = (Long) webctx.getSessionAttribute(ForumConstants.TOPICID_KEY);
    Long lf = (Long) webctx.getSessionAttribute(ForumConstants.FORUMID_KEY);
    OxygenUtils.info(
        "XXXX: ForumApplication.atStartOfRequest (from session): forumId: "
            + lf
            + ", topicId: "
            + lt);

    if (lf != null) {
      ff = (Forum) dao.get(Forum.class, lf, true);
      if (ff == null) {
        lf = null;
      }
    }
    if (lt != null) {
      ft = (Topic) dao.get(Topic.class, lt, true);
      if (ft == null) {
        lt = null;
      } else {
        ff = ft.getForum();
        lf = ff.getId();
      }
    }
    if (lf == null) {
      ff = dao.getRootForum();
      lf = ff.getId();
    }

    vctx.setAttribute(ForumConstants.TOPICID_KEY, lt);
    vctx.setAttribute(ForumConstants.FORUMID_KEY, lf);
    OxygenUtils.info(
        "XXXX: ForumApplication.atStartOfRequest (finally): forumId: " + lf + ", topicId: " + lt);
  }

  public void atEndOfRequest() {
    try {
      super.atEndOfRequest();
      ViewContext vctx = WebLocal.getViewContext();
      WebInteractionContext webctx = WebLocal.getWebInteractionContext();
      Long l = (Long) vctx.getAttribute(ForumConstants.FORUMID_KEY);
      webctx.setSessionAttribute(ForumConstants.FORUMID_KEY, l);
      l = (Long) vctx.getAttribute(ForumConstants.TOPICID_KEY);
      webctx.setSessionAttribute(ForumConstants.TOPICID_KEY, l);
      WebErrorModel.setFreemarkerTemplateErrorHandler(false);
    } finally {
      engine().getForumDAO().closeCurrentSession();
      ForumLocal.setForumEngine(null);
    }
  }

  public void checkAccess() throws Exception {
    boolean securityCheckingEnabled =
        "true".equals(engine().getProperty(ForumConstants.SECURITY_CHECKING_ENABLED_KEY));
    if (!securityCheckingEnabled) {
      return;
    }
    String adminrole = engine().getProperty(ForumConstants.ADMIN_ROLE_KEY);
    ViewContext vctx = WebLocal.getViewContext();
    WebAction waction = getAction(vctx.getAction());
    if (waction.isFlagSet(WebAction.FLAG_ADMIN_ACTION)) {
      WebInteractionContext webctx = WebLocal.getWebInteractionContext();
      if (!(webctx.isUserInRole(adminrole))) {
        throw new OxygenWebException("You do not have admin privileges");
      }
    }
  }

  public boolean isRetainReferenceToLastViewContext() {
    return false;
  }

  public void shutdown() {
    engine().close();
    sctx = null;
    pctx = null;
  }

  private ForumEngine engine() {
    return (ForumEngine) oce();
  }

  public WebUserSession newWebUserSession() throws Exception {
    return new ForumUserSession();
  }

  protected WebContainerEngine createWebContainerEngine(Properties p) throws Exception {
    return new ForumEngine(p);
  }

  protected void updatePropertiesForInit(Properties p) {}

  protected String getActionNotExistResourceKey() {
    return "action_not_exist";
  }
}
