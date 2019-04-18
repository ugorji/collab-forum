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

// uniqueConstraints={@UniqueConstraint(columnNames={"EMP_ID", "EMP_NAME"})}
@Entity
@Table(name = "oxyforum_forum")
public class Forum extends DataEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "f_id", nullable = false)
  private Long id;

  @Column(name = "f_prunedays", nullable = false)
  private int prunedays = 0;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "f_parent_id", nullable = true)
  private Forum parentForum;

  @OneToMany(mappedBy = "parentForum")
  @OrderBy("order ASC, id ASC")
  private List<Forum> childForums = new ArrayList(2);

  @OneToMany(mappedBy = "forum")
  @OrderBy("highvisibility DESC, id ASC")
  private List<Topic> topics = new ArrayList(2);

  public List<Forum> getChildForums() {
    return childForums;
  }

  public void setChildForums(List<Forum> childForums) {
    this.childForums = childForums;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Forum getParentForum() {
    return parentForum;
  }

  public void setParentForum(Forum parentForum) {
    this.parentForum = parentForum;
  }

  public int getPrunedays() {
    return prunedays;
  }

  public void setPrunedays(int prunedays) {
    this.prunedays = prunedays;
  }

  public List<Topic> getTopics() {
    return topics;
  }

  public void setTopics(List<Topic> topics) {
    this.topics = topics;
  }
}
