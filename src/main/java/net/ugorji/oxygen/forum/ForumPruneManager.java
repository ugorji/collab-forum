/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import net.ugorji.oxygen.forum.data.Forum;
import net.ugorji.oxygen.forum.data.ForumDAO;
import net.ugorji.oxygen.forum.data.Topic;

public class ForumPruneManager {
  public void prune(Forum forum) throws Exception {
    int pruneDays = forum.getPrunedays();
    prune(forum, pruneDays);
  }

  public void prune(Forum forum, int pruneDays) throws Exception {
    if (pruneDays > 0) {
      ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR, (-1 * pruneDays));
      Date d = cal.getTime();
      List list = dao.getTopicsForForumUnchangedSince(forum, d);
      for (Iterator itr = list.iterator(); itr.hasNext(); ) {
        Topic t = (Topic) itr.next();
        t.getAllposts().clear();
        dao.delete(t);
      }
      ForumLocal.getForumEngine().getForumIndexingManager().removeTopicsFromIndex(list);
      ForumLocal.getForumEngine()
          .getForumStatisticsManager()
          .recalculatePersistedStatistics(forum, false);
    }
  }
}
