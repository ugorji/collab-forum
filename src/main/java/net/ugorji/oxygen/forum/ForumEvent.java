/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

public class ForumEvent {

  public static final int FORUM = 1;
  public static final int TOPIC = 2;
  public static final int POST = 3;

  public static final int CREATE = 1;
  public static final int READ = 2;
  public static final int UPDATE = 3;
  public static final int DELETE = 4;

  private Object target;
  private int targetClass;
  private int type;

  public ForumEvent(Object target0, int targetClass0, int type0) {
    setTarget(target0);
    setTargetClass(targetClass0);
    setType(type0);
  }

  public Object getTarget() {
    return target;
  }

  public void setTarget(Object target) {
    this.target = target;
  }

  public int getTargetClass() {
    return targetClass;
  }

  public void setTargetClass(int targetClass) {
    this.targetClass = targetClass;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
