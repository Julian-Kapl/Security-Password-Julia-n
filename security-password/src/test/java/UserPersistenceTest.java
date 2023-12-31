import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;


@QuarkusTest
@Slf4j
public class UserPersistenceTest {
    @Inject
    UserPersistence persistence;

    @Test
    @TestTransaction
    @Order(0)
    public void testUserInsert() {
        User u1 = User.builder().username("flo.hagi06@gmail.com").password("password1").telephoneNumber("23-456-7890859").salt(PasswordHasher.generateSalt()).build();
        User u2 = User.builder()
                .username("andi.baggio@dav05.com")
                .password("password2")
                .telephoneNumber("87-654-3210091")
                .salt(PasswordHasher.generateSalt())
                .build();

        User u3 = User.builder()
                .username("m.bauer@marcus.com")
                .password("password3")
                .telephoneNumber("55-123-4567345")
                .salt(PasswordHasher.generateSalt())
                .build();

        User u4 = User.builder()
                .username("julia.meyr06@gmail.com")
                .password("julia123")
                .telephoneNumber("99-888-7777676")
                .salt(PasswordHasher.generateSalt())
                .build();

        User u5 = User.builder()
                .username("julian.kapl@liwest.at")
                .password("julian123")
                .telephoneNumber("11-222-3333803")
                .salt(PasswordHasher.generateSalt())
                .build();

        persistence.saveUsers(u1);
        persistence.saveUsers(u2);
        persistence.saveUsers(u3);
        persistence.saveUsers(u4);
        persistence.saveUsers(u5);

        Assert.assertTrue(persistence.getAllUsers().size() == 5);
    }

    @Test
    @TestTransaction
    @Order(1)
    public void getSingleUser() {
        testUserInsert();
        Assert.assertTrue(persistence.getAllUsers().size() == 5);
        User user = persistence.getUser("andi.baggio@dav05.com");
        Assert.assertNotNull(user);
    }

    @Test
    @TestTransaction
    public void changeUser() {
        testUserInsert();
        User user = persistence.getUser("andi.baggio@dav05.com");
        Assert.assertNotNull(user);
        persistence.saveUsers(user);
        User newUser = persistence.changeUser(user, "lala12");
        Assert.assertTrue(newUser.getPassword() == user.getPassword());
    }

    @Test
    @TestTransaction
    public void testValidateTelephoneNumberValid() {
        User user = new User();
        user.setTelephoneNumber("12-456-7890123");

        Assert.assertTrue(persistence.validateTelephoneNumber(user));
    }

    @Test
    @TestTransaction
    public void testValidateTelephoneNumberInvalid() {
        User user = new User();
        user.setTelephoneNumber("invalidNumber");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> persistence.validateTelephoneNumber(user));

        Assert.assertTrue("TelephoneNumber ist ungültig".equals(exception.getMessage()));
    }

    @Test
    @TestTransaction
    public void testValidateUsernameValid() {
        User user = new User();
        user.setUsername("valid.email@example.com");

        Assert.assertTrue(persistence.validateUsername(user));
    }

    @Test
    @TestTransaction
    public void testValidateUsernameInvalid() {
        User user = new User();
        user.setUsername("invalid-email");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> persistence.validateUsername(user));

        Assert.assertTrue("Username ist ungültig".equals(exception.getMessage()));
    }

    @Test
    @TestTransaction
    public void testValidatePasswordValid() {
        Assert.assertTrue(persistence.validatePassword("valid123"));
    }

    @Test
    @TestTransaction
    public void testValidatePasswordInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> persistence.validatePassword("short"));

        Assert.assertTrue("Password ist ungültig".equals(exception.getMessage()));
    }
}
