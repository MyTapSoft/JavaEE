package Lesson1.controller;

import Lesson1.exceptions.UnauthorizedException;
import Lesson1.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class PostViewController {

    private PostService postService;


    @Autowired
    public PostViewController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/feed")
    public String feed(HttpSession session, Model model, String offset) throws UnauthorizedException {
        if (session.getAttribute("loginStatus") == null) throw new UnauthorizedException("You have to login first");
        Long userId = (Long) session.getAttribute("userId");
        short offsets = Short.parseShort(offset);
        model.addAttribute("posts", postService.getFeed(userId, offsets));
        return "posts/feed";
    }
}
