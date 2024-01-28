import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

public class UserServiceTest {
    @Inject
    UserService userService;

    @InjectMock
    UserPersistence userPersistence;

    @BeforeEach
    void init() {
        Mockito.when(userPersistence.getUser(Mockito.any())).thenReturn((User) Arrays.asList(
                User.builder().username("julian.markus@gmail.com").build()
        ));
    }

    @Test
    public void addBicycleTest() {
        Mockito.when(userPersistence.saveUsers(Mockito.any())).thenAnswer(
                new Answer<User>() {
                    @Override
                    public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                        User user = invocationOnMock.getArgument(0);
                        user.setUsername("julia.meyr@lalala.com");
                        return user;
                    }
                }
        );

        User user = User.builder()
                .password("password1")
                .telephoneNumber("23-456-7890859")
                .salt(PasswordHasher.generateSalt())
                .build();

        userService.saveUser(user);
        Assertions.assertThat(user.getUsername()).isEqualTo("julia.meyr@lalala.com");
    }
}
