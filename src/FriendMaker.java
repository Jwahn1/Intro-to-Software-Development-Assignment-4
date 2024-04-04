public class FriendMaker {


    /* friend
     * Given a String, f, this method will friend the user with that name. The
     * method returns the User that was friended. Friending adds the friendship to
     * adj and to the other user's adj. Friending a user that is already a friend
     * does not change the friendship.
     *
     */

    //refactor: added User v as User.java uses this class
    public User friend( User v,String f ) {
        User u = User.users.get( f );
        v.adj.put( u.name, u );
        u.adj.put( v.name, v );
        return u;
    }

    /* unfriend
     * Given a String, f, this method will unfriend the user with that name. The
     * method returns the User that was unfriended. Unfriending removes the
     * friendship from adj and from the other user's adj.
     */

    //refactor: added User v as User.java uses this class
    public  User unfriend( User v,String f ) {
        User u = User.users.get( f );
        if(v.adj.containsKey(u.name) ){
            v.adj.remove( u.name );
            u.adj.remove( v.name );
        }
        return u;
    }

    /* isFriend
     * Given a User, u, this method returns true if u is a friend of v user and
     * false otherwise.
     */
    public boolean isFriend( User v,User u ) {
        return v.adj.containsKey( u.name );
    }

    /* follow
     * Given a String, f, this method will follow the user with that name. The
     * method returns the User that was followed. following adds the friendship to
     * adj .
     *
     */
    public User follow(User v, String f){
        User u = User.users.get( f );
        v.adj.put( u.name, u );
        return u;
    }

    /* unfollow
     * Given a String, f, this method will unfollow the user with that name. The
     * method returns the User that was unfollowed. unfollowed removes the
     * friendship from adj.
     */
    public User unfollow(User v, String f){
        User u = User.users.get( f );
        v.adj.remove( u.name );
        return u;
    }

    //function returns true when user u is in user v's adj but not vice versa
    public boolean isFollowed( User v,User u ) {
        return ((v.adj.containsKey( u.name )) && !(u.adj.containsKey(v.name)));
    }

}
