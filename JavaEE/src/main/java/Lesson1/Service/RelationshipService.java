package Lesson1.Service;

import Lesson1.DAO.RelationshipDAO;
import Lesson1.Exceptions.BadRequestException;
import Lesson1.Exceptions.InternalServerException;
import Lesson1.Model.Relationship;
import Lesson1.Model.RelationshipStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationshipService {
    private RelationshipDAO dao;

    @Autowired
    public RelationshipService(RelationshipDAO dao) {
        this.dao = dao;
    }


    public Relationship addRelationship(String userIdFrom, String userIdTo) throws BadRequestException {
        long from = Long.parseLong(userIdFrom);
        long to = Long.parseLong(userIdTo);
        if (from == to) throw new BadRequestException("IDs Are Same");
        if (dao.getRelationship(from, to) != null) throw new BadRequestException("Relationship Already Exist");
        Relationship result = new Relationship();
        result.setUserIdFrom(from);
        result.setUserIdTo(to);
        result.setStatus(RelationshipStatus.pending);
        return dao.addRelationship(result);

    }

    public Relationship updateRelationship(String userIdFrom, String userIdTo, String status) throws BadRequestException, InternalServerException {

        Relationship relationship = dao.getRelationship(Long.parseLong(userIdFrom), Long.parseLong(userIdTo));
        if (relationship == null)
            throw new BadRequestException("Relationship doesn't exist");
        RelationshipStatus desiredStatus = statusMapper(status);
        if (relationship.getStatus() == desiredStatus)
            throw new BadRequestException("Same status already exist");
        if (desiredStatus == RelationshipStatus.decline && relationship.getStatus() != RelationshipStatus.pending)
            throw new BadRequestException("Incorrect status");
        if (desiredStatus == RelationshipStatus.deleted && relationship.getStatus() != RelationshipStatus.accepted)
            throw new BadRequestException("Incorrect status");
        if (desiredStatus == RelationshipStatus.accepted && relationship.getStatus() != RelationshipStatus.pending
                || relationship.getStatus() != RelationshipStatus.deleted)
            throw new BadRequestException("Incorrect status");
        relationship.setStatus(desiredStatus);
        return dao.updateRelationship(relationship);

    }

    private RelationshipStatus statusMapper(String status) throws InternalServerException {
        //Status code:	0 - Pending, 1 - Accepted, 2 - Declined, 3 - Deleted
        switch (status) {
            case "0":
                return RelationshipStatus.pending;
            case "1":
                return RelationshipStatus.accepted;
            case "2":
                return RelationshipStatus.decline;
            case "3":
                return RelationshipStatus.deleted;
        }
        throw new InternalServerException("Can't parse status: " + status);
    }


}
