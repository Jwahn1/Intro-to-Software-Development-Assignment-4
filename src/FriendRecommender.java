import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

/* FriendRecommender.java
 *
  * This class is used to make friend recommendations for users of a social
  * network. The code is incomplete and contains bugs.
  *
*/
public class FriendRecommender {

  /* main
   * This method is used to read input from the user and print the output of the
   * friend recommendations. The input is read from the standard input and the
   * output is printed to the standard output.
   */
  public static void main( String [] args ) {
    FriendMaker friendMaker = new FriendMaker();
    ArrayList<String> output = new FriendRecommender().compute(friendMaker, new Scanner( System.in ) );

    for( String s : output ) {
      System.out.println( s );
    }
  }

  /* compute
   * This method takes a Scanner, input, and returns an ArrayList of Strings.
   * The Scanner contains the input from the user. The method reads the input
   * and makes friend recommendations based on the input. The method returns an
   * ArrayList of Strings that contains the friend recommendations.
   */
  public ArrayList<String> compute(FriendMaker friendMaker , Scanner input ) {
    ArrayList<String> list = new ArrayList<String>();

    for(String s = input.nextLine(); !s.equals( "end" ); s = input.nextLine()) {
      Scanner line = new Scanner( s );
      String name = line.next();
      User u = User.find( name );

      switch( line.next() ) {
        case "joins":
          assert( u == null );
          new User( name );
          break;
        case "leaves":
          assert( u != null );
          u.leave();
          break;
        case "friends":
          assert( u != null );
          String friended = line.next();
          recommend( u, friendMaker.friend(u, friended), list,friendMaker );
          recommendFriendFollow( User.users.get(friended), u, list,friendMaker );
          recommendFriendFollow(u, User.users.get(friended), list,friendMaker);
          break;
        case "unfriends":
          assert( u != null );
          friendMaker.unfriend(u, line.next() );
          break;
        case "follows":
          assert(u != null);
          recommendFollow( u, friendMaker.follow(u, line.next()), list,friendMaker );
          break;
        case "unfollows":
          assert(u != null);
          friendMaker.unfollow(u, line.next());
          break;
        default:
          System.out.println( "Unknown user action" );
      }
    }
    return list;
  }

  /* recommend
   * Given two users, u and f, and an ArrayList of Strings, al, this method
   * will recommend new friends for u based on the friends of f. The
   * recommendations are added to al. The recommendations are of the form
   * "A and B should be friends" where A and B are the names of the users and
   * A comes before B in sorted order. The method does not return anything so
   * the output is passed back in al.
   */
  public void recommend( User u, User f, ArrayList<String> al ,FriendMaker friendMaker) {
    ArrayList<String> tmp = new ArrayList<String>();
    makeRecommendations( u, f, tmp,friendMaker );
    makeRecommendations( f, u, tmp,friendMaker);
    Collections.sort( tmp );
    String prev = null;
    for( String s : tmp ) {
      if( !s.equals( prev ) ) {
        al.add( s );
        prev = s;
      }
    }
  }

  //used when a user u follows user f
  public void recommendFollow( User u, User f, ArrayList<String> al ,FriendMaker friendMaker) {
    ArrayList<String> tmp = new ArrayList<String>();
   //following somone you friend should be ignored
    if(!friendMaker.isFriend(u,f)){
      makeRecommendationsFollow( u, f, tmp,friendMaker );
    }


    Collections.sort( tmp );
    String prev = null;
    for( String s : tmp ) {
      if( !s.equals( prev ) ) {
        al.add( s );
        prev = s;
      }
    }
  }

  //used when a user u friends another user f and f follows other users
  public void recommendFriendFollow( User u, User f, ArrayList<String> al ,FriendMaker friendMaker) {
    ArrayList<String> tmp = new ArrayList<String>();
    makeRecommendationsFriendFollow( u, f, tmp,friendMaker );

    Collections.sort( tmp );
    String prev = null;
    for( String s : tmp ) {
      if( !s.equals( prev ) ) {
        al.add( s );
        prev = s;
      }
    }
  }



  /* makeRecommendations
   * Given two users, u and f, and an ArrayList of Strings, al, this method
   * will recommend new friends for u based on the friends of f. The
   * recommendations are added to al. The recommendations are of the form
   * "A and B should be friends" where A and B are the names of the users and
   * A comes before B in sorted order. The method does not return anything so
   * the output is passed back in al.
   */
  public void makeRecommendations( User u, User f, ArrayList<String> al,FriendMaker friendMaker ) {
    for( User v : f.adj.values() ) {
      if( (u != v) && !friendMaker.isFriend( u,v ) &&!friendMaker.isFollowed(f,v) ) {
        if( v.name.compareTo( u.name ) < 0 ) {
          al.add( v.name + " and " + u.name + " should be friends" );
        } else {
          al.add( u.name + " and " + v.name + " should be friends" );
        }
      }  
    }
  }

  // after user u follows another user f, the function will recommend  to all of user u's friends to follow user f
  public void makeRecommendationsFollow( User u, User f, ArrayList<String> al,FriendMaker friendMaker ) {
    for( User v : u.adj.values() ) {
      if( (f != v) && !friendMaker.isFollowed( v,f ) ) {
        al.add( v.name + " should follow " + f.name  );
      }
    }
  }

  /* after user u friends another user f, the function will recommend  all of the user's f follows to user u, so u can
  follow them.
  */
  public void makeRecommendationsFriendFollow( User u, User f, ArrayList<String> al,FriendMaker friendMaker ) {
    for( User v : f.adj.values() ) {
      if( (u != v) && friendMaker.isFollowed( f,v ) ) {
        al.add( u.name + " should follow " + v.name  );
      }
    }
  }


}
