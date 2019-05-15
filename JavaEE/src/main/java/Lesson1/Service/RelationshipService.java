package Lesson1.Service;

import Lesson1.DAO.RelationshipDAO;
import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.Relationship;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RelationshipService {
    private RelationshipDAO dao;

    @Autowired
    public RelationshipService(RelationshipDAO dao) {
        this.dao = dao;
    }


    public Relationship addRelationship(String userIdFrom, String userIdTo) throws BadRequestException, DuplicateMemberException {
        Relationship relationship = createNewRelationship(userIdFrom, userIdTo);
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

    private Relationship createNewRelationship(String userIdFrom, String userIdTo) throws BadRequestException, DuplicateMemberException {
        long from = Long.parseLong(userIdFrom);
        long to = Long.parseLong(userIdTo);
        if (from == to) throw new BadRequestException("IDs Are Same");
        if (dao.getRelationship(from, to) != null) throw new DuplicateMemberException("Relationship Already Exist");
        Relationship result = new Relationship();
        result.setUserIdFrom(from);
        result.setUserIdTo(to);
        result.setStatus((short) 0);
        return result;
    }

}
