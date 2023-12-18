import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;

@ApplicationScoped
public class UserService {
    @Inject
    private UserPersistence persistence;

    public Collection<User> getAllUsers() {
        return persistence.getAllUsers();
    }

    public User getUser(final String username) {
        return persistence.getUser(username);
    }
}
