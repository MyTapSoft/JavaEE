package Lesson1.Service;

import Lesson1.DAO.RelationshipDAO;
import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.Relationship;
import Lesson1.Model.RelationshipStatus;
import Lesson1.Service.Validator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RelationshipService {
    private final RelationshipDAO dao;

    @Autowired
    public RelationshipService(RelationshipDAO dao) {
        this.dao = dao;
    }


    public Relationship addRelationship(String userIdFrom, String userIdTo) throws BadRequestException {
        long from = Long.parseLong(userIdFrom);
        long to = Long.parseLong(userIdTo);

        Relationship relationship = validate(RelationshipStatus.pending, userIdFrom, userIdTo);

        if (relationship != null && (relationship.getStatus() == RelationshipStatus.deleted || relationship.getStatus() == RelationshipStatus.canceled)) {
            return updateRelationship(userIdFrom, userIdTo, RelationshipStatus.pending);
        }
        if (relationship != null) throw new BadRequestException("Relationship Already Exist");
        relationship = new Relationship();
        relationship.setUserIdFrom(from);
        relationship.setUserIdTo(to);
        relationship.setStatus(RelationshipStatus.pending);
        relationship.setFriendsRequestDate(new Date());
        return dao.addRelationship(relationship);
    }

    public Relationship updateRelationship(String userIdFrom, String userIdTo, RelationshipStatus status) throws BadRequestException {
        Relationship relationship = validate(status, userIdFrom, userIdTo);
        if (relationship == null) throw new BadRequestException("Relationship doesn't exist");
        relationship.setStatus(status);
        relationship.setFriendsRequestDate(new Date());
        return dao.updateRelationship(relationship);

    }

    private Relationship getRelationship(String userIdFrom, String userIdTo) {
        return dao.getRelationship(Long.parseLong(userIdFrom), Long.parseLong(userIdTo));
    }

    private long getFriendsAmount(long userId) {
        return dao.getFriendsAmount(userId);
    }

    private short getRequestAmount(long userId) {
        return dao.getRequestAmount(userId);
    }

    private Date getFriendRequestDate(long userIdFrom, long userIdTo) {
        return dao.getFriendRequestDate(userIdFrom, userIdTo);
    }

    private Relationship validate(RelationshipStatus desiredStatus, String userIdOne, String userIdTwo) throws BadRequestException {
        long idOne = Long.parseLong(userIdOne);
        long idTwo = Long.parseLong(userIdTwo);
        if (idOne == idTwo) throw new BadRequestException("IDs Are Same");

        Relationship relationship = getRelationship(userIdOne, userIdTwo);
        if (relationship == null) return null;

        if (relationship.getStatus() == desiredStatus) throw new BadRequestException("Status Already Established");

        Chain chain, chain1, chain2, chain3, chain4, chain5, chain6, chain7;
        chain = new FriendsAmountChain(RelationshipStatus.accepted, getFriendsAmount(idOne));
        chain1 = chain.setNextChain(new RequestDateChain(RelationshipStatus.deleted, getFriendRequestDate(idOne, idTwo)));
        chain2 = chain1.setNextChain(new RequestAmountChain(RelationshipStatus.pending, getRequestAmount(idOne)));
        chain3 = chain2.setNextChain(new RelationshipStatusAcceptedChain(RelationshipStatus.accepted, relationship.getStatus()));
        chain4 = chain3.setNextChain(new RelationshipStatusCancelChain(RelationshipStatus.canceled, relationship.getStatus()));
        chain5 = chain4.setNextChain(new RelationshipStatusDeclineChain(RelationshipStatus.decline, relationship.getStatus()));
        chain6 = chain5.setNextChain(new RelationshipStatusDeleteChain(RelationshipStatus.deleted, relationship.getStatus()));
        chain7 = chain6.setNextChain(new RelationshipStatusPendingChain(RelationshipStatus.pending, relationship.getStatus()));
        chain.validate(desiredStatus);
        return relationship;
    }

}
