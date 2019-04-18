package Lesson1.Service;


import Lesson1.DAO.UserDAO;
import Lesson1.Model.User;
import javassist.bytecode.DuplicateMemberException;
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

    public User saveUser(User user) throws DuplicateMemberException {
        if (dao.getEmailOrPhoneDuplicate(user.getEmail(), user.getPhoneNumber()).size() != 0) {
            System.out.println("DuplicateMemberException");
            throw new DuplicateMemberException("Email or Phone Number Already Exist");
        }
        return dao.saveUser(user);
    }

    public User updateUser(User user) {
        return dao.updateUser(user);
    }

    public void deleteUser(User user) {
        dao.deleteUser(user.getId());
    }

    public User getUser(long id) {
        User result = dao.getUser(id);
        if (result == null) throw new EntityExistsException();

        return result;
    }
}
