package Lesson1.DAO;

import Lesson1.Model.Relationship;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RelationshipDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String INCOME_USER_REQUESTS = "SELECT * FROM RELATIONSHIP " +
            "WHERE USER_ID_TO = :id";
    private static final String OUTCOME_USER_REQUESTS = "SELECT * FROM RELATIONSHIP " +
            "WHERE USER_ID_FROM = :id";
    private static final String GET_RELATIONSHIP = "SELECT * FROM RELATIONSHIP WHERE USER_ID_FROM = :idOne " +
            "OR USER_ID_FROM = :idTwo AND USER_ID_TO = :idOne OR USER_ID_TO = :idTwo";


    public Relationship addRelationship(Relationship relationship) {
        try {
            entityManager.persist(relationship);
        } catch (Exception e) {
            System.out.println(e);
        }
        //Не работает!
        System.out.println(relationship);
        return relationship;
    }

    public Relationship updateRelationship(Relationship relationship) {
        relationship = getRelationship(relationship.getUserIdFrom(), relationship.getUserIdTo());
        entityManager.merge(relationship);
        return relationship;
    }

    public List<Relationship> getIncomeRequests(String userId) {
        return entityManager.createNativeQuery(INCOME_USER_REQUESTS, Relationship.class)
                .setParameter("id", userId)
                .getResultList();
    }

    public List<Relationship> getOutcomeRequests(String userId) {
        return entityManager.createNativeQuery(OUTCOME_USER_REQUESTS, Relationship.class)
                .setParameter("id", userId)
                .getResultList();
    }

    public Relationship getRelationship(long userOne, long userTwo) {
        try {
            return (Relationship) entityManager.createNativeQuery(GET_RELATIONSHIP, Relationship.class)
                    .setParameter("idOne", userOne)
                    .setParameter("idTwo", userTwo)
                    .getSingleResult();
        } catch (NoResultException noResultExc) {
            return null;
        }

    }

}
