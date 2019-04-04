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

    public T save(T var) throws Exception {
        try {
            entityManager.merge(var);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Cant save Entity");
        }
        return var;
    }

    public void delete(long id, Class<?> tClass) throws Exception {
        try {
            entityManager.remove(getEntity(id, tClass));
        } catch (Exception e) {
            throw new Exception("Cant delete: Entity " + id);
        }
    }

    public T update(T var) throws Exception {
        getEntity(var.getId(), var.getClass());
        try {
            entityManager.merge(var);
        } catch (Exception e) {
            throw new Exception("Cant update Entity");
        }
        return var;
    }

    public T getEntity(long id, Class<?> tClass) throws Exception {
        try {
            T result = (T) entityManager.find(tClass, id);
            if (result == null) throw new EntityExistsException("Entity doesn't exist: " + id);
            return result;
        } catch (Exception e) {
            throw new Exception("Something goes wrong");
        }
    }
}