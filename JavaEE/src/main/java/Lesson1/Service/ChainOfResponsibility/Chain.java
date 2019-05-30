package Lesson1.Service.ChainOfResponsibility;

import Lesson1.Exceptions.BadRequestException;


public abstract class Chain {
    private Chain nextChain;

    public Chain setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
        return nextChain;
    }

    public void checkNext() throws BadRequestException {
        if (nextChain != null)
            nextChain.check();
    }

    public abstract void check() throws BadRequestException;
}
