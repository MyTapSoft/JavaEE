package Lesson1.Service.Validator;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.RelationshipStatus;

public class RequestAmountChain extends Chain {
    private int requestAmount;

    public RequestAmountChain(RelationshipStatus status, int requestAmount) {
        this.certainChainStatus = status;
        this.requestAmount = requestAmount;
    }

    @Override
    public void check() throws BadRequestException {
        if (requestAmount >= 10) throw new BadRequestException("Request Amount Limit");
    }
}
