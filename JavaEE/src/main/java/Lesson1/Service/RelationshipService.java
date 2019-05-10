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
        //Status code:	0 - Pending, 1 - Accepted, 2 - Declined, 3 - Deleted
        Relationship relationship = dao.getRelationship(Long.parseLong(userIdFrom), Long.parseLong(userIdTo));
        if (relationship == null)
            throw new BadRequestException("Relationship doesn't exist");
        short desiredStatus = Short.parseShort(status);
        if (relationship.getStatus() == desiredStatus)
            throw new BadRequestException("Same status already exist");
        if (desiredStatus == 3 && relationship.getStatus() != 1)
            throw new BadRequestException("Incorrect status");
        if (desiredStatus == 2 && relationship.getStatus() != 0)
            throw new BadRequestException("Incorrect status");
        if (desiredStatus == 1 && relationship.getStatus() != 0 || relationship.getStatus() != 3)
            throw new BadRequestException("Incorrect status");
        relationship.setStatus(desiredStatus);
        return dao.updateRelationship(relationship);
        //При такой структуре пользователь может принять/отклонить запрос за другого пользователя.
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
        Relationship relationship = dao.getRelationship(from, to);
        if (relationship != null) throw new BadRequestException("Relationship Already Exist");
        Relationship result = new Relationship();
        result.setUserIdFrom(from);
        result.setUserIdTo(to);
        result.setStatus((short) 0);
        return result;
    }

}
