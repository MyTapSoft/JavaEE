package Lesson1.service;

import Lesson1.exceptions.BadRequestException;
import Lesson1.exceptions.DuplicateException;
import Lesson1.exceptions.InternalServerException;
import Lesson1.exceptions.NotFoundException;
import Lesson1.model.Relationship;
import Lesson1.model.RelationshipStatus;
import Lesson1.repository.RelationshipDAO;
import Lesson1.service.Validator.*;
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
        Long from = Long.parseLong(userIdFrom);
        Long to = Long.parseLong(userIdTo);

        Relationship relationship = validate(RelationshipStatus.pending, userIdFrom, userIdTo);

        if (relationship != null && (relationship.getStatus() == RelationshipStatus.deleted || relationship.getStatus() == RelationshipStatus.canceled)) {
            return updateRelationship(userIdFrom, userIdTo, RelationshipStatus.pending);
        }
        if (relationship != null) throw new DuplicateException("Relationship Already Exist");
        relationship = new Relationship();
        relationship.setUserIdFrom(from);
        relationship.setUserIdTo(to);
        relationship.setStatus(RelationshipStatus.pending);
        relationship.setFriendsRequestDate(new Date());
        return dao.addRelationship(relationship);
    }

    public Relationship updateRelationship(String userIdFrom, String userIdTo, RelationshipStatus status) throws BadRequestException {
        Relationship relationship = validate(status, userIdFrom, userIdTo);
        relationship.setStatus(status);
        relationship.setFriendsRequestDate(new Date());
        return dao.updateRelationship(relationship);

    }

    private Relationship getRelationship(String userIdFrom, String userIdTo) {
        return dao.getRelationship(Long.parseLong(userIdFrom), Long.parseLong(userIdTo));
    }

    private Long getFriendsAmount(Long userId) {
        return dao.getFriendsAmount(userId);
    }

    private Long getRequestAmount(Long userId) {
        return dao.getRequestAmount(userId);
    }

    private Date getFriendRequestDate(Long userIdFrom, Long userIdTo) {
        Date friendsRequestDate = dao.getFriendRequestDate(userIdFrom, userIdTo);
        if (friendsRequestDate == null) throw new InternalServerException("Date not found. ID from: " + userIdFrom + " ID to: " + userIdTo);
        return friendsRequestDate;
    }

    private Relationship validate(RelationshipStatus desiredStatus, String userIdOne, String userIdTwo) throws BadRequestException {
        long idOne = Long.parseLong(userIdOne);
        long idTwo = Long.parseLong(userIdTwo);
        if (idOne == idTwo) throw new BadRequestException("IDs Are Same");

        Relationship relationship = getRelationship(userIdOne, userIdTwo);
        if (relationship == null) throw new NotFoundException("There's no relationship between users with ID: " + idOne + " ID: " + idTwo);
        if (relationship.getStatus() == desiredStatus) throw new DuplicateException("Status Already Established between users with ID: " + idOne + " ID: " + idTwo);

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
