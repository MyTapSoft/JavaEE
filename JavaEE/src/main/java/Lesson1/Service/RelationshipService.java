package Lesson1.Service;

import Lesson1.DAO.RelationshipDAO;
import Lesson1.Exceptions.BadRequestException;
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
        Relationship relationship = dao.getRelationship(from, to);
        if (relationship != null && (relationship.getStatus() == RelationshipStatus.deleted || relationship.getStatus() == RelationshipStatus.canceled)) {
            return updateRelationship(userIdFrom, userIdTo, "pending");
        }
        if (relationship != null) throw new BadRequestException("Relationship Already Exist");
        Relationship result = new Relationship();
        result.setUserIdFrom(from);
        result.setUserIdTo(to);
        result.setStatus(RelationshipStatus.pending);
        return dao.addRelationship(result);
    }

    public Relationship updateRelationship(String userIdFrom, String userIdTo, String status) throws BadRequestException {

        Relationship relationship = getRelationship(userIdFrom, userIdTo);

        if (relationship == null)
            throw new BadRequestException("Relationship doesn't exist");
        RelationshipStatus desiredStatus = RelationshipStatus.valueOf(status);
        if (relationship.getStatus() == desiredStatus)
            throw new BadRequestException("Same status already exist");
        if (desiredStatus == RelationshipStatus.pending && relationship.getStatus() != RelationshipStatus.deleted && relationship.getStatus() != RelationshipStatus.canceled)
            throw new BadRequestException("Incorrect status");
        if (desiredStatus == RelationshipStatus.decline && relationship.getStatus() != RelationshipStatus.pending)
            throw new BadRequestException("Incorrect status");
        if (desiredStatus == RelationshipStatus.deleted && relationship.getStatus() != RelationshipStatus.accepted)
            throw new BadRequestException("Incorrect status");
        if (desiredStatus == RelationshipStatus.accepted && relationship.getStatus() != RelationshipStatus.pending)
            throw new BadRequestException("Incorrect status");
        if (desiredStatus == RelationshipStatus.canceled && relationship.getStatus() != RelationshipStatus.pending)
            throw new BadRequestException("Incorrect status");

        relationship.setStatus(desiredStatus);
        return dao.updateRelationship(relationship);

    }

    public Relationship getRelationship(String userIdFrom, String userIdTo) {
        return dao.getRelationship(Long.parseLong(userIdFrom), Long.parseLong(userIdTo));
    }
}
