package Lesson1.Test;


import Lesson1.Model.Post;
import Lesson1.Model.User;
import Lesson1.Test.DAO.GeneralDAO;

import java.util.ArrayList;
import java.util.Date;

public class main {

    public static void main(String[] args) throws Exception {


//        Chain chain, chain1, chain2;
//
//        chain = new FriendsAmountChain(RelationshipStatus.accepted, 20);
//        chain1 = chain.setNextChain(new RequestDateChain( RelationshipStatus.deleted, new Date()));
//        chain2 = chain1.setNextChain(new RequestAmountChain(RelationshipStatus.pending, 5));
//        chain.validate(RelationshipStatus.deleted);

        // tes(RelationshipStatus.deleted, RelationshipStatus.accepted, RelationshipStatus.deleted, RelationshipStatus.canceled);


        GeneralDAO<User> userDAO = new GeneralDAO<>();
        GeneralDAO<Post> postDAO = new GeneralDAO<>();

        User john = new User();
        john.setBirthDate(new Date());
        john.setEmail("john@gmail.com");
        john.setPassword("43546asd");
        john.setPhoneNumber("79665888777");
        john.setRealName("John");
        john.setUserName("Johny");
        ArrayList<Post> johnPosts = new ArrayList<>();

        User kay = new User();
        kay.setBirthDate(new Date());
        kay.setEmail("cat@gmail.com");
        kay.setPassword("sdf4665");
        kay.setPhoneNumber("79885412544");
        kay.setRealName("Katy");
        kay.setUserName("Cat");
        ArrayList<Post> kayPosts = new ArrayList<>();

        User linda = new User();
        linda.setBirthDate(new Date());
        linda.setEmail("linda@gmail.com");
        linda.setPassword("84985sasdw");
        linda.setPhoneNumber("79641210147");
        linda.setRealName("Linda");
        linda.setUserName("Lindy");
        ArrayList<Post> lindaPosts = new ArrayList<>();

        ArrayList<User> list = new ArrayList<>();
        list.add(linda);
        list.add(kay);


        Post johnPost = new Post();
        johnPost.setDatePosted(new Date());
        johnPost.setLocation("NY");
        johnPost.setMessage("Message");
        johnPost.setUserPosted(john);
        johnPost.setUserPagePosted(linda);
        johnPost.setUsersTagged(list);

        Post johnPost1 = new Post();
        johnPost.setDatePosted(new Date());
        johnPost.setLocation("NY");
        johnPost.setMessage("Message");
        johnPost.setUserPosted(john);
        johnPost.setUserPagePosted(kay);
        johnPost.setUsersTagged(list);


        johnPosts.add(johnPost);
        johnPosts.add(johnPost1);


        john.setPosts(johnPosts);
        kay.setPosts(kayPosts);
        linda.setPosts(lindaPosts);

        userDAO.save(john);
        userDAO.save(kay);
        userDAO.save(linda);
//
//        System.out.println(john.getPosts().size());


    }

}
