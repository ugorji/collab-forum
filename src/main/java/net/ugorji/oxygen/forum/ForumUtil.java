/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

import java.io.StringReader;
import java.io.Writer;
import net.ugorji.oxygen.forum.data.DataEntity;
import net.ugorji.oxygen.forum.data.Topic;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class ForumUtil {
  public static void writeTextAsHtmlMarkup(String s, Writer w) throws Exception {
    try {
      StringReader r = new StringReader(s);
      ForumLocal.getForumEngine().getForumRenderEngine().render(w, r);
      w.flush();
    } catch (Exception exc) {
      w.write(WebLocal.getI18n().str("markup_cannot_be_parsed"));
    }
  }

  public static boolean checkAccessConsideringLockedStatusAndRoles(
      DataEntity de, boolean throwExceptionOnError) throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    ForumEngine engine = ForumLocal.getForumEngine();
    boolean securityCheckingEnabled =
        "true".equals(engine.getProperty(ForumConstants.SECURITY_CHECKING_ENABLED_KEY));
    if (securityCheckingEnabled) {
      String adminrole = engine.getProperty(ForumConstants.ADMIN_ROLE_KEY);
      if (!de.isWrite() && !(wctx.isUserInRole(adminrole))) {
        if (throwExceptionOnError) {
          throw new Exception(
              "The DataEntity is has its write flag turned off, and cannot be edited");
        }
        return false;
      }
      if (de instanceof Topic) {
        return checkAccessConsideringLockedStatusAndRoles(
            ((Topic) de).getForum(), throwExceptionOnError);
      }
    }
    return true;
  }

  // call this to know whether to challenge with captcha
  public static boolean isCaptchaEnabled() {
    boolean b1 =
        "true".equals(ForumLocal.getForumEngine().getProperty(ForumConstants.CAPTCHA_ENABLED_KEY));
    return (b1 && !WebLocal.getWebUserSession().isCaptchaChecked());
  }
}
