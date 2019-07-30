package Lesson1.Controller;


import Lesson1.Exceptions.BadRequestException;
import Lesson1.Exceptions.UnauthorizedException;
import Lesson1.Model.User;
import Lesson1.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @RequestMapping(path = "/registerUser", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) {
        try {
            userService.saveUser(user);
        } catch (BadRequestException duplicateExc) {
            return new ResponseEntity<>(duplicateExc.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("User Saved Successfully", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/updateUser")
    public String updateUser(@ModelAttribute User user, Model model, HttpSession session) {
        try {
            userService.updateUser(user, session);
        } catch (EntityExistsException emptyEntity) {
            model.addAttribute("error", emptyEntity + " It seems user doesn't exist. Nothing to update");
            return "404";
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc);
            return "500";
        }

        return "user-profile";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    public String getUser(Model model, @PathVariable String userId) {
        try {
            User user = userService.getUser(Long.parseLong(userId));
            model.addAttribute("user", user);
        } catch (NumberFormatException parseException) {
            model.addAttribute("error", "You entered wrong ID " + parseException);
            return "400";
        } catch (EntityExistsException emptyEntity) {
            model.addAttribute("error", emptyEntity + " It seems there's no user with ID: " + userId);
            return "404";
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc);
            return "500";
        }
        return "user/profile";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUser")
    public String deleteUser(HttpSession session, Model model) {
        try {

            Long userId = (Long) session.getAttribute("userId");
            userService.deleteUser(userId, session);
        } catch (NumberFormatException parseException) {
            model.addAttribute("error", parseException);
            return "400";
        } catch (EntityExistsException emptyEntity) {
            model.addAttribute("error", emptyEntity + " It seems user doesn't exist");
            return "404";
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc);
            return "500";
        }
        return "home";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<Object> loginUser(HttpSession session,
                                            @RequestParam(value = "username") String username,
                                            @RequestParam(value = "password") String password) {
        User user;
        try {

            if (session.getAttribute("loginStatus") != null)
                return new ResponseEntity<>("User Already Logged in!", HttpStatus.BAD_REQUEST);
            user = userService.login(username, password);
            session.setAttribute("loginStatus", "true");
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getUserName());
            session.setAttribute("realName", user.getRealName());
            session.setAttribute("birthDate", user.getBirthDate());
            session.setAttribute("userPosts", user.getPosts());
        } catch (BadRequestException badRequest) {
            return new ResponseEntity<>(badRequest.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/allUsers")
    public String allUsersPage(Model model) {
        try {
            model.addAttribute("user", userService.getAllUsers());
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc);
        }
        return "user/all-users";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/friends")
    public String getUserFriends(Model model, HttpSession session) {
        if (session.getAttribute("loginStatus") == null) {
            model.addAttribute("error", "You have to login first");
            return "401";
        }
        try {
            long userId = (long) session.getAttribute("userId");
            model.addAttribute("user", userService.getUserFriends(userId));
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc);
        }
        return "user/user-friends";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/logout")
    public String logoutUser(Model model, HttpSession session) {
        try {
            session.removeAttribute("loginStatus");
            session.removeAttribute("userId");
            session.removeAttribute("userName");
            session.removeAttribute("realName");
            session.removeAttribute("birthDate");
            session.removeAttribute("userPosts");
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc.getMessage());
        }
        return "home";

    }


    @RequestMapping(method = RequestMethod.GET, path = "/incomeRequests")
    public String incomeRequests(HttpSession session, Model model) {
        try {
            String userId = String.valueOf(session.getAttribute("userId"));
            model.addAttribute("user", userService.getIncomeRequests(userId, session));
        } catch (UnauthorizedException unauthorized) {
            model.addAttribute("error", unauthorized);
            return "401";
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc.getMessage());
        }
        return "user/user-friends";

    }

    @RequestMapping(method = RequestMethod.GET, path = "/outcomeRequests")
    public String outcomeRequests(HttpSession session, Model model) {
        try {
            String userId = String.valueOf(session.getAttribute("userId"));
            List<User> usersList = userService.getOutcomeRequests(userId, session);
            model.addAttribute("user", usersList);
        } catch (UnauthorizedException unauthorized) {
            model.addAttribute("error", unauthorized.getMessage());
            return "401";
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc.getMessage());
        }
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
