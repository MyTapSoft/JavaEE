package Lesson1.Service;

import Lesson1.DAO.RelationshipDAO;
import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.Relationship;
import Lesson1.Model.RelationshipStatus;
import Lesson1.Service.ChainOfResponsibility.Chain;
import Lesson1.Service.ChainOfResponsibility.FriendsAmountChain;
import Lesson1.Service.ChainOfResponsibility.RequestAmountChain;
import Lesson1.Service.ChainOfResponsibility.RequestDateChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

        Chain chain = new FriendsAmountChain(getFriendsAmount(from));
        Chain chain1 = new RequestAmountChain(getRequestAmount(from));
        Chain chain2 = new RequestDateChain(getFriendRequestDate(from, to));

        chain.setNextChain(chain1);
        chain1.setNextChain(chain2);

        chain.check();

        if (relationship != null && (relationship.getStatus() == RelationshipStatus.deleted || relationship.getStatus() == RelationshipStatus.canceled)) {
            return updateRelationship(userIdFrom, userIdTo, RelationshipStatus.pending);
        }
        if (relationship != null) throw new BadRequestException("Relationship Already Exist");
        Relationship result = new Relationship();
        result.setUserIdFrom(from);
        result.setUserIdTo(to);
        result.setStatus(RelationshipStatus.pending);
        return dao.addRelationship(result);
    }

    public Relationship updateRelationship(String userIdFrom, String userIdTo, RelationshipStatus status) throws BadRequestException {

        Relationship relationship = getRelationship(userIdFrom, userIdTo);

        if (relationship == null)
            throw new BadRequestException("Relationship doesn't exist");

        if (relationship.getStatus() == status)
            throw new BadRequestException("Same status already exist");
        if (status == RelationshipStatus.pending && relationship.getStatus() != RelationshipStatus.deleted && relationship.getStatus() != RelationshipStatus.canceled)
            throw new BadRequestException("Incorrect status");
        if (status == RelationshipStatus.decline && relationship.getStatus() != RelationshipStatus.pending)
            throw new BadRequestException("Incorrect status");
        if (status == RelationshipStatus.deleted && relationship.getStatus() != RelationshipStatus.accepted)
            throw new BadRequestException("Incorrect status");
        if (status == RelationshipStatus.accepted && relationship.getStatus() != RelationshipStatus.pending)
            throw new BadRequestException("Incorrect status");
        if (status == RelationshipStatus.canceled && relationship.getStatus() != RelationshipStatus.pending)
            throw new BadRequestException("Incorrect status");

        relationship.setStatus(status);
        return dao.updateRelationship(relationship);

    }

    public Relationship getRelationship(String userIdFrom, String userIdTo) {
        return dao.getRelationship(Long.parseLong(userIdFrom), Long.parseLong(userIdTo));
    }

    private short getFriendsAmount(long userId) {
        return dao.getFriendsAmount(userId);
    }

    private short getRequestAmount(long userId){
        return dao.getRequestAmount(userId);
    }

    private Date getFriendRequestDate(long userIdFrom, long userIdTo){
        return dao.getFriendRequestDate(userIdFrom, userIdTo);
    }
}
