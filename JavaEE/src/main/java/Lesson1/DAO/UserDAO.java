package Lesson1.DAO;

import Lesson1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UserDAO {
    private GeneralDAO<User> dao;

    @Autowired
    public UserDAO(GeneralDAO<User> dao) {
        this.dao = dao;
    }

    public User saveUser(User user) throws Exception {
        return dao.save(user);
    }

    public User updateUser(User user) throws Exception {
        return dao.update(user);
    }

    public void deleteUser(long id) throws Exception {
        dao.delete(id, User.class);
    }

    public User getUser(long id) throws Exception {
        return dao.getEntity(id, User.class);
    }
}
