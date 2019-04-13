package Lesson1.Service;


import Lesson1.DAO.PostDAO;
import Lesson1.Model.Post;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

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
        Post result = dao.findPost(id);
        if (result == null) throw new EntityExistsException();
        return result;

    }
}
