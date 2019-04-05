package Lesson1.Controller;


import Lesson1.JsonParser.JsonParser;
import Lesson1.Model.User;
import Lesson1.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class UserController {

    private UserService userService;
    private JsonParser<User> jsonParser;

    @Autowired
    public UserController(UserService userService, JsonParser<User> jsonParser) {
        this.userService = userService;
        this.jsonParser = jsonParser;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saveUser")
    public String createUser(HttpServletRequest req, Model model) {
        try {
            userService.saveUser(jsonParser.jsonToObject(req, User.class));
        } catch (IOException ioExcep) {
            model.addAttribute("error", ioExcep);
            return "404";
        } catch (Exception e) {
            model.addAttribute("error", e);
            return "500";
        }

        return "home";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/updateUser")
    public String updateUser(HttpServletRequest req, Model model) {
        try {
            userService.updateUser(jsonParser.jsonToObject(req, User.class));
        } catch (IOException ioExcep) {
            model.addAttribute("error", ioExcep);
            return "404";
        } catch (Exception e) {
            model.addAttribute("error", e);
            return "500";
        }

        return "home";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    public String getUser(Model model, @PathVariable String userId) {
        try {
            model.addAttribute("user", userService.getUser(Long.valueOf(userId)));
        } catch (NumberFormatException numberExcep) {
            model.addAttribute("error", numberExcep);
            return "404";
        } catch (Exception e) {
            model.addAttribute("error", e);
            return "500";
        }
        return "user";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUser")
    public String deleteUser(HttpServletRequest req, Model model) {
        try {
            User user = jsonParser.jsonToObject(req, User.class);
            userService.deleteUser(user);
        } catch (IOException ioExcep) {
            model.addAttribute("error", ioExcep);
            return "404";
        } catch (Exception e) {
            model.addAttribute("error", e);
            return "500";
        }
        return "home";
    }
}
