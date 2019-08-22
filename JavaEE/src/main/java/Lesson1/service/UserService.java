package Lesson1.service;


import Lesson1.repository.UserDAO;
import Lesson1.exceptions.BadRequestException;
import Lesson1.exceptions.DuplicateException;
import Lesson1.exceptions.NotFoundException;
import Lesson1.exceptions.UnauthorizedException;
import Lesson1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserService {

    private final UserDAO dao;

    @Autowired
    public UserService(UserDAO dao) {
        this.dao = dao;
    }


    public User saveUser(User user) {
        if (dao.findUserDuplicate(user.getEmail(), user.getPhoneNumber()) != null) {
            throw new DuplicateException("Email or Phone Number Already Exist");
        }
        return dao.saveUser(user);
    }

    public User updateUser(User user, HttpSession session) throws UnauthorizedException {
        isUserLogin(session);
        User result = dao.updateUser(user);
        if (result == null)
            throw new NotFoundException("User With ID: " + user.getId() + " Doesn't Exist");
        return result;
    }

    public void deleteUser(Long userId, HttpSession session) throws UnauthorizedException {
        isUserLogin(session);
        dao.deleteUser(userId);
    }

    public User getUser(Long id) throws BadRequestException {
        User result = dao.getUser(id);
        if (result == null)
            throw new BadRequestException("User with ID: " + id + " doesn't exist");
        return result;
    }

    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    public List<User> getUserFriends(Long userId) {
        return dao.getUserFriends(userId);
    }

    public User login(String login, String password) {

        return dao.getUser(login, password);
    }

    public List<User> getIncomeRequests(String userId, HttpSession session) throws UnauthorizedException {
        isUserLogin(session);
        return dao.getIncomeRequests(userId);

    }

    public List<User> getOutcomeRequests(String userId, HttpSession session) throws UnauthorizedException {
        isUserLogin(session);
        return dao.getOutcomeRequests(userId);

    }

    private void isUserLogin(HttpSession session) throws UnauthorizedException {
        if (session.getAttribute("loginStatus") == null) throw new UnauthorizedException("You have to login first");
    }


}
