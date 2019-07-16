package Lesson1.DAO;

import Lesson1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    private static final String GET_DUPLICATE_USER = "SELECT * FROM USERS " +
            "WHERE EMAIL = :userMail OR PHONE_NUMBER = :phoneNumber";

    private static final String GET_USER = "SELECT * FROM USERS " +
            "WHERE USER_NAME = :login AND PASSWORD = :password";

    private static final String GET_ALL_USERS = "SELECT * FROM USERS";

    private static final String GET_USER_FRIENDS = "SELECT U.* FROM USERS U " +
            "JOIN RELATIONSHIP R " +
            "ON (U.USER_ID = R.USER_ID_FROM OR U.USER_ID = R.USER_ID_TO) " +
            "AND U.USER_ID != :userId " +
            "WHERE R.STATUS = :status " +
            "AND (R.USER_ID_TO = :userId OR R.USER_ID_FROM = :userId)";

    private static final String INCOME_USER_REQUESTS = "SELECT U.* " +
            "FROM USERS U" +
            "JOIN RELATIONSHIP R ON (U.USER_ID = R.USER_ID_FROM OR U.USER_ID = R.USER_ID_TO) " +
            "AND U.USER_ID != :userId " +
            "WHERE R.STATUS = :status " +
            "AND R.USER_ID_TO = :userId";

    private static final String OUTCOME_USER_REQUESTS = "SELECT U.* " +
            "FROM USERS U" +
            "JOIN RELATIONSHIP R ON (U.USER_ID = R.USER_ID_FROM OR U.USER_ID = R.USER_ID_TO) " +
            "AND U.USER_ID != :userId " +
            "WHERE R.STATUS = :status " +
            "AND R.USER_ID_FROM = :userId";


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
        return (User) entityManager.createNativeQuery(GET_USER, User.class)
                .setParameter("login", login)
                .setParameter("password", password)
                .getSingleResult();
    }

    public List<User> getAllUsers() {
        return entityManager.createNamedQuery(GET_ALL_USERS, User.class)
                .getResultList();

    }

    public List<User> getUserFriends(long userId) {
        List<User> list = entityManager.createNativeQuery(GET_USER_FRIENDS, User.class)
                .setParameter("userId", userId)
                .setParameter("status", "accepted")
                .getResultList();
        return list;
    }

    public User findUserDuplicate(String userMail, String phoneNumber) {
        try {
            return (User) entityManager.createNativeQuery(
                    GET_DUPLICATE_USER, User.class)
                    .setParameter("userMail", userMail)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
        } catch (NoResultException empty) {
            return null;
        }

    }


    public List<User> getIncomeRequests(String userId) {
        return entityManager.createNativeQuery(INCOME_USER_REQUESTS, User.class)
                .setParameter("userId", userId)
                .setParameter("status", "pending")
                .getResultList();
    }

    public List<User> getOutcomeRequests(String userId) {
        return entityManager.createNativeQuery(OUTCOME_USER_REQUESTS, User.class)
                .setParameter("userId", userId)
                .setParameter("status", "pending")
                .getResultList();
    }


}
