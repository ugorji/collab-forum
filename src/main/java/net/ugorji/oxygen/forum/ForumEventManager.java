/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

import java.util.*;
import org.apache.commons.logging.LogFactory;
import net.ugorji.oxygen.util.OxygenUtils;

public class ForumEventManager {
  private Map listeners = new HashMap();

  public ForumEventManager() throws Exception {
    Properties p = ForumLocal.getForumEngine().getProperties();
    String prefix = ForumConstants.LISTENER_KEY_PREFIX;
    Properties pp = new Properties();
    OxygenUtils.extractProps(p, pp, prefix, true);
    for (Enumeration enum0 = pp.propertyNames(); enum0.hasMoreElements(); ) {
      String k = (String) enum0.nextElement();
      String v = pp.getProperty(k);
      ForumEventListener fe = (ForumEventListener) Class.forName(v).newInstance();
      listeners.put(k, fe);
    }
  }

  public void publish(ForumEvent evt) {
    for (Iterator itr = listeners.values().iterator(); itr.hasNext(); ) {
      ForumEventListener fe = (ForumEventListener) itr.next();
      try {
        fe.handleEvent(evt);
      } catch (Exception exc) {
        LogFactory.getLog(getClass()).error("error", exc);
      }
    }
  }
}
