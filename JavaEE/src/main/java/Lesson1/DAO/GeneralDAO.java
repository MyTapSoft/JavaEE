package Lesson1.DAO;

import Lesson1.Model.IdEntity;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableTransactionManagement
public class GeneralDAO<T extends IdEntity> {

    @PersistenceContext
    private EntityManager entityManager;

    public T save(T var) {
        entityManager.persist(var);
        return var;
    }

    public void delete(long id, Class<?> tClass) {
        entityManager.remove(getEntity(id, tClass));
    }

    public T update(T var) {
        getEntity(var.getId(), var.getClass());
        return entityManager.merge(var);
    }

    public T getEntity(long id, Class<?> tClass) {
        return (T) entityManager.find(tClass, id);

    }

}