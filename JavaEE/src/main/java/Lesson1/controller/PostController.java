package Lesson1.controller;


import Lesson1.exceptions.BadRequestException;
import Lesson1.exceptions.globalHandler.ControllerAdviceExceptionHandler;
import Lesson1.exceptions.UnauthorizedException;
import Lesson1.model.Post;
import Lesson1.service.PostService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class PostController {

    private final PostService postService;
    private final static Logger log = Logger.getLogger(ControllerAdviceExceptionHandler.class);


    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/savePost")
    public ResponseEntity<Object> savePost(@ModelAttribute Post post) throws BadRequestException {
        Post result = postService.save(post);
        log.info("New post added successfully. Post ID: " + result.getId() + ". User ID: " + result.getUserPosted().getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/updatePost")
    public ResponseEntity<Object> updatePost(@ModelAttribute Post post) throws BadRequestException {
        Post result = postService.update(post);
        log.info("Post updated successfully. Post ID: " + result.getId() + ". User ID: " + result.getUserPosted().getId());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/deletePost")
    public ResponseEntity<Object> deletePost(String postId) throws BadRequestException {
        postService.delete(Long.parseLong(postId));
        log.info("Post deleted successfully. Post ID: " + postId);

        return new ResponseEntity<>("Post with ID" + postId + " has been deleted", HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/post/{postId}")
    public ResponseEntity<Object> getPostsById(@PathVariable String postId, Model model) {
        return new ResponseEntity<>(postService.getById(Long.parseLong(postId)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getBasicPosts")
    public ResponseEntity<Object> getUserAndFriendsPosts(String userId) {
        return new ResponseEntity<>(postService.getUserAndFriendsPosts(Long.parseLong(userId)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getAllPosts")
    public ResponseEntity<Object> getAllPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getUserPosts")
    public ResponseEntity<Object> getUserPosts(String userId) {
        return new ResponseEntity<>(postService.getUserPosts(Long.parseLong(userId)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getFriendsPosts")
    public ResponseEntity<Object> getFriendsPosts(String userId) {
        return new ResponseEntity<>(postService.getFriendsPosts(Long.parseLong(userId)), HttpStatus.OK);
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