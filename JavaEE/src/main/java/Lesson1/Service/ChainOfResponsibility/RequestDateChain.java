package Lesson1.Service.ChainOfResponsibility;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.RelationshipStatus;

import java.util.Date;

public class RequestDateChain extends Chain {
    private Date requestDate;

    public RequestDateChain(RelationshipStatus status, Date requestDate) {
        this.status = status;
        this.requestDate = requestDate;
    }

    @Override
    public void check() throws BadRequestException {
        if (requestDate.getTime() <= new Date().getTime()) throw new BadRequestException("Date Limit");
    }
}
