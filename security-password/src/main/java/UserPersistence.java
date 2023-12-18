import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.Collection;

@ApplicationScoped
public class UserPersistence {
    @Inject
    EntityManager entityManager;
    @Transactional
    public void saveUsers(final User user) {
        entityManager.persist(user);
    }

    @Transactional
    public Collection<User> getAllUsers() {
        return entityManager.createQuery("select u from User u").getResultList();
    }

    @Transactional
    public User getUser(final String username) {
        return entityManager.find(User.class, username);
    }
}
