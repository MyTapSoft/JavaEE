package Lesson1.Service.ChainOfResponsibility;

import Lesson1.Exceptions.BadRequestException;



public abstract class Chain {
    private Chain nextChain;


    public void setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
    }

    public void checkNext() throws BadRequestException {
       check();
        if (nextChain != null)
            nextChain.check();
    }

    public abstract void check() throws BadRequestException;
}
