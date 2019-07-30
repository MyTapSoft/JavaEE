package Lesson1.Controller;


import Lesson1.Exceptions.BadRequestException;
import Lesson1.Exceptions.UnauthorizedException;
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
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PostController {

    private final PostService postService;


    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/savePost")
    public ResponseEntity<Object> savePost(@ModelAttribute Post post) {

        Post result = null;
        try {
            result = postService.save(post);
        } catch (BadRequestException badRequest) {
            new ResponseEntity<>(badRequest.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception otherException) {
            new ResponseEntity<>(otherException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.PUT, path = "/updatePost")
    public ResponseEntity<Object> updatePost(@ModelAttribute Post post) {
        Post result = null;
        try {
            result = postService.update(post);
        } catch (EntityExistsException emptyExc) {
            return new ResponseEntity<>(emptyExc, HttpStatus.BAD_REQUEST);
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/deletePost")
    public ResponseEntity<Object> deletePost(String postId) {
        try {
            postService.delete(Long.valueOf(postId));
        } catch (NumberFormatException numberExc) {
            return new ResponseEntity<>(numberExc, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EntityExistsException emptyExc) {
            return new ResponseEntity<>(emptyExc, HttpStatus.BAD_REQUEST);
        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Post with ID" + postId + " has been deleted", HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/post/{postId}")
    public ResponseEntity<Object> getPostsById(@PathVariable String postId, Model model) {
        Post result = null;
        try {
            result = postService.getById(Long.parseLong(postId));
        } catch (NumberFormatException numberExc) {
            return new ResponseEntity<>(numberExc, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (EntityExistsException emptyExc) {
            return new ResponseEntity<>(emptyExc, HttpStatus.BAD_REQUEST);

        } catch (Exception otherExc) {
            return new ResponseEntity<>(otherExc, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getBasicPosts")
    public ResponseEntity<Object> getUserAndFriendsPosts(String userId) {
        List<Post> postList;
        try {
            postList = postService.getUserAndFriendsPosts(Long.valueOf(userId));
        } catch (NumberFormatException numberExc) {
            return new ResponseEntity<>(numberExc.getMessage(), HttpStatus.BAD_REQUEST);
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

    @RequestMapping(method = RequestMethod.GET, value = "/feed")
    public String feed(HttpSession session, Model model, String offset) {
        try {
            if (session.getAttribute("loginStatus") == null) throw new UnauthorizedException("You have to login first");
            Long userId = (Long) session.getAttribute("userId");
            short offsets = Short.valueOf(offset);
            model.addAttribute("posts", postService.getFeed(userId, offsets));
        } catch (UnauthorizedException unauthorized) {
            model.addAttribute("error", unauthorized.getMessage());
            return "401";
        } catch (NumberFormatException numberExc) {
            model.addAttribute("error", numberExc.getMessage());
        } catch (Exception otherExc) {
            model.addAttribute("error", otherExc.getMessage());
        }
        return "posts/feed";
    }


}