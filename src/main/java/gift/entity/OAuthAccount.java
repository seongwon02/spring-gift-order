package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "oauth_account")
public class OAuthAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id", nullable = false, unique = true)
    private String kakaoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected OAuthAccount() {}

    public OAuthAccount(String kakaoId, Member member) {
        this.kakaoId = kakaoId;
        this.member = member;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public Member getMember() {
        return member;
    }
}
