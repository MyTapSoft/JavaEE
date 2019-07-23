package Lesson1.Service;


import Lesson1.DAO.UserDAO;
import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
public class UserService {

    private final UserDAO dao;

    @Autowired
    public UserService(UserDAO dao) {
        this.dao = dao;
    }

    public User saveUser(User user) throws BadRequestException {
        if (dao.findUserDuplicate(user.getEmail(), user.getPhoneNumber()) != null) {
            throw new BadRequestException("Email or Phone Number Already Exist");
        }
        return dao.saveUser(user);
    }

    public User updateUser(User user) {
        return dao.updateUser(user);
    }

    public void deleteUser(long userId) {
        dao.deleteUser(userId);
    }

    public User getUser(long id) {
        User result = dao.getUser(id);
        if (result == null) throw new EntityExistsException();

        return result;
    }

    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    public List<User> getUserFriends(long userId) {
        return dao.getUserFriends(userId);
    }

    public User login(String login, String password) throws BadRequestException {
        User user = dao.getUser(login, password);
        if (user == null) throw new BadRequestException("Incorrect username or password");
        return user;
    }

    public List<User> getIncomeRequests(String userId) {
        return dao.getIncomeRequests(userId);

    }

    public List<User> getOutcomeRequests(String userId) {
        return dao.getOutcomeRequests(userId);

    }

}
