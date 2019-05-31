package Lesson1.Service.Validator;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.RelationshipStatus;

import java.util.Date;

public class RequestDateChain extends Chain {
    private Date requestDate;

    public RequestDateChain(RelationshipStatus status, Date requestDate) {
        this.certainChainStatus = status;
        this.requestDate = requestDate;
    }

    @Override
    public void check() throws BadRequestException {
        if (requestDate.getTime() <= new Date().getTime()) throw new BadRequestException("Date Limit");
    }
}
