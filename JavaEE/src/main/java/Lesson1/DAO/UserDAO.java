package Lesson1.DAO;

import Lesson1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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

    private static final String GET_USER_FRIENDS = "SELECT * FROM USER1 WHERE USER_ID_FROM = :userId " +
            "OR USER_ID_TO = :userId AND STATUS = :statusCode";


    @Autowired
    public UserDAO(GeneralDAO<User> dao) {
        this.dao = dao;
    }


    public User saveUser(User user) {
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
        return list;
    }

    public User findUserDuplicate(String userMail, String phoneNumber) {
        return (User) entityManager.createNativeQuery(
                FIND_DUPLICATE_USER, User.class)
                .setParameter("userMail", userMail)
                .setParameter("phoneNumber", phoneNumber)
                .getSingleResult();
    }

    public User getUser(String login, String password) {
        return (User) entityManager.createNativeQuery(LOGIN_USER, User.class)
                .setParameter("login", login)
                .setParameter("password", password)
                .getSingleResult();
    }
}
