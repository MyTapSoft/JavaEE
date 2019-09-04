package Lesson1.controller;


import Lesson1.exceptions.NotFoundException;
import Lesson1.exceptions.UnauthorizedException;
import Lesson1.exceptions.globalHandler.ControllerAdviceExceptionHandler;
import Lesson1.model.User;
import Lesson1.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class UserRestController {

    private final UserService userService;
    private final static Logger log = Logger.getLogger(ControllerAdviceExceptionHandler.class);

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/registerUser", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        log.info("New user added successfully. ID " + user.getId());
        return new ResponseEntity<>("User Saved Successfully", HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateUser")
    public ResponseEntity<Object> updateUser(@ModelAttribute User user, HttpSession session) throws UnauthorizedException {
        userService.updateUser(user, session);
        log.info("User update data successfully. ID " + user.getId());
        return new ResponseEntity<>("User Updated Successfully", HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUser")
    public ResponseEntity<Object> deleteUser(HttpSession session) throws UnauthorizedException {
        Long userId = (Long) session.getAttribute("userId");
        userService.deleteUser(userId, session);
        logoutUser(session);
        log.info("User deleted successfully. ID " + userId);
        return new ResponseEntity<>("User With ID: " + userId + " Deleted Successfully", HttpStatus.OK);
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
    public ResponseEntity<Object> logoutUser(HttpSession session) throws UnauthorizedException {
        if (session.getAttribute("loginStatus") == null) {
            throw new UnauthorizedException("User Already Logged Out");
        }
        session.removeAttribute("loginStatus");
        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.removeAttribute("realName");
        session.removeAttribute("birthDate");
        session.removeAttribute("userPosts");
        return new ResponseEntity<>("User Logout Successfully", HttpStatus.OK);
    }
}
