package Lesson1.Controller;


import Lesson1.Model.Post;
import Lesson1.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityExistsException;
import java.util.List;

@Controller
public class PostController {

    private final PostService postService;


    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/savePost")
    public String savePost(@ModelAttribute Post post, Model model) {
        try {
            model.addAttribute("post", postService.save(post));
        }  catch (Exception otherException) {
            model.addAttribute("error", otherException);
            return "500";
        }

        return "home";

    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/updatePost")
    public String updatePost(@ModelAttribute Post post, Model model) {
        try {
            model.addAttribute("post", postService.update(post));
        } catch (EntityExistsException emptyExc) {
            model.addAttribute("error", emptyExc + " It seems posts doesn't exist. Nothing to update");
            return "404";
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc);
            return "500";
        }

        return "home";

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deletePost")
    public String deletePost(@PathVariable String postId, Model model) {
        try {
            postService.delete(Long.valueOf(postId));
        } catch (NumberFormatException numberExc) {
            model.addAttribute("error", "You entered wrong data " + numberExc);
            return "400";
        } catch (EntityExistsException emptyExc) {
            model.addAttribute("error", emptyExc + " It seems posts doesn't exist. Nothing to delete");
            return "404";
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc);
            return "500";
        }
        return "home";

    }

    @RequestMapping(method = RequestMethod.GET, value = "/post/{postId}")
    public String getPostsById(Model model, @PathVariable String postId) {
        try {
            model.addAttribute("post", postService.getById(Long.parseLong(postId)));
        } catch (NumberFormatException numberExc) {
            model.addAttribute("error", "You entered wrong numbers " + numberExc);
            return "400";
        } catch (EntityExistsException emptyExc) {
            model.addAttribute("error", emptyExc + " It seems there's no posts with ID: " + postId);
            return "404";
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc);
            return "500";
        }

        return "posts/post";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getBasicPosts")
    public ResponseEntity<Object> getUSerAndFriendsPosts(String userId) {
        List<Post> postList;
        try {
            postList = postService.getUserAndFriendsPosts(Long.valueOf(userId));
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getAllPosts")
    public ResponseEntity<Object> getAllPosts() {
        List<Post> postList;
        try {

            postList = postService.getAllPosts();

        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getUserPosts")
    public ResponseEntity<Object> getUserPosts(String userId) {
        List<Post> postList;
        try {
            postList = postService.getUserPosts(Long.valueOf(userId));
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getFriendsPosts")
    public ResponseEntity<Object> getFriendsPosts(String userId) {
        List<Post> postList;
        try {
            postList = postService.getFriendsPosts(Long.valueOf(userId));
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

}