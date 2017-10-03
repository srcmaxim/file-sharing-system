package github.srcmaxim.filesharingsystem.repository;

import github.srcmaxim.filesharingsystem.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @Override
    VerificationToken save(VerificationToken entity);

    VerificationToken findByToken(String token);

}
