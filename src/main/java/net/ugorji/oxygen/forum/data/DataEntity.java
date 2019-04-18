/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.data;

import java.util.Date;
import javax.persistence.*;

@MappedSuperclass
public abstract class DataEntity extends ForumEntity {

  @Column(name = "f_title", nullable = false)
  private String title;

  @Column(name = "f_details", nullable = false, length = Short.MAX_VALUE)
  private String details;

  @Column(name = "f_order", nullable = false)
  private int order = 0;

  @JoinColumn(name = "f_author", nullable = false)
  @ManyToOne(cascade = CascadeType.ALL)
  private User author;

  @Column(name = "f_date", nullable = false)
  private Date date = new Date();

  @Column(name = "f_write", nullable = false)
  private boolean write = true;

  @Column(name = "f_read", nullable = false)
  private boolean read = true;

  @Column(name = "f_highvisibility", nullable = false)
  private boolean highvisibility = false;

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public boolean isHighvisibility() {
    return highvisibility;
  }

  public void setHighvisibility(boolean highvisibility) {
    this.highvisibility = highvisibility;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isWrite() {
    return write;
  }

  public void setWrite(boolean write) {
    this.write = write;
  }

  // convenience methods for now
  public String getSubject() {
    return getTitle();
  }

  public String getName() {
    return getTitle();
  }

  public String getText() {
    return getDetails();
  }

  public String getDescription() {
    return getDetails();
  }
}
