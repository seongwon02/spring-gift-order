package gift.repository;

import gift.entity.MemberKakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberKakaoTokenRepository extends JpaRepository<MemberKakaoToken, Long> {
}
