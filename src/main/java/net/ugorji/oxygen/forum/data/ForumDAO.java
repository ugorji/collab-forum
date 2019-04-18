/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.persistence.*;
import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.util.CloseUtils;
import net.ugorji.oxygen.util.Closeable;
import net.ugorji.oxygen.util.OxyLocal;
import net.ugorji.oxygen.util.OxygenUtils;
import net.ugorji.oxygen.util.StringUtils;
import net.ugorji.oxygen.web.WebResourceNotFoundException;

public class ForumDAO implements Closeable {
  private EntityManagerFactory emf;

  public ForumDAO() throws Exception {
    // String s = null;
    // s = cfg.get(ForumConstants.HIBERNATE_RESOURCE_LOCATION);
    // sessionFactory = new Configuration().configure(s).buildSessionFactory();
    Properties pp = new Properties();
    Properties p = ForumLocal.getForumEngine().getProperties();
    String pfx = p.getProperty(ForumConstants.DAO_PROVIDER_PROPERTIES_PREFIX_KEY);
    OxygenUtils.extractProps(p, pp, pfx, false);
    emf = Persistence.createEntityManagerFactory("oxyforum", pp);

    // initDatabase();
  }

  private EntityManager getCurrentSession() {
    EntityManager s = (EntityManager) OxyLocal.get(EntityManager.class);
    if (s == null) {
      s = emf.createEntityManager();
      s.getTransaction().begin();
      OxyLocal.set(EntityManager.class, s);
    }
    return s;
  }

  public void closeCurrentSession() {
    EntityManager s = (EntityManager) OxyLocal.get(EntityManager.class);
    if (s == null) return;
    try {
      s.flush();
    } catch (Exception exc) {
      OxygenUtils.error(exc);
    }
    try {
      s.getTransaction().commit();
    } catch (Exception exc) {
      OxygenUtils.error(exc);
    }
    try {
      s.close();
    } catch (Exception exc) {
      OxygenUtils.error(exc);
    }
    OxyLocal.set(EntityManager.class, null);
  }

  // public void commit() {
  //  Transaction tx = (Transaction)OxyLocal.get(Transaction.class);
  //  if(tx != null) {
  //    tx.commit();
  //  }
  // }

  public void flush() {
    getCurrentSession().flush();
  }

  public void close() {
    closeCurrentSession();
    emf.close();
  }

  public Forum getRootForum() {
    Query query =
        getCurrentSession()
            .createQuery(
                "select p from " + Forum.class.getName() + " p where p.parentForum is NULL");
    return (Forum) query.getSingleResult();
  }

  public Object get(Class clazz, Serializable id, boolean assumeExisting) {
    EntityManager sess = getCurrentSession();
    // Object obj = (assumeExisting ? sess.load(clazz, id) : sess.get(clazz, id));
    Object obj = sess.find(clazz, id);
    if (assumeExisting && obj == null) {
      throw new WebResourceNotFoundException(
          "No object of type: " + clazz.getName() + " and id: " + id + " exists");
    }
    // sess.flush();
    return obj;
  }

  public void refresh(ForumEntity obj) {
    if (obj != null) {
      EntityManager sess = getCurrentSession();
      sess.refresh(obj);
    }
  }

  public ForumEntity detach(ForumEntity obj) {
    return obj;
    // return getCurrentSession().detach(obj);
  }

  public void saveOrUpdate(ForumEntity obj) {
    saveOrUpdate(obj, false);
  }

  private void saveOrUpdate(ForumEntity obj, boolean flush) {
    if (obj != null && obj.getId() == null) {
      EntityManager sess = getCurrentSession();
      sess.persist(obj);
      if (flush) {
        sess.flush();
      }
      // System.out.println("Persisted Object: " + obj.getId() + " " + obj.getClass().getName());
    }
  }

  public void delete(ForumEntity obj) {
    if (obj != null) {
      EntityManager sess = getCurrentSession();
      sess.remove(obj);
    }
  }

  public User getUser(String username) {
    EntityManager sess = getCurrentSession();
    String s = "select p from net.ugorji.oxygen.forum.data.User p where p.name = ?1";
    User user = null;
    try {
      user = (User) sess.createQuery(s).setParameter(1, username).getSingleResult();
    } catch (NoResultException exc) {
      user = new User();
      user.setName(username);
      user.setLastvisit(new Date());
      sess.persist(user);
    }
    // System.out.println("User id: " + user.getId());
    return user;
  }

  public Misc getMisc(String key, String defValue) {
    EntityManager sess = getCurrentSession();
    String s = "select p from net.ugorji.oxygen.forum.data.Misc p where p.key = ?1";
    Misc m = null;
    try {
      m = (Misc) sess.createQuery(s).setParameter(1, key).getSingleResult();
    } catch (NoResultException exc) {
      if (defValue != null) {
        m = new Misc();
        m.setKey(key);
        m.setValue(defValue);
        sess.persist(m);
      }
    }
    return m;
  }

  public List getTopicsChangedSince(Date d) {
    // String s = "select t from net.ugorji.oxygen.forum.data.Topic t where t.lastPost.date > ?";
    String s =
        "select p.topic, max(p.id) from net.ugorji.oxygen.forum.data.Post p where p.date > ?1 group by p.topic";
    return executeQuery(s, new Object[] {d}, true);
  }

  public List getTopicsForForumUnchangedSince(Forum f, Date d) {
    // String s = "select t from net.ugorji.oxygen.forum.data.Topic t where t.forum = ? AND t.lastPost.date <
    // ?";
    String s =
        "select p.topic, max(p.id) from net.ugorji.oxygen.forum.data.Post p where p.topid.forum = ?1 and p.date < ?2 group by p.topic";
    return executeQuery(s, new Object[] {f, d}, true);
  }

  public List getAllForums() {
    return (executeQuery("select t from net.ugorji.oxygen.forum.data.Forum t", null, false));
  }

  public List getAll(Class c) {
    return (executeQuery("select t from " + c.getName() + " t", null, false));
  }

  public int getCount(Class c, String property, Object value) {
    String s = "select count(c) from " + c.getName() + " c " + " where c." + property + " = ?1";
    return getSingleRowIntResult(s, new Object[] {value});
  }

  public int getCount(Class c) {
    String s = "select count(c) from " + c.getName() + " c";
    return getSingleRowIntResult(s, null);
  }

  public int getSum(Class c, String property) {
    String s = "select sum(c." + property + ") from " + c.getName() + " c";
    return getSingleRowIntResult(s, null);
  }

  public void setWatchTopic(User u, Topic t, boolean yesOrNo) {
    if (yesOrNo) {
      u.getWatchedTopics().add(t);
    } else {
      u.getWatchedTopics().remove(t);
    }
  }

  private List executeQuery(String s, Object[] args, boolean returnFirstOnly) {
    List cats = getQuery(s, args).getResultList();
    List list2 = cats;
    if (returnFirstOnly) {
      list2 = new ArrayList(cats.size());
      for (Object o : cats) {
        list2.add(((Object[]) o)[0]);
      }
    }
    return list2;
  }

  private Query getQuery(String s, Object[] args) {
    args = (args == null) ? new Object[0] : args;
    // System.out.println("executeQuery: " + s + " ---- " + Arrays.asList(args));
    EntityManager sess = getCurrentSession();
    Query query = sess.createQuery(s);
    for (int i = 0; i < args.length; i++) {
      query.setParameter(i + 1, args[i]);
    }
    return query;
  }

  public void clearDBTables() throws Exception {
    // System.out.println(">>>>>>>>>>>>>> clearDBTables START >>>>>>>>>>>>>");
    OxygenUtils.info("clearDBTables starting ...");
    closeCurrentSession();
    // clearDBTablesJPA();
    clearDBTablesJDBC();
    closeCurrentSession();
    // System.out.println(">>>>>>>>>>>>>> clearDBTables END >>>>>>>>>>>>>");
    // System.exit(1);
    OxygenUtils.info("clearDBTables finished");
  }

  private void clearDBTablesJPA() {
    // whenever we explicitly call delete, we are not communicating with EM cache.
    // So - we must explicitly close, then start+commit+close after each delete.
    String[] qc = new String[] {"Post", "Topic", "Forum", "User", "Misc"};
    for (int i = 0; i < qc.length; i++) {
      // System.out.println(">>>>>>>>>>>>>> clearDBTables: " + qc[i]);
      Query query = getCurrentSession().createQuery("delete from net.ugorji.oxygen.forum.data." + qc[i]);
      int i2 = query.executeUpdate();
      OxygenUtils.info("XXXX: Deleted " + i2 + " instances of " + qc[i]);
      closeCurrentSession();
    }
  }

  private void clearDBTablesJDBC() throws Exception {
    EntityManager s = emf.createEntityManager();
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = (Connection) s.unwrap(Connection.class);
      stmt = conn.createStatement();
      String[] qc = new String[] {"post", "topic", "topic_watch", "forum", "user", "misc"};
      for (int i = 0; i < qc.length; i++) {
        int i2 = stmt.executeUpdate("delete from oxyforum_" + qc[i]);
        OxygenUtils.info("XXXX: Deleted (" + i2 + ")? rows from oxyforum_" + qc[i]);
      }
      String isql =
          ForumLocal.getForumEngine().getProperty(ForumConstants.IDENTITY_SEQUENCE_CLEAR_SQL_KEY);
      if (!StringUtils.isBlank(isql)) {
        int i2 = stmt.executeUpdate(isql);
        OxygenUtils.info("XXXX: Deleted (" + i2 + ")? rows from the identity sequences table");
      }
      conn.commit();
    } finally {
      CloseUtils.close(stmt);
      CloseUtils.close(conn);
      CloseUtils.close(s);
    }
  }

  private int getSingleRowIntResult(String s, Object[] args) {
    Object obj = getQuery(s, args).getSingleResult();
    if (obj == null) {
      throw new RuntimeException("No Result was returned from the Query: " + s);
    } else if (obj instanceof Long) {
      return ((Long) obj).intValue();
    } else if (obj instanceof Integer) {
      return ((Integer) obj).intValue();
    } else {
      throw new RuntimeException("Unable to decipher result for query: " + s + " Result: " + obj);
    }
  }
}
