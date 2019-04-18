package Lesson1.DAO;

import Lesson1.Model.IdEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class GeneralDAO<T extends IdEntity> {

    @PersistenceContext
    private EntityManager entityManager;

    public T save(T var) {
        return entityManager.merge(var);
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