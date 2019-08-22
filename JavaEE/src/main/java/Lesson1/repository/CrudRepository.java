package Lesson1.repository;

import Lesson1.model.IdEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public class CrudRepository<T extends IdEntity> {

    @PersistenceContext
    private EntityManager entityManager;

    public T save(T var) {
        entityManager.persist(var);
        return var;
    }

    public void delete(Long id, Class<?> tClass) {
        entityManager.remove(findById(id, tClass));
    }

    public T update(T var) {
        findById(var.getId(), var.getClass());
        return entityManager.merge(var);
    }

    public T findById(Long id, Class<?> tClass) {
        return (T) entityManager.find(tClass, id);

    }

    public T find(Class<?> tClass, T object){
        return (T)entityManager.find(tClass, object);
    }

}