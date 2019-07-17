package Lesson1.Controller;

import Lesson1.JsonParser.JsonParser;
import Lesson1.Model.Post;
import Lesson1.Service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class PostController {

    private final PostService postService;
    private final JsonParser<Post> jsonParser;

    @Autowired
    public PostController(PostService postService, JsonParser<Post> jsonParser) {
        this.postService = postService;
        this.jsonParser = jsonParser;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/savePost")
    public String savePost(HttpServletRequest req, Model model) {
        try {
            model.addAttribute("post", postService.save(jsonParser.jsonToObject(req, Post.class)));
        } catch (IOException IOExc) {
            model.addAttribute("error", "You entered wrong data " + IOExc);
            return "400";
        } catch (Exception otherException) {
            model.addAttribute("error", otherException);
            return "500";
        }

        return "home";

    }

    @RequestMapping(method = RequestMethod.POST, value = "/updatePost")
    public String updatePost(HttpServletRequest req, Model model) {
        try {
            model.addAttribute("post", postService.update(jsonParser.jsonToObject(req, Post.class)));
        } catch (IOException IOExc) {
            model.addAttribute("error", "You entered wrong data " + IOExc);
            return "400";
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
    public String deletePost(HttpServletRequest req, Model model) {
        try {
            postService.delete(jsonParser.jsonToObject(req, Post.class));
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
        List<String> postList;
        try {
            JsonParser<Post> jsonParser = new JsonParser<>();
            postList = jsonParser.objectToJson(postService.getUserAndFriendsPosts(Long.valueOf(userId)));
        } catch (JsonProcessingException parseExc) {
            return new ResponseEntity<>(parseExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getAllPosts")
    public ResponseEntity<Object> getAllPosts() {
        List<String> postList;
        try {
            JsonParser<Post> jsonParser = new JsonParser<>();
            postList = jsonParser.objectToJson(postService.getAllPosts());

        } catch (JsonProcessingException parseExc) {
            return new ResponseEntity<>(parseExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getUserPosts")
    public ResponseEntity<Object> getUserPosts(String userId) {
        List<String> postList;
        try {
            JsonParser<Post> jsonParser = new JsonParser<>();
            postList = jsonParser.objectToJson(postService.getUserPosts(Long.valueOf(userId)));
        } catch (JsonProcessingException parseExc) {
            return new ResponseEntity<>(parseExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getFriendsPosts")
    public ResponseEntity<Object> getFriendsPosts(String userId) {
        List<String> postList;
        try {
            JsonParser<Post> jsonParser = new JsonParser<>();
            postList = jsonParser.objectToJson(postService.getFriendsPosts(Long.valueOf(userId)));
        } catch (JsonProcessingException parseExc) {
            return new ResponseEntity<>(parseExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

}