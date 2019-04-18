/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import net.ugorji.oxygen.forum.data.ForumDAO;
import net.ugorji.oxygen.manager.GroupManager;
import net.ugorji.oxygen.manager.UserPasswordManager;
import net.ugorji.oxygen.manager.UserPreferencesManager;
import net.ugorji.oxygen.markup.MarkupConstants;
import net.ugorji.oxygen.markup.MarkupParserFactory;
import net.ugorji.oxygen.util.CloseUtils;
import net.ugorji.oxygen.util.OxygenUtils;
import net.ugorji.oxygen.util.PluginManager;
import net.ugorji.oxygen.util.SimpleLock;
import net.ugorji.oxygen.web.ActionManager;
import net.ugorji.oxygen.web.I18nManager;
import net.ugorji.oxygen.web.MarkupContainerEngine;
import net.ugorji.oxygen.web.WebLocal;

public class ForumEngine extends MarkupContainerEngine {

  private ForumDAO dao;
  private ForumIndexingManager indexMgr;
  private ForumEventManager eventMgr;
  private ForumStatisticsManager statsMgr;
  private ForumRenderEngine renderEngine;
  private ForumPruneManager pruneMgr;

  /* package */
  ForumEngine() throws Exception {
    this(new Properties());
  }

  /* package */
  ForumEngine(Properties p) throws Exception {
    OxygenUtils.info("ForumEngine initialization starting ...");
    long lcurr = System.currentTimeMillis();
    try {
      ForumLocal.setForumEngine(this);
      WebLocal.setWebContainerEngine(this);
      props = new Properties();
      reinit(p);
    } finally {
      ForumLocal.setForumEngine(null);
      WebLocal.setWebContainerEngine(null);
    }
    OxygenUtils.info(
        "ForumEngine initialization completed in " + (System.currentTimeMillis() - lcurr) + " ms");
  }

  public ForumIndexingManager getForumIndexingManager() {
    return indexMgr;
  }

  public ForumDAO getForumDAO() {
    return dao;
  }

  public ForumEventManager getForumEventManager() {
    return eventMgr;
  }

  public ForumStatisticsManager getForumStatisticsManager() {
    return statsMgr;
  }

  public ForumRenderEngine getForumRenderEngine() {
    return renderEngine;
  }

  public ForumPruneManager getForumPruneManager() {
    return pruneMgr;
  }

  public void close() {
    OxygenUtils.info("ForumEngine closing ...");
    long lcurr = System.currentTimeMillis();
    CloseUtils.close(postInitPluginMgr);
    props.clear();
    CloseUtils.close(dao);
    CloseUtils.close(authMgr);
    CloseUtils.close(prefsMgr);
    CloseUtils.close(atzMgr);
    CloseUtils.close(i18n);
    CloseUtils.close(actionMgr);
    CloseUtils.close(templateHdlr);
    // CloseUtils.close(indexMgr);
    // CloseUtils.close(eventMgr);
    CloseUtils.close(statsMgr);
    CloseUtils.close(renderEngine);
    // CloseUtils.close(pruneMgr);
    CloseUtils.close(preInitPluginMgr);
    CloseUtils.close(shortTermAcquiredLock);
    CloseUtils.close(longTermLock);
    int initTimeSecs = (int) ((System.currentTimeMillis() - lcurr) / 1000);
    OxygenUtils.info("ForumEngine is closed in " + initTimeSecs + " seconds");
  }

  public boolean isUsernameOverrideAllowed() {
    return "true".equals(getProperty(ForumConstants.ALLOW_OVERRIDE_USERNAME_KEY));
  }

  private void reinit(Properties initProps0) throws Exception {
    // System.out.println("Integer.MAX_VALUE: " + Integer.MAX_VALUE);
    if (initProps0 != null) {
      // should we pass in the initProps0 as the parent of props? FIXME
      props.putAll(initProps0);
    }
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    InputStream is = cl.getResourceAsStream("net/ugorji/oxygen/forum/init.properties");
    props.load(is);
    CloseUtils.close(is);

    is = cl.getResourceAsStream(ForumConstants.CONFIG_INIT_OVERRIDE_FILE);
    if (is != null) {
      props.load(is);
      CloseUtils.close(is);
    }

    propReplaceReferences(props);

    File f = new File(getConfigDirectory(), "oxyforum.properties");
    if (f.exists()) {
      is = new FileInputStream(f);
      props.load(is);
      CloseUtils.close(is);
    }

    propReplaceReferences(props);

    timer = new Timer(true);

    String s = null;

    s = getProperty(MarkupConstants.PARSER_FACTORY_CLASS_KEY);
    markupParserFactory = (MarkupParserFactory) Class.forName(s).newInstance();

    preInitPluginMgr = new PluginManager(props, "net.ugorji.oxygen.forum.plugin.preinit.");
    preInitPluginMgr.start();

    actionMgr = new ActionManager(props, "net.ugorji.oxygen.forum.action.");
    i18n = new I18nManager("net.ugorji.oxygen.forum.Forum");
    templateHdlr = new ForumTemplateHandler();

    s = getProperty(ForumConstants.AUTHENTICATION_MANAGER_KEY);
    authMgr = (UserPasswordManager) Class.forName(s).newInstance();
    authMgr.init(props);

    s = getProperty(ForumConstants.AUTHORIZATION_MANAGER_KEY);
    atzMgr = (GroupManager) Class.forName(s).newInstance();
    atzMgr.init(props);

    s = getProperty(ForumConstants.USERPREFERENCES_MANAGER_KEY);
    prefsMgr = (UserPreferencesManager) Class.forName(s).newInstance();
    prefsMgr.init(props);

    dao = new ForumDAO();

    statsMgr = new ForumStatisticsManager();

    indexMgr = new ForumIndexingManager();

    renderEngine = new ForumRenderEngine();

    pruneMgr = new ForumPruneManager();

    eventMgr = new ForumEventManager();

    postInitPluginMgr = new PluginManager(props, "net.ugorji.oxygen.forum.plugin.postinit.");
    postInitPluginMgr.start();

    longTermLock = new SimpleLock();
    shortTermAcquiredLock = new SimpleLock.Strict();
  }
}
