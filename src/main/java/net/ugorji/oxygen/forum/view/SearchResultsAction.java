/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumIndexingManager;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.forum.data.ForumDAO;
import net.ugorji.oxygen.forum.data.Topic;
import net.ugorji.oxygen.markup.indexing.HitsHandler;
import net.ugorji.oxygen.util.StringUtils;
import net.ugorji.oxygen.web.GenericWebAction;
import net.ugorji.oxygen.web.TemplateHandler;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class SearchResultsAction extends GenericWebAction {
  public static final SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");

  protected void preRender() throws Exception {
    // System.out.println(">>>>>>>>> SearchResultsAction >>>>>>>>");
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    String s = null;

    Map q = new HashMap();
    if (!StringUtils.isBlank(s = wctx.getParameter(ForumConstants.SEARCH_INDEX_POSTER))) {
      q.put(ForumConstants.SEARCH_INDEX_POSTER, s);
    }
    if (!StringUtils.isBlank(s = wctx.getParameter(ForumConstants.SEARCH_INDEX_TITLE))) {
      q.put(ForumConstants.SEARCH_INDEX_TITLE, s);
    }
    if (!StringUtils.isBlank(s = wctx.getParameter(ForumConstants.SEARCH_INDEX_TOPIC_CONTENTS))) {
      q.put(ForumConstants.SEARCH_INDEX_TOPIC_CONTENTS, s);
    }
    if (!StringUtils.isBlank(s = wctx.getParameter(ForumConstants.SEARCH_INDEX_FORUM_ID))) {
      q.put(ForumConstants.SEARCH_INDEX_FORUM_ID, s);
    }
    Date[] d = new Date[2];
    if (!StringUtils.isBlank(
        s = wctx.getParameter(ForumConstants.SEARCH_INDEX_TOPIC_LAST_POST_DATE + ".0"))) {
      d[0] = dateformat.parse(s);
    }
    if (!StringUtils.isBlank(
        s = wctx.getParameter(ForumConstants.SEARCH_INDEX_TOPIC_LAST_POST_DATE + ".1"))) {
      d[1] = dateformat.parse(s);
    }
    if (d[0] != null || d[1] != null) {
      q.put(ForumConstants.SEARCH_INDEX_TOPIC_LAST_POST_DATE, d);
    }

    ForumIndexingManager fim = ForumLocal.getForumEngine().getForumIndexingManager();
    final ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
    final LinkedHashMap map = new LinkedHashMap();
    HitsHandler hhdlr =
        new HitsHandler() {
          public void handleHits(Hits hits) throws Exception {
            int numhits = hits.length();
            int maxHits =
                Integer.parseInt(
                    ForumLocal.getForumEngine()
                        .getProperty(ForumConstants.SEARCH_MAX_NUM_HITS_KEY));
            numhits = Math.min(maxHits, numhits);
            for (int i = 0; i < numhits; i++) {
              Document doc = hits.doc(i);
              Long id = new Long(doc.get(ForumConstants.SEARCH_INDEX_TOPIC_ID));
              Topic t = (Topic) dao.get(Topic.class, id, true);
              map.put(t, new Double((double) hits.score(i)));
            }
          }
        };
    fim.search(q, hhdlr);

    ViewContext tctx = WebLocal.getViewContext();
    SearchResultsModel model = new SearchResultsModel();
    model.map = map;
    tctx.setModel("searchresults", model);
  }

  public int render() throws Exception {
    preRender();
    // System.out.println(">>>>>>>>>>>>> pre-render done >>>>>>>>>>> - now calling render");
    TemplateHandler thdlr = WebLocal.getTemplateHandler();
    thdlr.render();
    return super.render();
  }

  public static class SearchResultsModel {
    private LinkedHashMap map;

    public Topic[] getTopics() {
      return (Topic[]) map.keySet().toArray(new Topic[0]);
    }

    public double getScore(Topic t) {
      return ((Double) map.get(t)).doubleValue();
    }
  }
}
