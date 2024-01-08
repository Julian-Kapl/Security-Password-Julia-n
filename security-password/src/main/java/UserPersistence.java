import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class UserPersistence {
    @Inject
    EntityManager entityManager;

    @Transactional
    public void saveUsers(final User user) {
        try{
            if(validateUsername(user) && validateTelephoneNumber(user) && validatePassword(user.getPassword())){
                String salt = PasswordHasher.generateSalt();
                String hashedPassword = PasswordHasher.hashPassword(user.getPassword(), salt);
                user.setPassword(hashedPassword);
                user.setSalt(salt);
                entityManager.persist(user);
            }
        }catch(Exception ex){
            log.warn(ex.getMessage());
        }
    }

    @Transactional
    public Collection<User> getAllUsers() {
        return entityManager.createQuery(User.QUERY_FIND_ALL).getResultList();
    }

    @Transactional
    public User getUser(final String username) {
        return entityManager.find(User.class, username);
    }

    @Transactional
    public User changeUser(final User user,final String password){
        try{
            if(validatePassword(password)){
                String hashedPassword = PasswordHasher.hashPassword(user.getPassword(), user.getSalt());
                user.setPassword(hashedPassword);

                final String username = user.getUsername();
                User persistUser = entityManager.find(User.class, username);
                if(persistUser != null){
                    return entityManager.merge(user);
                }
                throw new RuntimeException("User with username" + username + "not found");
            }
        }catch (Exception ex){
            log.warn(ex.getMessage());
        }
        return null;
    }
    public boolean validateTelephoneNumber(User user) {
        Pattern pattern = Pattern.compile("^+?([0-9]{2})?-?([0-9]{3})?-?[0-9]{7,8}$");
        Matcher matcher = pattern.matcher(user.getTelephoneNumber());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("TelephoneNumber ist ungültig");
        }
        return pattern.matcher(user.getTelephoneNumber()).find();
    }

    public boolean validateUsername(User user) {
        Pattern pattern = Pattern.compile("^([\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4})?$");
        Matcher matcher = pattern.matcher(user.getUsername());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Username ist ungültig");
        }
        return pattern.matcher(user.getUsername()).find();
    }

    public boolean validatePassword(final String password) {
        Pattern pattern = Pattern.compile("^([a-zA-Z]|[0-9]){6,}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Password ist ungültig");
        }
        return pattern.matcher(password).find();
    }
}
