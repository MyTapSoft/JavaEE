package Lesson1.Service.ChainOfResponsibility;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.RelationshipStatus;

public class FriendsAmountChain extends Chain {
    private int friendsAmount;

    public FriendsAmountChain(RelationshipStatus status, int friendsAmount) {
        this.status = status;
        this.friendsAmount = friendsAmount;

    }

    @Override
    public void check() throws BadRequestException {
        if (friendsAmount >= 100) throw new BadRequestException("Friends Limit");
    }
}
