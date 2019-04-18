/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.data;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "oxyforum_topic")
@PrimaryKeyJoinColumn(name = "f_post_id")
public class Topic extends Post {

  @Column(name = "f_numviews", nullable = false)
  private int numviews;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "f_forum_id", nullable = false)
  private Forum forum;

  @OneToMany(mappedBy = "topic")
  @OrderBy("parentPost ASC, id ASC")
  private List<Post> allposts = new ArrayList(2);

  @ManyToMany
  @OrderBy("id ASC")
  @JoinTable(
      name = "oxyforum_topic_watch",
      joinColumns = @JoinColumn(name = "f_topic_id"),
      inverseJoinColumns = @JoinColumn(name = "f_user_id"))
  private List<User> usersWatching = new ArrayList(2);

  public Topic() {
    setTopic(this);
  }

  public List<Post> getAllposts() {
    return allposts;
  }

  public void setAllposts(List<Post> allposts) {
    this.allposts = allposts;
  }

  public Forum getForum() {
    return forum;
  }

  public void setForum(Forum forum) {
    this.forum = forum;
  }

  public int getNumviews() {
    return numviews;
  }

  public void setNumviews(int numviews) {
    this.numviews = numviews;
  }

  public List<User> getUsersWatching() {
    return usersWatching;
  }

  public void setUsersWatching(List<User> usersWatching) {
    this.usersWatching = usersWatching;
  }
}
