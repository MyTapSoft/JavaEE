package Lesson1.service.Validator;

import Lesson1.exceptions.BadRequestException;
import Lesson1.model.RelationshipStatus;

public class FriendsAmountChain extends Chain {
    private Long friendsAmount;

    public FriendsAmountChain(RelationshipStatus status, long friendsAmount) {
        this.certainChainStatus = status;
        this.friendsAmount = friendsAmount;

    }

    @Override
    public void check() throws BadRequestException {
        if (friendsAmount >= 100) throw new BadRequestException("Friends Limit");
    }
}
