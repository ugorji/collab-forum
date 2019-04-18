/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.extension;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.ugorji.oxygen.forum.*;
import net.ugorji.oxygen.forum.data.Post;
import net.ugorji.oxygen.forum.data.Topic;
import net.ugorji.oxygen.forum.data.User;
import net.ugorji.oxygen.forum.view.BaseForumModel;
import net.ugorji.oxygen.util.I18n;
import net.ugorji.oxygen.util.SendMail;
import net.ugorji.oxygen.util.StringUtils;
import net.ugorji.oxygen.web.ViewContext;
import net.ugorji.oxygen.web.WebLocal;

public class TopicWatchListener implements ForumEventListener {

  public void handleEvent(ForumEvent fe) throws Exception {
    boolean b = false;
    Post p = null;
    Object o = fe.getTarget();
    if (o instanceof Post
        && (p = (Post) o) != null
        && (b =
            "true"
                .equals(
                    ForumLocal.getForumEngine().getProperty(ForumConstants.EMAIL_ENABLED_KEY)))) {
      sendNotificationEmail(p, fe.getType());
    }
  }

  private void sendNotificationEmail(Post p, int eventType) throws Exception {
    ForumEngine engine = ForumLocal.getForumEngine();
    Topic t = p.getTopic();
    List users = t.getUsersWatching();
    String htmlTmplLoc = "topic-watch-notification.html";
    StringWriter stw = new StringWriter();
    Map model = new HashMap();
    model.put("topic", t);
    model.put("post", p);
    model.put("writer", stw);
    model.put("link", getLink(t));
    ((ForumTemplateHandler) engine.getTemplateHandler()).write(htmlTmplLoc, model, stw);
    String emailHTML = stw.toString();

    // System.out.println("Sending Email Notification to users: '" + users + "'");

    if (users != null && users.size() > 0) {
      StringBuffer buf = new StringBuffer();
      for (Iterator itr = users.iterator(); itr.hasNext(); ) {
        User user = (User) itr.next();
        String emailaddress = BaseForumModel.getUserpref(user.getName(), "email");
        if (!StringUtils.isBlank(emailaddress)) {
          buf.append(emailaddress).append(",");
        }
      }

      if (buf.length() != 0) {
        I18n i18n = engine.getI18nManager().getI18n(null);
        SendMail.EmailInfoHolder email = new SendMail.EmailInfoHolder();
        email.recipients = buf.toString();
        email.sender = engine.getProperty(ForumConstants.EMAIL_SENDER_KEY);
        email.subject = i18n.str("topic_watch_notification_subject");
        email.headers.put("Content-Type", "text/html; charset=\"us-ascii\"");
        email.text = emailHTML;
        email.properties.put(
            "mail.smtp.host", engine.getProperty(ForumConstants.EMAIL_SMTP_HOST_KEY));
        // email.write(new PrintWriter(System.out));
        SendMail.sendMail(email);
      }
    }
  }

  private String getLink(Topic t) throws Exception {
    ViewContext vctx = new ViewContext();
    vctx.setAction("topic");
    vctx.setAttribute(ForumConstants.ID_KEY, t.getId());
    vctx.setAttribute(ForumConstants.TOPICID_KEY, t.getId());
    vctx.setAttribute(ForumConstants.FORUMID_KEY, t.getForum().getId());
    return WebLocal.getWebInteractionContext().toURLString(vctx, null);
  }
}
