/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.extension;

import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import org.apache.commons.logging.LogFactory;
import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumEngine;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.forum.ForumPruneManager;
import net.ugorji.oxygen.forum.data.Forum;
import net.ugorji.oxygen.forum.data.ForumDAO;

public class ForumAutoPruneHandler implements Runnable {
  public void run() {
    final ForumEngine engine = ForumLocal.getForumEngine();
    TimerTask tt =
        new TimerTask() {
          public void run() {
            try {
              ForumLocal.setForumEngine(engine);
              ForumAutoPruneHandler.this.autoprune();
            } catch (Exception exc) {
              LogFactory.getLog(ForumAutoPruneHandler.class).error("error", exc);
            } finally {
              ForumLocal.setForumEngine(null);
            }
          }
        };
    engine.addTask(tt, ForumConstants.ONE_DAY, ForumConstants.ONE_DAY);
  }

  public void autoprune() throws Exception {
    ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
    ForumPruneManager pm = ForumLocal.getForumEngine().getForumPruneManager();
    List list = dao.getAll(Forum.class);
    for (Iterator itr = list.iterator(); itr.hasNext(); ) {
      Forum f = (Forum) itr.next();
      pm.prune(f);
    }
  }
}
