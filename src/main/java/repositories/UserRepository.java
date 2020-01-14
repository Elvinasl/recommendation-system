package repositories;


import models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository extends DatabaseRepository<User> {

    UserRepository() {
        super(User.class);
    }


    @Transactional
    public User findByExternalUserId(String externalUserId) {
        return em.createQuery("SELECT u FROM User u WHERE u.externalUserId = :externalUserId", User.class)
                .setParameter("externalUserId", externalUserId)
                .getSingleResult();
    }
}
