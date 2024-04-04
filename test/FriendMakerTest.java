import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FriendMakerTest {
    private static String[] friendNames = {"Alice", "Bob", "Carol", "Dave", "Eve", "Frank", "Grace"};
    @AfterEach
    void clearUsers() {
        User.users.clear();
    }
    // Test unfriending a user removes them from both users' adj
    @Test
    void testUnfriend() {
        FriendMaker friendMaker = new FriendMaker();
        User u = new User(friendNames[0]);
        User v = new User(friendNames[1]);
        friendMaker.friend(u,friendNames[1]);
        User w = friendMaker.unfriend(u,friendNames[1]);
        assertEquals(0, u.adj.size(), "u's adj has the wrong size");
        assertEquals(0, v.adj.size(), "v's adj has the wrong size");
        assertEquals(null, u.adj.get(friendNames[1]), "u's adj has the wrong user");
        assertEquals(null, v.adj.get(friendNames[0]), "v's adj has the wrong user");
        assertEquals(v, w, "unfriend returned the wrong user");
    }

    // Test friending a user twice does not add them twice
    @Test
    void testFriendTwice() {
        FriendMaker friendMaker = new FriendMaker();
        User u = new User(friendNames[0]);
        User v = new User(friendNames[1]);
        User w = friendMaker.friend(u,friendNames[1]);
        User x = friendMaker.friend(u,friendNames[1]);
        assertEquals(1, u.adj.size(), "u's adj has the wrong size");
        assertEquals(1, v.adj.size(), "v's adj has the wrong size");
        assertEquals(v, u.adj.get(friendNames[1]), "u's adj has the wrong user");
        assertEquals(u, v.adj.get(friendNames[0]), "v's adj has the wrong user");
        assertEquals(v, w, "friend returned the wrong user");
        assertEquals(v, x, "friend returned the wrong user");
    }



    // Test unfriending a user in the opposite order
    @Test
    void testUnfriendOpposite() {
        FriendMaker friendMaker = new FriendMaker();
        User u = new User(friendNames[0]);
        User v = new User(friendNames[1]);
        friendMaker.friend(u,friendNames[1]);
        User w = friendMaker.unfriend(v,friendNames[0]);
        assertEquals(0, u.adj.size(), "u's adj has the wrong size");
        assertEquals(0, v.adj.size(), "v's adj has the wrong size");
        assertEquals(null, u.adj.get(friendNames[1]), "u's adj has the wrong user");
        assertEquals(null, v.adj.get(friendNames[0]), "v's adj has the wrong user");
        assertEquals(u, w, "unfriend returned the wrong user");
    }

    /* Test a user leaving when they have a friend removes them from the HashMap
     * and removes them from their friend's adj.
     */
    @Test
    void testLeaveFriend() {
        FriendMaker friendMaker = new FriendMaker();
        User u = new User(friendNames[0]);
        User v = new User(friendNames[1]);
        friendMaker.friend(u,friendNames[1]);
        u.leave();
        assertEquals(0, v.adj.size(), "v's adj has the wrong size");
        assertFalse(v.adj.containsKey(friendNames[0]), "v's adj has the wrong user");
        assertEquals(1, User.users.size(), "Incorrect size");
        assertEquals(null, User.users.get(friendNames[0]), "User not removed from HashMap");
        assertEquals(v, User.users.get(friendNames[1]), "User v not in HashMap");
    }

    //test a user following another user
    @Test
    void testFollow(){
        FriendMaker friendMaker = new FriendMaker();
        User u = new User(friendNames[0]);
        User v = new User(friendNames[1]);
        friendMaker.follow(u,v.name);
        assertEquals(1,u.adj.size(),"u's adj has the wrong size");
        assertEquals(0,v.adj.size(),"v's adj has the wrong size");
        assertEquals(true,friendMaker.isFriend(u,v),"v is not in u's adj");
        assertEquals(false,friendMaker.isFriend(v,u),"u is  in v's adj");
    }

    //test following a user twice, this shouldn't change the status of adj
    @Test
    void testFollowTwice(){
        FriendMaker friendMaker = new FriendMaker();
        User u = new User(friendNames[0]);
        User v = new User(friendNames[1]);
        friendMaker.follow(u,v.name);
        friendMaker.follow(u,v.name);
        assertEquals(1,u.adj.size(),"u's adj has the wrong size");
        assertEquals(0,v.adj.size(),"v's adj has the wrong size");
        assertEquals(true,friendMaker.isFriend(u,v),"v is not in u's adj");
        assertEquals(false,friendMaker.isFriend(v,u),"u is  in v's adj");
    }

    //test following a user and then friending them, this shouldn't change the status of  u's adj
    @Test
    void testFollowFriend(){
        FriendMaker friendMaker = new FriendMaker();
        User u = new User(friendNames[0]);
        User v = new User(friendNames[1]);
        friendMaker.follow(u,v.name);
        friendMaker.friend(u,v.name);
        assertEquals(1,u.adj.size(),"u's adj has the wrong size");
        assertEquals(1,v.adj.size(),"v's adj has the wrong size");
        assertEquals(true,friendMaker.isFriend(u,v),"v is not in u's adj");
        assertEquals(true,friendMaker.isFriend(v,u),"u is not in v's adj");
    }

    //test a user following another user and then unfollowing
    @Test
    void testUnfollow(){
        FriendMaker friendMaker = new FriendMaker();
        User u = new User(friendNames[0]);
        User v = new User(friendNames[1]);
        friendMaker.follow(u,v.name);
        friendMaker.unfollow(u,v.name);
        assertEquals(0,u.adj.size(),"u's adj has the wrong size");
        assertEquals(0,v.adj.size(),"v's adj has the wrong size");
        assertEquals(false,friendMaker.isFriend(u,v),"v is in u's adj");

    }

    //test a user following and then unfriending the user should return with both adj being empty
    @Test
    void testFollowUnfriend(){
        FriendMaker friendMaker = new FriendMaker();
        User u = new User(friendNames[0]);
        User v = new User(friendNames[1]);
        friendMaker.follow(u,v.name);
        friendMaker.unfriend(u,v.name);
        assertEquals(0,u.adj.size(),"u's adj has the wrong size");
        assertEquals(0,v.adj.size(),"v's adj has the wrong size");
        assertEquals(false ,friendMaker.isFriend(u,v),"v is  in u's adj");
        assertEquals(false,friendMaker.isFriend(v,u),"u is  in v's adj");
    }
}
