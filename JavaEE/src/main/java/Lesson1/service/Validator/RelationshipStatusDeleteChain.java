package Lesson1.service.Validator;

import Lesson1.exceptions.BadRequestException;
import Lesson1.model.RelationshipStatus;


public class RelationshipStatusDeleteChain extends Chain {
    private RelationshipStatus relationshipStatus;

    public RelationshipStatusDeleteChain(RelationshipStatus relationshipStatus, RelationshipStatus status) {
        this.certainChainStatus = status;
        this.relationshipStatus = relationshipStatus;
    }

    @Override
    public void check() throws BadRequestException {
        if (relationshipStatus != RelationshipStatus.accepted) throw new BadRequestException("You can delete only your friend");
    }
}
