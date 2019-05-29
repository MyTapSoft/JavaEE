package Lesson1.Service.ChainOfResponsibility;

import Lesson1.Exceptions.BadRequestException;

public class RequestAmountChain extends Chain {

    private int requestAmount;

    public RequestAmountChain(int requestAmount) {
        this.requestAmount = requestAmount;
    }

    @Override
    public void check() throws BadRequestException {
        if (requestAmount >= 10) throw new BadRequestException("Request Amount Limit");
        checkNext();
    }
}
