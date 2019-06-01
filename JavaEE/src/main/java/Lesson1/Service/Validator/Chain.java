package Lesson1.Service.Validator;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.RelationshipStatus;


public abstract class Chain {
    private Chain nextChain;
    protected RelationshipStatus certainChainStatus;

    public Chain setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
        return nextChain;
    }


    public void validate(RelationshipStatus desiredStatus) throws BadRequestException {
        if (this.certainChainStatus == desiredStatus) check();
        if (nextChain != null)
            nextChain.validate(desiredStatus);
    }

    public abstract void check() throws BadRequestException;
}
