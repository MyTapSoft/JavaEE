package Lesson1.Service;

import Lesson1.DAO.PostDAO;
import Lesson1.Model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
public class PostService {

    private final PostDAO dao;

    @Autowired
    public PostService(PostDAO dao) {
        this.dao = dao;
    }

    public Post savePost(Post post)  {
        return dao.savePost(post);
    }

    public void deletePost(Post post) {
        dao.deletePost(post.getId());
    }

    public Post updatePost(Post post)  {
        return dao.updatePost(post);
    }

    public Post findPost(long id) {
        Post result = dao.findPost(id);
        if (result == null) throw new EntityExistsException();
        return result;

    }
}
