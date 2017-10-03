package github.srcmaxim.filesharingsystem.model;

import github.srcmaxim.filesharingsystem.util.PasswordGenerator;
import github.srcmaxim.filesharingsystem.util.PasswordGenerator.PasswordGeneratorBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@ToString(exclude = "token")
@EqualsAndHashCode(of = {"token", "user"})
@Entity(name = "token")
public class VerificationToken {

    private static final int EXPIRATION_DAYS = 1;
    private static final int TOKEN_LENGTH = 16;
    private static final PasswordGenerator GENERATOR = new PasswordGeneratorBuilder()
            .useLower(true)
            .useUpper(true)
            .useDigits(true)
            .build();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final String token = GENERATOR.generate(TOKEN_LENGTH);

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private final User user;

    /**
     * Global representation of UTC time when User
     * might verify token (if UTC.now() < expiryDate).
     */
    private final Instant expiryDate = calculateExpiryDate();

    public VerificationToken() {
        this(null);
    }

    public VerificationToken(User user) {
        this.user = user;
    }

    private Instant calculateExpiryDate() {
        ZonedDateTime utc = ZonedDateTime.now(ZoneId.of("UTC"));
        return utc.plusDays(EXPIRATION_DAYS).toInstant();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

}

