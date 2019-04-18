/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.render;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.ugorji.oxygen.forum.ForumConstants;
import net.ugorji.oxygen.forum.ForumLocal;
import net.ugorji.oxygen.markup.*;
import net.ugorji.oxygen.util.OxygenUtils;

public class DefaultForumRenderEngine extends MarkupRenderEngine {

  CensoredWordManager censoredMgr;
  EmoticonsManager emoticonsMgr;
  ShorthandManager shorthandMgr;
  Properties props;
  Map macros = new HashMap();

  public DefaultForumRenderEngine() throws Exception {
    props = ForumLocal.getForumEngine().getProperties();
    emoticonsMgr = new EmoticonsManager(props);
    censoredMgr = new CensoredWordManager(props);
    shorthandMgr = new ShorthandManager(props);

    Properties p2 = new Properties();
    OxygenUtils.extractProps(props, p2, ForumConstants.MACRO_KEY_PREFIX, true);
    for (Enumeration enum0 = p2.propertyNames(); enum0.hasMoreElements(); ) {
      String name = (String) enum0.nextElement();
      Class clazz = Class.forName(p2.getProperty(name));
      MarkupMacro wp = (MarkupMacro) clazz.newInstance();
      macros.put(name, wp);
    }
  }

  public void render(Writer w, Reader in) throws Exception {
    MarkupRenderContext context = newRenderContext();
    render(w, in, context, Integer.MAX_VALUE);
  }

  public void render(
      final Writer out,
      final Reader in,
      final MarkupRenderContext context,
      final int maxNumParagraphs)
      throws IOException {
    if (out == null) {
      return;
    }
    PrintWriter pw = null;
    if (out instanceof PrintWriter) {
      pw = (PrintWriter) out;
    } else {
      pw = new PrintWriter(out);
    }

    final PrintWriter _pw = pw;
    try {
      ForumParser parser = new ForumParser(in, _pw, context, this, maxNumParagraphs);
      parser.markupToHTML();
    } catch (MarkupParserBase.MaxParagraphsExceededSignal wexc) {
      // no-op - we just gracefully exit here
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception exc) {
      throw new RuntimeException(exc);
    } finally {
      flushPW(_pw);
    }
  }

  public MarkupMacro getMacro(String command) {
    return (MarkupMacro) macros.get(command);
  }

  public MarkupRenderContext newRenderContext() {
    return new ForumMarkupRenderContext();
  }

  private static class ForumParser {
    private int numParagraphs = 0;
    private int maxNumParagraphs = 0;
    private MarkupParser mp;
    private MarkupParserBase mbase;

    public ForumParser(
        Reader _r,
        PrintWriter _w,
        MarkupRenderContext _rc,
        MarkupRenderEngine _re,
        int _maxNumParagraphs)
        throws Exception {
      mp = ForumLocal.getForumEngine().getMarkupParserFactory().newMarkupParser(_r);
      mbase = new MarkupParserBase();
      mbase.setRenderContext(_rc);
      mbase.setRenderEngine(_re);
      mbase.setWriter(_w);
      mbase.setMaxNumParagraphs(_maxNumParagraphs);
      mp.setMarkupParserBase(mbase);
    }

    public void markupToHTML() throws Exception {
      mp.markupToHTML();
    }
  }

  class ForumMarkupRenderContext extends DefaultMarkupRenderContext {

    public ForumMarkupRenderContext() {
      super(props, censoredMgr, emoticonsMgr, shorthandMgr);
      set(MarkupConstants.ESCAPE_HTML_KEY, Boolean.TRUE);
      set(MarkupConstants.FREELINK_SUPPORTED_KEY, Boolean.TRUE);
      set(MarkupConstants.CAMEL_CASE_IS_LINK_KEY, Boolean.TRUE);
      set(MarkupConstants.SLASH_SEPARATED_IS_LINK_KEY, Boolean.TRUE);
      set(MarkupConstants.DECORATE_EXTERNAL_LINKS_KEY, Boolean.TRUE);
      set(MarkupConstants.AS_IS_SUPPORTED_KEY, Boolean.TRUE);
      set(MarkupConstants.SINGLE_PAGE_KEY, Boolean.FALSE);
      set(MarkupConstants.REAL_PAGE_VIEW_KEY, Boolean.TRUE);
    }

    public MarkupMacro getMacro(String command) {
      return (MarkupMacro) macros.get(command);
    }

    public String getContentUnavailableString() {
      return "<pre>CONTENT IS UNAVAILABLE</pre>";
    }
  }
}
