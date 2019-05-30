package Lesson1.Service.ChainOfResponsibility;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.RelationshipStatus;


public abstract class Chain {
    private Chain nextChain;
    protected RelationshipStatus status;

    public Chain setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
        return nextChain;
    }


    public void start(RelationshipStatus status) throws BadRequestException {
        if (this.status == status) check();
        if (nextChain != null)
            nextChain.start(status);
    }

    public abstract void check() throws BadRequestException;
}
