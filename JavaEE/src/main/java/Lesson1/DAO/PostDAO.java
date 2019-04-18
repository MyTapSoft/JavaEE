package Lesson1.DAO;

import Lesson1.Model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PostDAO {
    private GeneralDAO<Post> dao;

    @Autowired
    public PostDAO(GeneralDAO<Post> dao) {
        this.dao = dao;
    }

    public Post savePost(Post post)  {
        return dao.save(post);
    }

    public Post updatePost(Post post)  {
        return dao.update(post);
    }

    public void deletePost(long id)  {
        dao.delete(id, Post.class);
    }

    public Post findPost(long id)  {
        return dao.getEntity(id, Post.class);
    }
}