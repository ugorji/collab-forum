/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.SyndFeedOutput;
import java.util.ArrayList;
import java.util.List;
import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumEngine;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.forum.data.Post;
import net.ugorji.oxygen.forum.data.Topic;
import net.ugorji.oxygen.util.StringUtils;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebInteractionContext;
import net.ugorji.oxygen.web.WebLocal;

public class RSSAction extends SearchResultsAction {
  public int render() throws Exception {
    preRender();
    WebInteractionContext request = WebLocal.getWebInteractionContext();
    ForumEngine fe = ForumLocal.getForumEngine();
    ViewContext tctx = WebLocal.getViewContext();
    SearchResultsModel model = (SearchResultsModel) tctx.getModel("searchresults");

    String s = null;
    String baseurl = request.getBaseURL();
    SyndFeed feed = new SyndFeedImpl();
    s = request.getParameter("rss.format");
    s = (StringUtils.isBlank(s) ? fe.getProperty(ForumConstants.RSS_FORMAT_KEY, "rss_2.0") : s);
    feed.setFeedType(s);
    feed.setTitle(BaseForumModel.getConfigProperty("net.ugorji.oxygen.forum.title"));
    feed.setLink(baseurl);
    feed.setDescription(" ... ");

    List entries = new ArrayList();

    Topic[] topics = model.getTopics();
    for (int i = 0; i < topics.length; i++) {
      SyndEntry entry = new SyndEntryImpl();
      entry.setTitle(topics[i].getTitle());
      Long lastPostId = fe.getForumStatisticsManager().getLastPostIdForTopic(topics[i].getId());
      Post p = (Post) fe.getForumDAO().get(Post.class, lastPostId, true);
      entry.setPublishedDate(p.getDate());
      entry.setLink(BaseForumModel.getURL("topic", topics[i].getId()));
      entry.setAuthor(topics[i].getAuthor().getName());
      SyndContent description = new SyndContentImpl();
      description.setType("text/plain");
      description.setValue("...");
      entry.setDescription(description);
      entries.add(entry);
    }
    feed.setEntries(entries);

    request.setContentType(
        "text/xml; charset=" + BaseForumModel.getConfigProperty("net.ugorji.oxygen.forum.encoding"));
    SyndFeedOutput output = new SyndFeedOutput();
    output.output(feed, request.getWriter());

    return RENDER_COMPLETED;
  }
}
