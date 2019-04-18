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
import net.ugorji.oxygen.forum.ForumConstants;

@MappedSuperclass
public abstract class PostEntity extends DataEntity {
  @Column(name = "f_ip", nullable = false)
  private String ip = ForumConstants.UNKNOWN_IP_ADDRESS;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "f_parent_post_id", nullable = true)
  private Post parentPost;

  @OneToMany(mappedBy = "parentPost")
  @OrderBy("id ASC")
  private List<Post> childPosts = new ArrayList(2);

  public List<Post> getChildPosts() {
    return childPosts;
  }

  public void setChildPosts(List<Post> childPosts) {
    this.childPosts = childPosts;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Post getParentPost() {
    return parentPost;
  }

  public void setParentPost(Post parentPost) {
    this.parentPost = parentPost;
  }
}
