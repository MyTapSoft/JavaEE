package Lesson1.Service.Validator;

import Lesson1.Exceptions.BadRequestException;
import Lesson1.Model.RelationshipStatus;


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
