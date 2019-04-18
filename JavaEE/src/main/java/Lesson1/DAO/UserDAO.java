package Lesson1.DAO;

import Lesson1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class UserDAO {
    private GeneralDAO<User> dao;
    @PersistenceContext
    private EntityManager entityManager;

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

    public List<User> getEmailOrPhoneDuplicate(String userMail, long phoneNumber) {
        return entityManager.createNativeQuery(
                "SELECT * FROM USER1 " +
                        "WHERE EMAIL = :userMail OR PHONE_NUMBER = :phoneNumber", User.class)
                .setParameter("userMail", userMail)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }
}
