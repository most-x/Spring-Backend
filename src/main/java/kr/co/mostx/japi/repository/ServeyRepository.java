package kr.co.mostx.japi.repository;

import kr.co.mostx.japi.entity.Servey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServeyRepository extends JpaRepository<Servey, Long>, ServeyCustomRepository {
    Optional<Servey> findByServeyNumber(String serveyNumber);
}
