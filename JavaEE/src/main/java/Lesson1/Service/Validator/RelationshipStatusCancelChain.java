package Lesson1.Service.Validator;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.RelationshipStatus;

public class RelationshipStatusCancelChain extends Chain {
    private RelationshipStatus relationshipStatus;

    public RelationshipStatusCancelChain(RelationshipStatus status, RelationshipStatus relationshipStatus) {
        this.certainChainStatus = status;
        this.relationshipStatus = relationshipStatus;
    }

    @Override
    public void check() throws BadRequestException {
        if (relationshipStatus != RelationshipStatus.pending)
            throw new BadRequestException("You don't have requests for canceling");
    }
}
