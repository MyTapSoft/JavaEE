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

    private static final String GET_USER_POSTS = "SELECT * FROM POST WHERE USER_POSTED = :userId";

    private static final String GET_ALL_POSTS = "SELECT * FROM POST";
    private static final String GET_USER_AND_FRIENDS_POSTS_BY_USER_PAGE = "SELECT P.*\n" +
            " FROM POST P\n" +
            "         JOIN USERS U ON P.USER_POSTED = U.USER_ID\n" +
            " WHERE P.USER_PAGE_POSTED = :userId\n" +
            " ORDER BY P.DATE_POSTED";


    private static final String GET_FRIENDS_POSTS_BY_USER_PAGE = "SELECT P.*\n" +
            " FROM POST P\n" +
            "    JOIN USERS U ON P.USER_POSTED = U.USER_ID\n" +
            " WHERE P.USER_PAGE_POSTED = :userId\n" +
            "    AND P.USER_POSTED != :userId";

    private static final String GET_FEED = "SELECT p.*\n" +
            " FROM POST p\n" +
            " WHERE p.USER_POSTED IN (SELECT U.USER_ID\n" +
            "                        FROM USERS U\n" +
            "                                 JOIN RELATIONSHIP R\n" +
            "                                      ON (U.USER_ID = R.USER_ID_FROM OR U.USER_ID = R.USER_ID_TO)\n" +
            "                                          AND U.USER_ID != :userId\n" +
            "                        WHERE R.STATUS = :status\n" +
            "                          AND (R.USER_ID_TO = :userId OR R.USER_ID_FROM = :userId))\n" +
            " ORDER BY P.DATE_POSTED DESC \n" +
            "    OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY;\n";


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

    public Post getPost(long id) {
        return dao.getEntity(id, Post.class);
    }

    public List<Post> getUserAndFriendsPosts(long userId) {
        List<Post> list = entityManager.createNativeQuery(GET_USER_AND_FRIENDS_POSTS_BY_USER_PAGE, Post.class)
                .setParameter("userId", userId)
                .getResultList();
        return list;
    }

    public List<Post> getFriendsPosts(long userId) {
        List<Post> list = entityManager.createNativeQuery(GET_FRIENDS_POSTS_BY_USER_PAGE, Post.class)
                .setParameter("userId", userId)
                .getResultList();
        return list;
    }

    public List<Post> getAllPosts() {
        List<Post> list = entityManager.createNativeQuery(GET_ALL_POSTS, Post.class)
                .getResultList();
        return list;
    }

    public List<Post> getUserPosts(long userId) {
        List<Post> list = entityManager.createNativeQuery(GET_USER_POSTS, Post.class)
                .setParameter("userId", userId)
                .getResultList();
        return list;
    }

    public List<Post> getFeed(long userId, short offset) {
        List<Post> list = entityManager.createNativeQuery(GET_FEED, Post.class)
                .setParameter("userId", userId)
                .setParameter("offset", offset)
                .setParameter("limit", 5)
                .setParameter("status", "accepted")
                .getResultList();
        return list;
    }

}