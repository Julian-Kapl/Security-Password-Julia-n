import io.quarkus.test.InjectMock;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.gradle.internal.impldep.com.google.common.base.Predicates.equalTo;

public class UserResourceTest {

    @InjectMock
    UserService userService;

    @Test
    public void findExistingBikeByUUIDTest() {
        Mockito.when(userService.getUser("flo.hagi06@gmail.com"))
                .thenReturn(User.builder()
                        .username("flo.hagi06@gmail.com")
                        .password("password1")
                        .telephoneNumber("23-456-7890859")
                        .salt(PasswordHasher.generateSalt())
                        .build());

        given()
                .when().get("/api/v1/users/flo.hagi06@gmail.com")
                .then()
                .statusCode(200)
                .body("username", (ResponseAwareMatcher<Response>) equalTo("flo.hagi06@gmail.com"))
                .body("password", (ResponseAwareMatcher<Response>) equalTo("password1"))
                .body("telephone", (ResponseAwareMatcher<Response>) equalTo("23-456-7890859"));
    }

}
