package Lesson1.service.Validator;

import Lesson1.exceptions.BadRequestException;
import Lesson1.model.RelationshipStatus;

public class RelationshipStatusPendingChain extends Chain {
    private RelationshipStatus relationshipStatus;

    public RelationshipStatusPendingChain(RelationshipStatus status, RelationshipStatus relationshipStatus) {
        this.certainChainStatus = status;
        this.relationshipStatus = relationshipStatus;
    }

    @Override
    public void check() throws BadRequestException {
        if (relationshipStatus != RelationshipStatus.deleted && relationshipStatus != RelationshipStatus.canceled)
            throw new BadRequestException("You can't send request to this person");
    }
}
