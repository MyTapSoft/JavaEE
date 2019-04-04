package Lesson1.Service;


import Lesson1.DAO.PostDAO;
import Lesson1.Model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private PostDAO dao;

    @Autowired
    public PostService(PostDAO dao) {
        this.dao = dao;
    }

    public Post savePost(Post post) throws Exception {
        return dao.savePost(post);
    }

    public void deletePost(Post post) throws Exception {
        dao.deletePost(post.getId());
    }

    public Post updatePost(Post post) throws Exception {
        return dao.updatePost(post);
    }

    public Post findPost(long id) throws Exception {
        return dao.findPost(id);
    }
}
