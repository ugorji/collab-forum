/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

import java.util.Locale;
import net.ugorji.oxygen.util.OxyLocal;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class ForumLocal {

  public static ForumEngine getForumEngine() {
    return (ForumEngine) OxyLocal.get(ForumEngine.class);
  }

  public static void setForumEngine(ForumEngine engine) {
    OxyLocal.set(ForumEngine.class, engine);
    if (engine != null) {
      WebLocal.setProperties(engine.getProperties());
      if (engine.getI18nManager() != null) {
        WebInteractionContext wctx = WebLocal.getWebInteractionContext();
        Locale locale = (wctx == null) ? null : wctx.getLocale();
        WebLocal.setI18n(engine.getI18nManager().getI18n(locale));
      }
    }
  }

  public static ForumUserSession getForumUserSession() {
    return (ForumUserSession) WebLocal.getWebUserSession();
  }
}
