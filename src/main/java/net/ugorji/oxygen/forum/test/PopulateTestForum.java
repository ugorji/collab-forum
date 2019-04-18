/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.forum.test;

// Commenting this out for now ... Ugorji June 5, 2007
public abstract class PopulateTestForum {}

/*
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import net.ugorji.oxygen.util.CloseUtils;
import net.ugorji.oxygen.util.StringUtils;

public class PopulateTestForum {
  private static String CREATE_FORUM = "insert into oxyforum_forum (forum_name, forum_desc, forum_parent_id) values(?, 'whatever', ?)";
  private static String CREATE_TOPIC = "insert into oxyforum_topic (forum_id, topic_title, topic_poster_id, topic_date, topic_first_post_id, topic_last_post_id) values(?, \"Admin Notice\", 1, CURRENT_TIMESTAMP, 1, 1)";
  private static String CREATE_POST = "insert into oxyforum_post (topic_id, poster_id, post_date, poster_ip, post_subject, post_text) values(?, 1, CURRENT_TIMESTAMP, '255.255.255.255', ?, ?)";
  private static String UPDATE_TOPIC = "update oxyforum_topic set topic_first_post_id = ?, topic_last_post_id = ? where topic_id = ?";
  private static String UPDATE_POST = "update oxyforum_post set post_parent_id = ? where topic_id = ? AND post_id != ?";
  private static String UPDATE_FORUM = "update oxyforum_forum set forum_last_post_id = ?, forum_topics = ?, forum_posts = ? where forum_id = ?";
  private static String UPDATE_USER = "update oxyforum_user set user_posts = ? where user_id = 1";

  private static String[] LARGE_STRING;
  private static int numTopicsPerForum = 2;
  private static int numPostsPerTopic = 20;

  private static long currTopicId = 1;
  private static long currPostId = 1;
  private static long currForumId = 1;

  static {
    try {
      InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("net.ugorji.oxygen/forum/test/LARGE_STRING.TXT");
      String s = StringUtils.readerToString(new InputStreamReader(is));
      LARGE_STRING = new String[3];
      LARGE_STRING[0] = s.substring(0, 177);
      LARGE_STRING[1] = s.substring(178, 355);
      LARGE_STRING[2] = s.substring(356);
    } catch(Exception exc) {
      throw new RuntimeException(exc);
    }
  }

  public static void main(String[] args) throws Exception {
    //if(true) { printLargeString(); return; }
    Class.forName("com.mysql.jdbc.Driver");
    Connection conn = null;
    try {
      int numTopicsPerForum = 2;
      int numPostsPerTopic = 20;
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/oxyforum?autoReconnect=true");
      PreparedStatement pstmt1 = conn.prepareStatement(CREATE_FORUM);
      PreparedStatement pstmt2 = conn.prepareStatement(CREATE_TOPIC);
      PreparedStatement pstmt3 = conn.prepareStatement(CREATE_POST);
      PreparedStatement pstmt4 = conn.prepareStatement(UPDATE_TOPIC);
      PreparedStatement pstmt5 = conn.prepareStatement(UPDATE_POST);
      PreparedStatement pstmt6 = conn.prepareStatement(UPDATE_FORUM);
      PreparedStatement pstmt7 = conn.prepareStatement(UPDATE_USER);
      //create forums - Top/1, Top/2, Top/3, Top/1/1, Top/2/1, Top/3/1
      createForum(pstmt1, "1", 1); //forumid = 2
      createForum(pstmt1, "2", 1); //forumid = 3
      createForum(pstmt1, "3", 1); //forumid = 4
      createForum(pstmt1, "1", 2); //forumid = 5
      createForum(pstmt1, "2", 3); //forumid = 6
      createForum(pstmt1, "3", 4); //forumid = 7

      for(int i = 2; i <= 7; i++) {
        int fNumTopics = 0;
        int fNumPosts = 0;
        for(int j = 0; j < numTopicsPerForum; j++) {
          //create a topic
          createTopic(pstmt2, (long)i);
          fNumTopics++;
          long firstPostId = 0;
          long lastPostId = 0;
          for(int k = 0; k < numPostsPerTopic; k++) {
            createPost(pstmt3, currTopicId, "A post for topic: " + currTopicId + " at iteration: " + k, LARGE_STRING[k%3]);
            fNumPosts++;
            if(k == 0) {
              firstPostId = currPostId;
            } else if(k == (numPostsPerTopic - 1)) {
              lastPostId = currPostId;
            }
          }
          updateTopic(pstmt4, currTopicId, firstPostId, lastPostId);
          updatePost(pstmt5, currTopicId, firstPostId);
          System.out.println("Created Topic: " + currTopicId + " for forum: " + i);
        }
        updateForum(pstmt6, (long)i, fNumTopics, fNumPosts, currPostId);
      }
      updateUser(pstmt7, (int)currPostId);
    } finally {
      CloseUtils.close(conn);
      System.out.println("Done ... ");
    }
  }

  private static void updateUser(PreparedStatement pstmt, int numPosts) throws Exception {
    pstmt.setInt(1, numPosts);
    pstmt.executeUpdate();
  }

  private static void updateForum(PreparedStatement pstmt, long forumid, int numTopics, int numPosts, long lastpostid) throws Exception {
    pstmt.setLong(1, lastpostid);
    pstmt.setInt(2, numTopics);
    pstmt.setInt(3, numPosts);
    pstmt.setLong(4, forumid);
    pstmt.executeUpdate();
  }

  private static void updateTopic(PreparedStatement pstmt, long topicid, long firstPostId, long lastPostId) throws Exception {
    pstmt.setLong(1, firstPostId);
    pstmt.setLong(2, lastPostId);
    pstmt.setLong(3, topicid);
    pstmt.executeUpdate();
  }

  private static void updatePost(PreparedStatement pstmt, long topicid, long firstPostId) throws Exception {
    pstmt.setLong(1, firstPostId);
    pstmt.setLong(2, topicid);
    pstmt.setLong(3, firstPostId);
    pstmt.executeUpdate();
  }

  private static void createForum(PreparedStatement pstmt, String name, long parentid) throws Exception {
    pstmt.setString(1, name);
    pstmt.setLong(2, parentid);
    pstmt.executeUpdate();
    currForumId++;
  }

  private static void createTopic(PreparedStatement pstmt, long topicid) throws Exception {
    pstmt.setLong(1, topicid);
    pstmt.executeUpdate();
    currTopicId++;
  }

  private static void createPost(PreparedStatement pstmt, long l1, String s1, String s2) throws Exception {
    pstmt.setLong(1, l1);
    pstmt.setString(2, s1);
    pstmt.setString(3, s2);
    pstmt.executeUpdate();
    currPostId++;
  }

  private static void printLargeString() throws Exception {
    System.out.println("============================ ");
    System.out.println(LARGE_STRING[0]);
    System.out.println("============================ ");
    System.out.println(LARGE_STRING[1]);
    System.out.println("============================ ");
    System.out.println(LARGE_STRING[2]);
    System.out.println("============================ ");
  }

}

*/
