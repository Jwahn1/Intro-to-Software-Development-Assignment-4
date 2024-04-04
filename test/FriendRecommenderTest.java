import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class FriendRecommenderTest {
  private static String [] friendNames = { "Alice", "Bob", "Carol", "Dave", "Eve", "Frank", "Grace"};

  // clear the static users HashMap before each test
  @AfterEach
  void clearUsers() {
    User.users.clear();
  }

  // recommending friends for the same user should make no recommendations
  @Test
  void testSameUser() {
    FriendMaker friendMaker = new FriendMaker();
    User u = new User( friendNames[0] );
    FriendRecommender fr = new FriendRecommender();
    ArrayList<String> al = new ArrayList<String>();
    fr.makeRecommendations( u, u, al,friendMaker );
    assertEquals( 0, al.size(), "No recommendations should be made for the same user");
  }

  // recommending friends between two users with no friends should make no recommendations
  @Test
  void testNoFriends() {
    FriendMaker friendMaker = new FriendMaker();
    User u = new User( friendNames[0] );
    User f = new User( friendNames[1] );
    FriendRecommender fr = new FriendRecommender();
    ArrayList<String> al = new ArrayList<String>();
    fr.makeRecommendations( u, f, al,friendMaker );
    assertEquals( 0, al.size(), "No recommendations should be made for two users with no friends");
  }
  // recommending friends between two users with only each other as friends should make no recommendations
  @Test
  void testOneFriend() {
    FriendMaker friendMaker = new FriendMaker();
    User u = new User( friendNames[0] );
    User f = new User( friendNames[1] );
    friendMaker.friend(u, f.name );
    FriendRecommender fr = new FriendRecommender();
    ArrayList<String> al = new ArrayList<String>();
    fr.makeRecommendations( u, f, al,friendMaker );
    assertEquals( 0, al.size(), "No recommendations should be made for two users with only each other as friends");
  }

  // recommending friends between two users where the second has another friend should recommend that friend
  @Test
  void testTwoFriends() {
    FriendMaker friendMaker = new FriendMaker();
    User u = new User( friendNames[0] );
    User f = new User( friendNames[1] );
    User g = new User( friendNames[2] );
    friendMaker.friend(u, f.name );
    friendMaker.friend(f, g.name );
    FriendRecommender fr = new FriendRecommender();
    ArrayList<String> al = new ArrayList<String>();
    fr.makeRecommendations( u, f, al,friendMaker );
    assertEquals( 1, al.size(), "Multiple recommendations were made for two users with only each other as friends");
    assertEquals( u.name + " and " + g.name + " should be friends", al.get( 0 ), "Incorrect recommendation");
  }

  // recommending friends between two users where the second has another friend should recommend that friend in sorted order
  @Test
  void testTwoFriendsSorted() {
    FriendMaker friendMaker = new FriendMaker();
    User u = new User( friendNames[2] );
    User f = new User( friendNames[1] );
    User g = new User( friendNames[0] );
    friendMaker.friend(u, f.name );
    friendMaker.friend(f, g.name );
    FriendRecommender fr = new FriendRecommender();
    ArrayList<String> al = new ArrayList<String>();
    fr.makeRecommendations( u, f, al,friendMaker );
    assertEquals( 1, al.size(), "Wrong recommendation count" );
    assertEquals( g.name + " and " + u.name + " should be friends", al.get( 0 ), "Incorrect recommendation");
  }

  // recommending friends between two users where the first has another friend should return no recommendations
  @Test
  void testTwoFriends2() {
    FriendMaker friendMaker = new FriendMaker();
    User u = new User( friendNames[0] );
    User f = new User( friendNames[1] );
    User g = new User( friendNames[2] );
    friendMaker.friend(u, f.name );
    friendMaker.friend(f, g.name );
    FriendRecommender fr = new FriendRecommender();
    ArrayList<String> al = new ArrayList<String>();
    fr.makeRecommendations( f, u, al,friendMaker );
    assertEquals( 0, al.size(), "Wrong recommendation count" );
  }

  /*
  When a user makes a new friend, their followed users should be suggested as users
  to follow for the new friend. If Bob follows Alice and Bob becomes friends with Carol
  then a suggestion should be “Carol should follow Alice”. Similarly, any users that Carol
  follows and Bob does not follow should be suggested as new users for Bob to follow.
   */
  @Test
  void testRecommendFollowToFriends() {
    FriendMaker friendMaker = new FriendMaker();
    User u = new User( friendNames[0] );
    User f = new User( friendNames[1] );
    User g = new User( friendNames[2] );
    User h = new User( friendNames[3] );


    friendMaker.follow(u, h.name);
    friendMaker.friend(f, u.name );

    FriendRecommender fr = new FriendRecommender();
    ArrayList<String> al = new ArrayList<String>();
    fr.makeRecommendations( f, u, al,friendMaker );
    assertEquals( 0, al.size(), "Wrong recommendation count" );
  }

  /*
  When a user follows a user, their friends networks should be suggested to follow that
  user. For example, if Alice is already friends with Bob and then Bob follows Carol then
  a suggestion should be “Alice should follow Carol”
   */


}