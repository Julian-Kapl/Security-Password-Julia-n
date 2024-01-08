import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "user")

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

@NamedQuery(
        name = User.QUERY_FIND_ALL,
        query = "select u from User u"
)

public class User {
    public static final String QUERY_FIND_ALL = "find_all";

    @Id
    private String username;

    @Length(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String telephoneNumber;

    private String salt;
}
