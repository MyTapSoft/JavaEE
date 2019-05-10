package Lesson1.Service;

import Lesson1.DAO.RelationshipDAO;
import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipService {
    private RelationshipDAO dao;

    @Autowired
    public RelationshipService(RelationshipDAO dao) {
        this.dao = dao;
    }

    public Relationship addRelationship(String userIdFrom, String userIdTo) throws BadRequestException {

        Relationship relationship = createNewRelationship(userIdFrom, userIdTo);
        if (dao.getRelationship(relationship.getUserIdFrom(), relationship.getUserIdTo()) != null)
            throw new BadRequestException("Relationship Already Exist");
        return dao.addRelationship(relationship);
    }

    public Relationship updateRelationship(String userIdFrom, String userIdTo, String status) throws BadRequestException {
        if (dao.getRelationship(Long.parseLong(userIdFrom), Long.parseLong(userIdTo)) == null)
            throw new BadRequestException("Relationship doesn't exist");
        Relationship relationship = createNewRelationship(userIdFrom, userIdTo);
        relationship.setStatus(Short.parseShort(status));
        return dao.updateRelationship(relationship);
    }

    public List<Relationship> getIncomeRequests(String userId) {
        return dao.getIncomeRequests(userId);

    }

    public List<Relationship> getOutcomeRequests(String userId) {
        return dao.getOutcomeRequests(userId);

    }

    private Relationship createNewRelationship(String userIdFrom, String userIdTo) throws BadRequestException {
        long from = Long.parseLong(userIdFrom);
        long to = Long.parseLong(userIdTo);
        if (from == to) throw new BadRequestException("IDs Are Same");
        Relationship relationship = new Relationship();
        relationship.setUserIdFrom(from);
        relationship.setUserIdTo(to);
        relationship.setStatus((short) 0);
        return relationship;
    }

}
