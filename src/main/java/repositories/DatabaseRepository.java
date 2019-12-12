package repositories;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

abstract class DatabaseRepository<T> {

    private final Class<T> type;

    @PersistenceContext(unitName = "entityManagerFactory")
    protected EntityManager em;

    DatabaseRepository(Class<T> type) {
        this.type = type;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void add(T o) {
        em.persist(o);
    }

    @Transactional
    public void update(T o) {
        em.merge(o);
    }

    @Transactional
    public T getById(int id) {
        return em.find(type, id);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<T> list() {
        return em.createQuery("select o from " + type.getName() + " o").getResultList();
    }

    @Transactional
    public void remove(int id) {
        T o = this.getById(id);
        if (null != o) {
            em.remove(o);
        }
    }

}
