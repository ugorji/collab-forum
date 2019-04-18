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
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "oxyforum_user")
public class User extends ForumEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "f_id", nullable = false)
  private Long id;

  @Column(name = "f_name", nullable = false)
  private String name;

  @Column(name = "f_active", nullable = false)
  private boolean active = false;

  @Column(name = "f_lastvisit", nullable = false)
  private Date lastvisit = new Date();

  @Column(name = "f_regdate", nullable = false)
  private Date regdate = new Date();

  @OneToMany(mappedBy = "author")
  @OrderBy("id ASC")
  private List<Topic> topics = new ArrayList(2);

  @OneToMany(mappedBy = "author")
  @OrderBy("id ASC")
  private List<Post> posts = new ArrayList(2);

  @ManyToMany(mappedBy = "usersWatching")
  @OrderBy("highvisibility DESC, id ASC")
  private List<Topic> watchedTopics = new ArrayList(2);

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Date getLastvisit() {
    return lastvisit;
  }

  public void setLastvisit(Date lastvisit) {
    this.lastvisit = lastvisit;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  public Date getRegdate() {
    return regdate;
  }

  public void setRegdate(Date regdate) {
    this.regdate = regdate;
  }

  public List<Topic> getTopics() {
    return topics;
  }

  public void setTopics(List<Topic> topics) {
    this.topics = topics;
  }

  public List<Topic> getWatchedTopics() {
    return watchedTopics;
  }

  public void setWatchedTopics(List<Topic> watchedTopics) {
    this.watchedTopics = watchedTopics;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
