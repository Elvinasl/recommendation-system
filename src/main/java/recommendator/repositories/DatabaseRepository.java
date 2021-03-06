package recommendator.repositories;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Defines the basic operations for each repository (database related)
 *
 * @param <T> Object type of the repository
 */
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
    public T add(T o) {
        em.persist(o);
        return o;
    }

    @Transactional
    public T update(T o) {
        em.merge(o);
        return o;
    }

    @Transactional
    public T getById(long id) {
        return em.find(type, id);
    }

    @Transactional
    public boolean exists(T o) {
        return count(o) > 0;
    }

    @Transactional
    public long count(T o) {
        return (long) em.createQuery(
                "select count(o) from :obj o")
                .setParameter("obj", type.getName())
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<T> list() {
        return em.createQuery("select o from " + type.getName() + " o").getResultList();
    }

    @Transactional
    public void remove(long id) {
        T o = this.getById(id);
        if (null != o) {
            em.remove(o);
        }
    }

    @Transactional
    public void flush() {
        em.flush();
    }

    @Transactional
    public T getSingleResultOrNull(Query query) {
        List results = query.getResultList();
        if (results.isEmpty()) return null;
        else if (results.size() == 1) return (T) results.get(0);
        throw new NonUniqueResultException();
    }

    @Transactional
    public void deleteAll() {
        em.createQuery("DELETE FROM  " + type.getName())
                .executeUpdate();
    }
}
