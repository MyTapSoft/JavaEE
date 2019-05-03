package Lesson1.DAO;

import Lesson1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class UserDAO {
    private GeneralDAO<User> dao;
    @PersistenceContext
    private EntityManager entityManager;
    private static final String findDuplicateUser = "SELECT * FROM USER1 " +
            "WHERE EMAIL = :userMail OR PHONE_NUMBER = :phoneNumber";

    private static final String loginUser = "SELECT * FROM USER1 " +
            "WHERE USER_NAME = :login AND PASSWORD = :password";


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

    public User findUserDuplicate(String userMail, String phoneNumber) {
        return (User) entityManager.createNativeQuery(
                findDuplicateUser, User.class)
                .setParameter("userMail", userMail)
                .setParameter("phoneNumber", phoneNumber)
                .getSingleResult();
    }

    public User login(String login, String password) {
        return (User) entityManager.createNativeQuery(loginUser, User.class)
                .setParameter("login", login)
                .setParameter("password", password)
                .getSingleResult();
    }
}
