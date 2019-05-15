package Lesson1.DAO;

import Lesson1.Model.Relationship;
import Lesson1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class UserDAO {
    private GeneralDAO<User> dao;
    @PersistenceContext
    private EntityManager entityManager;
    private static final String FIND_DUPLICATE_USER = "SELECT * FROM USER1 " +
            "WHERE EMAIL = :userMail OR PHONE_NUMBER = :phoneNumber";

    private static final String LOGIN_USER = "SELECT * FROM USER1 " +
            "WHERE USER_NAME = :login AND PASSWORD = :password";

    private static final String GET_ALL_USERS = "SELECT * FROM USER1";

    private static final String GET_USER_FRIENDS = "SELECT * FROM USER1 " +
            "LEFT JOIN RELATIONSHIP R " +
            "ON USER1.USER1_ID = R.USER_ID_FROM OR USER1.USER1_ID = R.USER_ID_TO AND R.STATUS = :statusCode " +
            "WHERE USER1_ID = :userId";

    private static final String INCOME_USER_REQUESTS = "SELECT * FROM USER1 LEFT JOIN RELATIONSHIP R " +
            "ON USER1.USER1_ID = R.USER_ID_TO AND R.STATUS = :statusCode WHERE USER1_ID = :userId";

    private static final String OUTCOME_USER_REQUESTS = "SELECT * FROM USER1 LEFT JOIN RELATIONSHIP R " +
            "ON USER1.USER1_ID = R.USER_ID_FROM AND R.STATUS = :statusCode WHERE USER1_ID = :userId";


    @Autowired
    public UserDAO(GeneralDAO<User> dao) {
        this.dao = dao;
    }


    public User saveUser(User user) {
        System.out.println("DAO");
        return dao.save(user);
    }

    public User updateUser(User user) {
        return dao.update(user);
    }

    public void deleteUser(long id) {
        dao.delete(id, User.class);
    }

    public User getUser(long id) {
        return dao.getEntity(id, User.class);
    }

    public User getUser(String login, String password) {
        return (User) entityManager.createNativeQuery(LOGIN_USER, User.class)
                .setParameter("login", login)
                .setParameter("password", password)
                .getSingleResult();
    }

    public List<User> getAllUsers() {
        List<User> list = entityManager.createNativeQuery(GET_ALL_USERS, User.class)
                .getResultList();
        return list;
    }

    public List<User> getUserFriends(long userId) {
        List<User> list = entityManager.createNativeQuery(GET_USER_FRIENDS, User.class)
                .setParameter("userId", userId)
                .setParameter("statusCode", 1)
                .getResultList();
        System.out.println(list);
        return list;
    }

    public User findUserDuplicate(String userMail, String phoneNumber) {
        try {
            return (User) entityManager.createNativeQuery(
                    FIND_DUPLICATE_USER, User.class)
                    .setParameter("userMail", userMail)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
        } catch (NoResultException empty) {
            return null;
        }

    }


    public List<User> getIncomeRequests(String userId) {
        return entityManager.createNativeQuery(INCOME_USER_REQUESTS, Relationship.class)
                .setParameter("userId", userId)
                .setParameter("statusCode", 0)
                .getResultList();
    }

    public List<User> getOutcomeRequests(String userId) {
        return entityManager.createNativeQuery(OUTCOME_USER_REQUESTS, Relationship.class)
                .setParameter("userId", userId)
                .setParameter("statusCode", 0)
                .getResultList();
    }
}
