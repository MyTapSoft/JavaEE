package Lesson1.Controller;

import Lesson1.JsonParser.JsonParser;
import Lesson1.Model.Post;
import Lesson1.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class PostController {

    private PostService postService;
    private JsonParser<Post> jsonParser;

    @Autowired
    public PostController(PostService postService, JsonParser<Post> jsonParser) {
        this.postService = postService;
        this.jsonParser = jsonParser;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/savePost")
    public String savePost(HttpServletRequest req, Model model) {
        try {
            model.addAttribute("post", postService.savePost(jsonParser.jsonToObject(req, Post.class)));
        } catch (IOException e1) {
            model.addAttribute("error", "You entered wrong data " + e1);
            return "404";
        } catch (Exception e) {
            model.addAttribute("error", e);
            return "500";
        }

        return "home";

    }

    @RequestMapping(method = RequestMethod.POST, value = "/updatePost")
    public String updatePost(HttpServletRequest req, Model model) {
        try {
            model.addAttribute("post", postService.updatePost(jsonParser.jsonToObject(req, Post.class)));
        } catch (IOException e1) {
            model.addAttribute("error", "You entered wrong data " + e1);
            return "404";
        } catch (EntityExistsException empty) {
            model.addAttribute("error", empty + " It seems post doesn't exist. Nothing to update");
            return "404";
        } catch (Exception e) {
            model.addAttribute("error", e);
            return "500";
        }

        return "home";

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deletePost")
    public String deletePost(HttpServletRequest req, Model model) {
        try {
            postService.deletePost(jsonParser.jsonToObject(req, Post.class));
        } catch (NumberFormatException numberExcep) {
            model.addAttribute("error", "You entered wrong data " + numberExcep);
            return "404";
        } catch (EntityExistsException empty) {
            model.addAttribute("error", empty + " It seems post doesn't exist. Nothing to delete");
            return "404";
        } catch (Exception e) {
            model.addAttribute("error", e);
            return "500";
        }
        return "home";

    }

    @RequestMapping(method = RequestMethod.GET, value = "/post/{postId}")
    public String findPost(Model model, @PathVariable String postId) {
        try {
            model.addAttribute("post", postService.findPost(Long.parseLong(postId)));
        } catch (NumberFormatException numberExcep) {
            model.addAttribute("error", "You entered wrong numbers " + numberExcep);
            return "404";
        } catch (EntityExistsException empty) {
            model.addAttribute("error", empty + " It seems there's no post with ID: " + postId);
            return "404";
        } catch (Exception e) {
            model.addAttribute("error", e);
            return "500";
        }

        return "home";
    }
}