package in.nikhilsaini.authify.repository;


import in.nikhilsaini.authify.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempUserRepository extends JpaRepository<TempUser, String> {
}
