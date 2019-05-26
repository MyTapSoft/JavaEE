package Lesson1.DAO;

import Lesson1.Model.Relationship;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public class RelationshipDAO {

    @PersistenceContext
    private EntityManager entityManager;


    private static final String GET_RELATIONSHIP = "SELECT * \n" +
            " FROM RELATIONSHIP\n" +
            " WHERE (USER_ID_FROM = :idOne AND USER_ID_TO = :idTwo)\n" +
            "  OR (USER_ID_FROM = :idTwo AND USER_ID_TO = :idOne)";

    private static final String DELETE_RELATIONSHIP = "DELETE\n" +
            " FROM RELATIONSHIP\n" +
            " WHERE (USER_ID_FROM = :idOne AND USER_ID_TO = :idTwo)\n" +
            "   OR (USER_ID_FROM = :idTwo AND USER_ID_TO = :idOne)";


    public Relationship addRelationship(Relationship relationship) {
        entityManager.persist(relationship);
        return relationship;
    }

    public Relationship updateRelationship(Relationship relationship) {
        entityManager.merge(relationship);
        return relationship;
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

    public void deleteRelationship(long userOne, long userTwo) {
        entityManager.createNativeQuery(DELETE_RELATIONSHIP, Relationship.class)
                .setParameter("idOne", userOne)
                .setParameter("idTwo", userTwo)
                .executeUpdate();


    }
}
