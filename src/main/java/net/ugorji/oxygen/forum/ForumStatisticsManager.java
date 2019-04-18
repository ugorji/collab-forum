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
import net.ugorji.oxygen.forum.data.*;
import net.ugorji.oxygen.util.Closeable;

/*
 * Periodically, we will persist the numviews of a topic to the DB e.g. every 30 minutes, and when app is shutdown
 * This is configurable
 */
public class ForumStatisticsManager implements ForumEventListener, Closeable {
  private Map<Long, Integer> topicNumViews = new HashMap();
  private Map<Long, Integer> forumNumTopics = new HashMap();
  private Map<Long, Integer> forumNumPosts = new HashMap();
  private Map<Long, Long> forumLastPostIds = new HashMap();
  private Map<Long, Long> topicLastPostIds = new HashMap();
  private Map<Long, Integer> topicNumReplies = new HashMap();
  private Map<Long, Integer> userNumPosts = new HashMap();

  public ForumStatisticsManager() {
    final ForumEngine engine = ForumLocal.getForumEngine();
    String s = engine.getProperty(ForumConstants.STATISTICS_UPDATE_INTERVAL_KEY);
    long delay = new Long(s).longValue();
    long interval = delay;
    TimerTask tt =
        new TimerTask() {
          public void run() {
            ForumLocal.setForumEngine(engine);
            recalculatePersistedStatistics();
            ForumLocal.setForumEngine(null);
          }
        };
    engine.addTask(tt, delay, interval);
  }

  public void handleEvent(ForumEvent evt) {
    if (evt.getTarget() instanceof Topic && evt.getType() == ForumEvent.READ) {
      Topic t = (Topic) evt.getTarget();
      setNumViewsForTopic(t.getId(), getNumViewsForTopic(t.getId()) + 1);
    }
  }

  public void recalculatePersistedStatistics() {
    ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
    Forum f = (Forum) dao.getRootForum();
    recalculatePersistedStatistics(f, true);
    // do same for all users
    List l = dao.getAll(User.class);
    for (Iterator itr = l.iterator(); itr.hasNext(); ) {
      User u = (User) itr.next();
      recalculatePersistedStatistics(u);
    }
  }

  public void recalculatePersistedStatistics(Forum f, boolean recursive) {
    recalculatePersistedStatisticsSingleForum(f);
    if (recursive) {
      List s = f.getChildForums();
      for (Iterator itr = s.iterator(); itr.hasNext(); ) {
        Forum f2 = (Forum) itr.next();
        recalculatePersistedStatistics(f2, recursive);
      }
    }
  }

  public void recalculatePersistedStatistics(User u) {
    List s = u.getPosts();
    setNumPostsForUser(u.getId(), s.size());
  }

  public void close() {}

  public int getNumPostedMessages() {
    return ForumLocal.getForumEngine().getForumDAO().getCount(Post.class);
  }

  public int getNumRegisteredUsers() {
    return ForumLocal.getForumEngine().getForumDAO().getCount(User.class);
  }

  public long getLastPostIdForForum(long id) {
    return (int) getget(forumLastPostIds, id, -1);
  }

  public int getNumTopicsForForum(long id) {
    return (int) getget(forumNumTopics, id, -1);
  }

  public int getNumPostsForForum(long id) {
    return (int) getget(forumNumPosts, id, -1);
  }

  public int getNumRepliesForTopic(long id) {
    return (int) getget(topicNumReplies, id, -1);
  }

  public long getLastPostIdForTopic(long id) {
    return (int) getget(topicLastPostIds, id, -1);
  }

  public int getNumViewsForTopic(long id) {
    return (int) getget(topicNumViews, id, 0);
  }

  public int getNumPostsForUser(long id) {
    return (int) getget(userNumPosts, id, 0);
  }

  public void setLastPostIdForForum(long id, long val) {
    forumLastPostIds.put(id, val);
  }

  public void setLastPostIdForTopic(long id, long val) {
    topicLastPostIds.put(id, val);
  }

  public void setNumTopicsForForum(long id, int val) {
    forumNumTopics.put(id, val);
  }

  public void setNumPostsForForum(long id, int val) {
    forumNumPosts.put(id, val);
  }

  public void setNumRepliesForTopic(long id, int val) {
    topicNumReplies.put(id, val);
  }

  public void setNumViewsForTopic(long id, int val) {
    topicNumViews.put(id, val);
  }

  public void setNumPostsForUser(long id, int val) {
    userNumPosts.put(id, val);
  }

  public void incrNumTopicsForForum(long id) {
    incr(forumNumTopics, id);
  }

  public void incrNumPostsForForum(long id) {
    incr(forumNumPosts, id);
  }

  public void incrNumRepliesForTopic(long id) {
    incr(topicNumReplies, id);
  }

  public void incrNumViewsForTopic(long id) {
    incr(topicNumViews, id);
  }

  public void incrNumPostsForUser(long id) {
    incr(userNumPosts, id);
  }

  private long getget(Map m, long id, long defvalue) {
    Number n = (Number) m.get(id);
    return (n == null ? defvalue : n.longValue());
  }

  private void incr(Map<Long, Integer> m, long id) {
    m.put(id, (int) getget(m, id, 0) + 1);
  }

  private void recalculatePersistedStatisticsSingleForum(Forum f) {
    List s = f.getTopics();
    int numTopics = s.size();
    int numPosts = 0;
    // TBD ugorji ensure we can find the last post id from SQL, or JPQL
    Long lastPostId = ForumConstants.LONG_MINUS_1;
    for (Iterator itr = s.iterator(); itr.hasNext(); ) {
      Topic t = (Topic) itr.next();
      Long newLastPostId = recalculatePersistedStatisticsSingleTopic(t);
      if (lastPostId == null || newLastPostId > lastPostId) {
        lastPostId = newLastPostId;
      }
      numPosts = numPosts + getNumRepliesForTopic(t.getId());
    }
    setNumPostsForForum(f.getId(), numPosts);
    setNumTopicsForForum(f.getId(), numTopics);
    // System.out.println("f.getId(): " + f.getId());
    setLastPostIdForForum(f.getId(), lastPostId);
  }

  private Long recalculatePersistedStatisticsSingleTopic(Topic t) {
    // no-op
    // We assume the following:
    // - Posts are never deleted if the Topic is still alive.
    //  Instead, their content is changed to maybe an empty string.
    // - This means
    //  - first post of a topic can never change,
    //  - last post of a topic can never disappear.
    // however, the numReplies might be wrong, and lastPost might point to wrong one.
    // so at least change those.

    Long lpid = ForumConstants.LONG_MINUS_1;
    List s = t.getAllposts();
    Post[] posts = (Post[]) s.toArray(new Post[0]);
    setNumRepliesForTopic(t.getId(), posts.length);
    if (posts.length > 0) {
      Arrays.sort(posts);
      lpid = posts[posts.length - 1].getId();
      setLastPostIdForTopic(t.getId(), lpid);
    } else {
      setLastPostIdForTopic(t.getId(), lpid);
    }

    return lpid;
    // int numPosts = ForumEngine.getForumEngine().getForumDAO().getCount(Post.class, "topic.id",
    // t.getId());
    // t.setNumReplies(numPosts);
    // t.setFirstPost(dao.findFirstPost(t));
    // t.setLastPost(dao.findLastPost(t));
  }

  private void updateNumViews() {
    List l = new ArrayList(topicNumViews.keySet());
    for (Iterator itr = l.iterator(); itr.hasNext(); ) {
      Long id = (Long) itr.next();
      int numviews = topicNumViews.get(id);
      Topic t = (Topic) ForumLocal.getForumEngine().getForumDAO().get(Topic.class, id, true);
      t.setNumviews(numviews);
      ForumLocal.getForumEngine().getForumDAO().saveOrUpdate(t);
    }
  }
}
