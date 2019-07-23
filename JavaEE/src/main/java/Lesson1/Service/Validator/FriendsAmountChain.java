package Lesson1.Service.Validator;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.RelationshipStatus;

public class FriendsAmountChain extends Chain {
    private long friendsAmount;

    public FriendsAmountChain(RelationshipStatus status, long friendsAmount) {
        this.certainChainStatus = status;
        this.friendsAmount = friendsAmount;

    }

    @Override
    public void check() throws BadRequestException {
        if (friendsAmount >= 100) throw new BadRequestException("Friends Limit");
    }
}
