package Lesson1.DAO;

import Lesson1.Model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class PostDAO {
    private GeneralDAO<Post> dao;
    @PersistenceContext
    private EntityManager entityManager;
    private static final String GET_USER_POSTS = "SELECT P.*\n" +
            " FROM POST P\n" +
            " WHERE P.USER_POSTED IN (SELECT U.USER_ID\n" +
            "                        FROM USERS U\n" +
            "                                 JOIN RELATIONSHIP R\n" +
            "                                      ON (U.USER_ID = R.USER_ID_FROM OR U.USER_ID = R.USER_ID_TO)\n" +
            "                        WHERE R.STATUS = :status\n" +
            "                          AND (R.USER_ID_TO = :userId OR R.USER_ID_FROM = :userId)) " +
            " ORDER BY P.DATE_POSTED;";


    private static final String GET_FRIENDS_POSTS = "SELECT p.*\n" +
            "FROM POST p\n" +
            "WHERE p.USER_POSTED IN (SELECT U.USER_ID\n" +
            "                        FROM USERS U\n" +
            "                                 JOIN RELATIONSHIP R\n" +
            "                                      ON (U.USER_ID = R.USER_ID_FROM OR U.USER_ID = R.USER_ID_TO)\n" +
            "                                          AND U.USER_ID != :userId\n" +
            "                        WHERE R.STATUS = :status\n" +
            "                          AND (R.USER_ID_TO = :userId OR R.USER_ID_FROM = :userId))";

    @Autowired
    public PostDAO(GeneralDAO<Post> dao) {
        this.dao = dao;
    }

    public Post savePost(Post post) {
        return dao.save(post);
    }

    public Post updatePost(Post post) {
        return dao.update(post);
    }

    public void deletePost(long id) {
        dao.delete(id, Post.class);
    }

    public Post findPost(long id) {
        return dao.getEntity(id, Post.class);
    }

    public List<Post> getUserAndFriendsPosts(long userId) {
        List<Post> list = entityManager.createNativeQuery(GET_USER_POSTS, Post.class)
                .setParameter("userId", userId)
                .getResultList();
        return list;
    }

    public List<Post> getFriendsPosts(long userId) {
        List<Post> list = entityManager.createNativeQuery(GET_FRIENDS_POSTS, Post.class)
                .setParameter("userId", userId)
                .getResultList();
        return list;
    }
}