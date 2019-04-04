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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
    public String createUser(HttpServletRequest req) throws Exception {
        userService.saveUser(jsonParser.jsonToObject(req, User.class));
        return "ADS_CREATE";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/updateUser")
    public String updateUser(HttpServletRequest req) throws Exception {
        userService.updateUser(jsonParser.jsonToObject(req, User.class));
        userService.saveUser(jsonParser.jsonToObject(req, User.class));
        return "ADS_UPDATE";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    public String getUser(Model model, @PathVariable String userId) throws Exception {
        try {
            model.addAttribute("user", userService.getUser(Integer.valueOf(userId)));
        } catch (NumberFormatException error){
            model.addAttribute("error", error);
            System.out.println(error);
            return "500";
        }
        return "user";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteUser")
    public @ResponseBody
    String deleteUser(HttpServletRequest req) throws Exception {
        User user = jsonParser.jsonToObject(req, User.class);
        userService.deleteUser(user);
        return "User with id " + user.getId() + " was delete successfully";
    }
}
