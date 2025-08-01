package gift.repository;

import gift.entity.MemberKakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberKakaoTokenRepository extends JpaRepository<MemberKakaoToken, Long> {
    Optional<MemberKakaoToken> findByMemberId(Long memberId);
}
