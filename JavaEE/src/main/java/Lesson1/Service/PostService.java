package Lesson1.Service;

import Lesson1.DAO.PostDAO;
import Lesson1.DAO.RelationshipDAO;
import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.Post;
import Lesson1.Model.Relationship;
import Lesson1.Model.RelationshipStatus;
import Lesson1.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class PostService {

    private final PostDAO dao;
    private final RelationshipDAO relationshipDAO;

    @Autowired
    public PostService(PostDAO dao, RelationshipDAO relationshipDAO) {
        this.dao = dao;
        this.relationshipDAO = relationshipDAO;
    }

    public Post save(Post post) throws BadRequestException {
        validation(post);
        return dao.savePost(post);
    }

    public void delete(long id) throws BadRequestException {
        Post post = dao.getPost(id);
        if (post.getUserPosted().getId() != id) throw new BadRequestException("You can't modify this post");
        dao.deletePost(id);
    }

    public Post update(Post post) throws BadRequestException {
        validation(post);
        return dao.updatePost(post);
    }

    public Post getById(long id) {
        Post result = dao.getPost(id);
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

    public List<Post> getFeed(long userId, short offset, HttpSession session) {


        return dao.getFeed(userId, offset);
    }


    public List<Post> getUserPosts(long id) {
        return dao.getUserPosts(id);
    }

    private void validation(Post post) throws BadRequestException {
        User userPosted = post.getUserPosted();
        User userPagePosted = post.getUserPagePosted();
        Relationship isFriends = relationshipDAO.getRelationship(userPosted.getId(), userPagePosted.getId());
        if (isFriends == null || isFriends.getStatus() != RelationshipStatus.accepted)
            throw new BadRequestException("User's not a friend");
        if (post.getMessage().replaceAll("\\s+", "").isEmpty()) throw new BadRequestException("Message is empty");
    }
}
