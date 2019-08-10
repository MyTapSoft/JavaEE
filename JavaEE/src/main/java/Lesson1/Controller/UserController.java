package Lesson1.Controller;


import Lesson1.Exceptions.BadRequestException;
import Lesson1.Exceptions.NotFoundException;
import Lesson1.Exceptions.ResponseStatusHandler;
import Lesson1.Exceptions.UnauthorizedException;
import Lesson1.Model.User;
import Lesson1.Service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final static Logger log = Logger.getLogger(ResponseStatusHandler.class);


    @Autowired

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping(path = "/registerUser", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        log.info("New user added successfully. ID " + user.getId());
        return new ResponseEntity<>("User Saved Successfully", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateUser")
    public String updateUser(@ModelAttribute User user, HttpSession session) throws UnauthorizedException {
        userService.updateUser(user, session);
        log.info("User update data successfully. ID " + user.getId());
        return "user-profile";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    public String getUser(Model model, @PathVariable String userId) throws BadRequestException {
        User user = userService.getUser(Long.parseLong(userId));
        model.addAttribute("user", user);
        return "user/profile";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUser")
    public String deleteUser(HttpSession session) throws UnauthorizedException {
        Long userId = (Long) session.getAttribute("userId");
        userService.deleteUser(userId, session);
        log.info("User deleted successfully. ID " + userId);
        return "home";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/allUsers")
    public String allUsersPage(Model model) {
        model.addAttribute("user", userService.getAllUsers());
        return "user/all-users";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/friends")
    public String getUserFriends(Model model, HttpSession session) throws BadRequestException {
        if (session.getAttribute("loginStatus") == null) throw new BadRequestException("You have to login first");
        long userId = (long) session.getAttribute("userId");
        model.addAttribute("user", userService.getUserFriends(userId));
        return "user/user-friends";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<Object> loginUser(HttpSession session,
                                            @RequestParam(value = "username") String username,
                                            @RequestParam(value = "password") String password) {

        if (session.getAttribute("loginStatus") != null)
            return new ResponseEntity<>("User Already Logged in!", HttpStatus.BAD_REQUEST);


        User user = userService.login(username, password);
        if (user == null) throw new NotFoundException("Wrong Login Or Password");
        session.setAttribute("loginStatus", "true");
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getUserName());
        session.setAttribute("realName", user.getRealName());
        session.setAttribute("birthDate", user.getBirthDate());
        session.setAttribute("userPosts", user.getPosts());
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/logout")
    public String logoutUser(HttpSession session) throws UnauthorizedException {
        if (session.getAttribute("loginStatus") == null) {
            throw new UnauthorizedException("User Already Logged Out");
        }
        session.removeAttribute("loginStatus");
        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.removeAttribute("realName");
        session.removeAttribute("birthDate");
        session.removeAttribute("userPosts");

        return "home";
    }


    @RequestMapping(method = RequestMethod.GET, path = "/incomeRequests")
    public String incomeRequests(HttpSession session, Model model) throws UnauthorizedException {
        String userId = String.valueOf(session.getAttribute("userId"));
        model.addAttribute("user", userService.getIncomeRequests(userId, session));

        return "user/user-friends";

    }

    @RequestMapping(method = RequestMethod.GET, path = "/outcomeRequests")
    public String outcomeRequests(HttpSession session, Model model) throws UnauthorizedException {

        String userId = String.valueOf(session.getAttribute("userId"));
        List<User> usersList = userService.getOutcomeRequests(userId, session);
        model.addAttribute("user", usersList);

        return "user/user-friends";

    }


    @RequestMapping(method = RequestMethod.GET, value = "/register-user")
    public String registerUserPage() {
        return "user/user-registration";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String loginPage() {
        return "user/user-login";
    }


}
