/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.view;

import java.util.regex.Pattern;
import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.forum.ForumUtil;
import net.ugorji.oxygen.forum.data.ForumDAO;
import net.ugorji.oxygen.forum.data.Post;
import net.ugorji.oxygen.forum.data.Topic;
import net.ugorji.oxygen.util.StringUtils;
import net.ugorji.oxygen.web.*;

/*
 * This supports the following - only one of them should be specified
 *  - Edit a given post  (+ postid)
 *  - Create a new post  (+ parentpostid)
 *  - Edit a topit       (+ topicid)
 *  - Create a new Topic (+ forumid)
 *  - Consequently, there will be a hidden field for only one of these, and the
 *    Model passed will have variables for this: key and value
 */
public class PostFormAction extends GenericWebAction {

  {
    setFlag(FLAG_WRITE_ACTION);
  }

  private static Pattern quotePattern =
      Pattern.compile(
          "\\{quote:?([^}]*)\\}.*?\\{/quote\\}|\\{quote[\\s\\|]*/\\}",
          Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
  // |\\{quote[\\s\\|]*/\\}

  public int render() throws Exception {
    ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();

    PostFormModel pm = new PostFormModel();

    String s = null;
    if ((s = wctx.getParameter("forumid")) != null && !StringUtils.isBlank(s)) {
      pm.forumid = new Long(s);
    }
    if ((s = wctx.getParameter("topicid")) != null && !StringUtils.isBlank(s)) {
      pm.topicid = new Long(s);
    }
    if ((s = wctx.getParameter("postid")) != null && !StringUtils.isBlank(s)) {
      pm.postid = new Long(s);
    }
    if ((s = wctx.getParameter("parentpostid")) != null && !StringUtils.isBlank(s)) {
      pm.parentpostid = new Long(s);
    }
    pm.sync();

    ViewContext vctx = WebLocal.getViewContext();

    vctx.setModel("post", pm);
    TemplateHandler thdlr = WebLocal.getTemplateHandler();
    thdlr.render();
    return RENDER_COMPLETED;
  }

  public static class PostFormModel {
    private Long forumid = null;
    private Long topicid = null;
    private Long postid = null;
    private Long parentpostid = null;
    private Post p;
    private boolean quote = false;

    public PostFormModel() {}

    private void sync() throws Exception {
      ViewContext vctx = WebLocal.getViewContext();
      Long ll = null;
      p = new Post();
      p.setTitle("");
      p.setDetails("");

      ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();

      if (postid != null) {
        p = (Post) dao.get(Post.class, postid, true);
        topicid = p.getTopic().getId();
        vctx.setAttribute(ForumConstants.TOPICID_KEY, topicid);
        vctx.setAttribute(ForumConstants.FORUMID_KEY, p.getTopic().getForum().getId());
        p = (Post) dao.detach(p);
      } else if (parentpostid != null) {
        // System.out.println("parentpostid: " + parentpostid);
        Post p2 = (Post) dao.get(Post.class, parentpostid, true);
        topicid = p2.getTopic().getId();
        vctx.setAttribute(ForumConstants.TOPICID_KEY, topicid);
        vctx.setAttribute(ForumConstants.FORUMID_KEY, p2.getTopic().getForum().getId());
        p.setParentPost(p2);
        p.setTopic(p2.getTopic());
        p.setTitle("Re: " + p2.getTitle());
      } else if (topicid != null) {
        p = (Topic) dao.get(Topic.class, topicid, true);
        vctx.setAttribute(ForumConstants.TOPICID_KEY, p.getTopic().getId());
        vctx.setAttribute(ForumConstants.FORUMID_KEY, p.getTopic().getForum().getId());
        p = (Post) dao.detach(p);
      } else if (forumid != null) {
        vctx.setAttribute(ForumConstants.FORUMID_KEY, forumid);
        vctx.setAttribute(ForumConstants.TOPICID_KEY, null);
      }
    }

    public Long getForumId() {
      return forumid;
    }

    public Long getTopicId() {
      return topicid;
    }

    public Long getPostId() {
      return postid;
    }

    public Long getParentPostId() {
      return parentpostid;
    }

    public Post getPost() {
      return p;
    }

    public boolean isCreateNewTopic() {
      return forumid != null;
    }

    public boolean isCaptchaEnabled() {
      return ForumUtil.isCaptchaEnabled();
    }

    public String getCancelURL() throws Exception {
      if (topicid != null) {
        return BaseForumModel.getURL("topic", topicid);
      } else if (forumid != null) {
        return BaseForumModel.getURL("forum", forumid);
      } else {
        return BaseForumModel.getURL("forum");
      }
    }
  }
}
