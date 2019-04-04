package Lesson1.Controller;

import Lesson1.JsonParser.JsonParser;
import Lesson1.Model.Post;
import Lesson1.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
    public @ResponseBody
    String savePost(HttpServletRequest req) throws Exception {
        return postService.savePost(jsonParser.jsonToObject(req, Post.class)).toString();

    }

    @RequestMapping(method = RequestMethod.POST, value = "/updatePost")
    public @ResponseBody
    String updatePost(HttpServletRequest req) throws Exception {
        return postService.updatePost(jsonParser.jsonToObject(req, Post.class)).toString();

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deletePost")
    public @ResponseBody
    String deletePost(HttpServletRequest req) throws Exception {

        postService.deletePost(jsonParser.jsonToObject(req, Post.class));
        return "Deleted successfully";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getPost", params = {"id"})
    public @ResponseBody
    String findPost(@RequestParam(value = "id") long id) throws Exception {
        return postService.findPost(id).toString();
    }
}