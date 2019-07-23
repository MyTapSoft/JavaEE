package Lesson1.Service;

import Lesson1.DAO.PostDAO;
import Lesson1.Model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
public class PostService {

    private final PostDAO dao;

    @Autowired
    public PostService(PostDAO dao) {
        this.dao = dao;
    }

    public Post save(Post post) {
        return dao.savePost(post);
    }

    public void delete(long id) {
        dao.deletePost(id);
    }

    public Post update(Post post) {
        return dao.updatePost(post);
    }

    public Post getById(long id) {
        Post result = dao.findPost(id);
        if (result == null) throw new EntityExistsException();
        return result;

    }

    public List<Post> getUserAndFriendsPosts(long id) {
        return dao.getUserAndFriendsPosts(id);
    }

    public List<Post> getFriendsPosts(long id) {
        return dao.getFriendsPosts(id);
    }

    public List<Post> getAllPosts() {
        return dao.getAllPosts();
    }

    public List<Post> getUserPosts(long id) {
        return dao.getUserPosts(id);
    }
}
