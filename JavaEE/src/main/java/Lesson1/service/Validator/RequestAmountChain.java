package Lesson1.service.Validator;

import Lesson1.exceptions.BadRequestException;
import Lesson1.model.RelationshipStatus;

public class RequestAmountChain extends Chain {
    private Long requestAmount;

    public RequestAmountChain(RelationshipStatus status, Long requestAmount) {
        this.certainChainStatus = status;
        this.requestAmount = requestAmount;
    }

    @Override
    public void check() throws BadRequestException {
        if (requestAmount >= 10) throw new BadRequestException("Request Amount Limit");
    }
}
