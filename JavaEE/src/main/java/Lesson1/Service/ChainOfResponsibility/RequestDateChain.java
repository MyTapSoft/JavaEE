package Lesson1.Service.ChainOfResponsibility;

import Lesson1.Exceptions.BadRequestException;

import java.util.Date;

public class RequestDateChain extends Chain {

    private Date requestDate;

    public RequestDateChain(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public void check() throws BadRequestException {
        if (requestDate.getTime() <= new Date().getTime()) throw new BadRequestException("Friends Limit");
        checkNext();
    }
}
