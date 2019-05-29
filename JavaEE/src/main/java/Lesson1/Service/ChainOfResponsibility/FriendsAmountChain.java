package Lesson1.Service.ChainOfResponsibility;

import Lesson1.Exceptions.BadRequestException;

public class FriendsAmountChain extends Chain {
    private int friendsAmount;

    public FriendsAmountChain(int friendsAmount) {
        this.friendsAmount = friendsAmount;
    }

    @Override
    public void check() throws BadRequestException {
        if (friendsAmount >= 100) throw new BadRequestException("Friends Limit");
        checkNext();
    }
}
