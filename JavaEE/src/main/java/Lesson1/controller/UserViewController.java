package Lesson1.controller;

import Lesson1.exceptions.BadRequestException;
import Lesson1.exceptions.UnauthorizedException;
import Lesson1.model.User;
import Lesson1.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserViewController {

    private final UserService userService;

    @Autowired
    public UserViewController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    public String getUser(Model model, @PathVariable String userId) throws BadRequestException {
        User user = userService.getUser(Long.parseLong(userId));
        model.addAttribute("user", user);
        return "user/profile";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/allUsers")
    public String getAllUsers(Model model) {
        model.addAttribute("user", userService.getAllUsers());
        return "user/all-users";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/friends")
    public String getUserFriends(Model model, HttpSession session) throws BadRequestException {
        if (session.getAttribute("loginStatus") == null) throw new BadRequestException("You have to login first");
        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("user", userService.getUserFriends(userId));
        return "user/user-friends";
    }


    @RequestMapping(method = RequestMethod.GET, path = "/incomeRequests")
    public String getIncomeRequests(HttpSession session, Model model) throws UnauthorizedException {
        String userId = String.valueOf(session.getAttribute("userId"));
        model.addAttribute("user", userService.getIncomeRequests(userId, session));
        return "user/user-friends";

    }

    @RequestMapping(method = RequestMethod.GET, path = "/outcomeRequests")
    public String getOutcomeRequests(HttpSession session, Model model) throws UnauthorizedException {

        String userId = String.valueOf(session.getAttribute("userId"));
        List<User> usersList = userService.getOutcomeRequests(userId, session);
        model.addAttribute("user", usersList);
        return "user/user-friends";

    }


    @RequestMapping(method = RequestMethod.GET, value = "/register-user")
    public String getRegistrationPage() {
        return "user/user-registration";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String getLoginPage() {
        return "user/user-login";
    }
}
