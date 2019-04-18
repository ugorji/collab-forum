/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.extension;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumEngine;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.forum.data.*;
import net.ugorji.oxygen.util.CloseUtils;
import net.ugorji.oxygen.util.OxygenUtils;
import net.ugorji.oxygen.util.Plugin;
import net.ugorji.oxygen.util.StringUtils;

public class ForumDBInit implements Plugin {
  public void init() {}

  public void close() {}

  public void start() throws Exception {
    String s = null;
    ForumEngine fe = ForumLocal.getForumEngine();
    InputStream is = null;
    boolean doWork = "true".equals(fe.getProperty(ForumConstants.DB_INIT_ENABLED_KEY));
    // System.out.println("ForumDBInit: doWork: " + doWork);
    if (doWork) {
      ForumDAO dao = fe.getForumDAO();
      try {
        boolean clearTables =
            "true".equals(fe.getProperty(ForumConstants.DB_INIT_CLEAR_TABLES_KEY));
        if (clearTables) {
          // System.out.println("ForumDBInit: clearing tables ... ");
          dao.clearDBTables();
          dao.closeCurrentSession();
        }
        s = fe.getProperty(ForumConstants.DB_INIT_LOAD_RESOURCE_KEY);
        is = OxygenUtils.searchForResourceAsStream(s, getClass());
        FCXMLParser parsehdlr = new FCXMLParser();
        parsehdlr.dao = dao;
        SAXParserFactory fact = SAXParserFactory.newInstance();
        SAXParser sp = fact.newSAXParser();
        // System.out.println("ForumDBInit: parsing XML file ... ");
        sp.parse(is, parsehdlr);
        // dao.closeCurrentSession();
        // recalc statistics
        fe.getForumStatisticsManager().recalculatePersistedStatistics();
      } finally {
        CloseUtils.close(is);
        dao.closeCurrentSession();
      }
    }
  }

  private static class FCXMLParser extends DefaultHandler {
    private ForumEntity x;
    private Map ym = new HashMap();
    private ForumDAO dao;

    private int numIters(String s) {
      return (StringUtils.isBlank(s) ? 1 : Integer.parseInt(s));
    }

    private Object gg(Map m, Object key) {
      return (key == null ? null : m.get(key));
    }

    private String gs(String s, int i) {
      return (i == 1 ? s : (s + i));
    }

    public void startDocument() throws SAXException {}

    public void endDocument() throws SAXException {}

    public void endElement(String uri, String localName, String qName) throws SAXException {}

    public void characters(char[] ch, int start, int length) throws SAXException {}

    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
      try {
        String lk = attributes.getValue("loadkey");
        int len = numIters(attributes.getValue("iterations"));
        String n = attributes.getValue("title");
        String d = attributes.getValue("details");
        String a = attributes.getValue("author");
        boolean ac = "true".equals(attributes.getValue("active"));
        String p = attributes.getValue("parent");
        String ip = attributes.getValue("ip");

        if ("user".equals(qName)) {
          for (int i = 1; i <= len; i++) {
            User x = new User();
            x.setActive(ac);
            x.setName(gs(n, i));
            x.setRegdate(new Date());
            x.setLastvisit(new Date());
            dao.saveOrUpdate(x);
            putt(ym, gs(lk, i), x);
          }
        } else if ("forum".equals(qName)) {
          for (int i = 1; i <= len; i++) {
            Forum x = new Forum();
            x.setTitle(gs(n, i));
            x.setDetails(gs(d, i));
            x.setParentForum((Forum) ym.get(p));
            x.setAuthor((User) ym.get(a));
            dao.saveOrUpdate(x);
            putt(ym, gs(lk, i), x);
          }
        } else if ("topic".equals(qName)) {
          for (int i = 1; i <= len; i++) {
            Topic x = new Topic();
            x.setForum((Forum) ym.get(p));
            x.setTitle(gs(n, i));
            x.setAuthor((User) ym.get(a));
            x.setDate(new Date());
            x.setIp(gs(ip, i));
            x.setDetails(d);
            dao.saveOrUpdate(x);
            // System.out.println("Just called saveOrUpdate Topic");
            // System.out.println("Just called saveOrUpdate Topic with id: " + x.getId());
            putt(ym, gs(lk, i), x);
          }
        } else if ("post".equals(qName)) {
          for (int i = 1; i <= len; i++) {
            Post x = new Post();
            x.setParentPost((Post) ym.get(p));
            x.setTopic(x.getParentPost().getTopic());
            x.setAuthor((User) ym.get(a));
            x.setDate(new Date());
            x.setIp(gs(ip, i));
            x.setTitle(gs(n, i));
            x.setDetails(d);
            dao.saveOrUpdate(x);
            putt(ym, gs(lk, i), x);
          }
        }
      } catch (Exception exc) {
        // exc.printStackTrace();
        throw new SAXException(exc);
      } finally {
        // dao.flush();
      }
    }

    private void putt(Map m, String k, ForumEntity v) {
      m.put(k, v);
      // dao.flush();
      // System.err.println("     XXXXX: Putting " + v.getClass().getName().substring(18) + ": " +
      // v.getId());
    }
  }
}
