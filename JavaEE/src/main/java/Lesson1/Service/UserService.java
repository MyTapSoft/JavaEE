package Lesson1.Service;


import Lesson1.DAO.UserDAO;
import Lesson1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
public class UserService {

    private UserDAO dao;

    @Autowired
    public UserService(UserDAO dao) {
        this.dao = dao;
    }

    public User saveUser(User user) throws Exception {
        return dao.saveUser(user);
    }

    public User updateUser(User user) throws Exception {
        return dao.updateUser(user);
    }

    public void deleteUser(User user) throws Exception {
        dao.deleteUser(user.getId());
    }

    public User getUser(long id) throws Exception {
        User result = dao.getUser(id);
        if (result == null) throw new EntityExistsException();
        return result;
    }
}
