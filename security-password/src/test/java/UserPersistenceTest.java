import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class UserPersistenceTest {
    @Inject
    UserPersistence persistence;

    @Test
    public void test() {

    }
}
