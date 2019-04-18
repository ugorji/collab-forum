/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.extension;

import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumEngine;
import net.ugorji.oxygen.forum.ForumIndexingManager;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.util.Plugin;

public class GeneralPostInitActionsHandler implements Plugin {
  public void init() {}

  public void close() {}

  public void start() throws Exception {
    ForumEngine fe = ForumLocal.getForumEngine();
    ForumIndexingManager fim = fe.getForumIndexingManager();
    String s = fe.getProperty(ForumConstants.INDEX_AT_STARTUP_KEY);
    if ("incremental".equals(s)) {
      fim.incrementalIndexFiles();
    } else if ("recreate".equals(s)) {
      fim.deleteIndexFiles();
      fim.indexAll();
    }

    s = fe.getProperty(ForumConstants.RECALCULATE_PERSISTED_STATISTICS_AT_STARTUP_KEY);
    if ("true".equals(s)) {
      fe.getForumStatisticsManager().recalculatePersistedStatistics();
    }
  }
}
