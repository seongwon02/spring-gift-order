package gift.repository;

import gift.entity.Member;
import gift.entity.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 레포지토리에 멤버가 정상적으로 저장되는지 테스트")
    void save() {
        Member expectedMember = memberRepository.save(
                new Member(
                        "test@email.com",
                        "0000",
                        RoleType.USER
                )
        );

        Member actualMember = memberRepository.save(expectedMember);

        assertAll(
                () -> assertThat(actualMember.getId()).isNotNull(),
                () -> assertThat(actualMember.getEmail()).isEqualTo(expectedMember.getEmail()),
                () -> assertThat(actualMember.getPassword()).isEqualTo(expectedMember.getPassword()),
                () -> assertThat(actualMember.getRole()).isEqualTo(expectedMember.getRole())
        );
    }

    @Test
    @DisplayName("멤버 레포지토리에 멤버가 이메일을 통해 정상적으로 조회되는지 테스트")
    void find() {

        Member expectedMember = memberRepository.save(
                new Member (
                    "test@email.com",
                    "0000",
                        RoleType.USER
                )
        );

        Member actualMember = memberRepository.findByEmail(expectedMember.getEmail()).get();

        assertAll(
                () -> assertThat(actualMember.getId()).isNotNull(),
                () -> assertThat(actualMember.getEmail()).isEqualTo(expectedMember.getEmail()),
                () -> assertThat(actualMember.getPassword()).isEqualTo(expectedMember.getPassword()),
                () -> assertThat(actualMember.getRole()).isEqualTo(expectedMember.getRole())
        );
    }

    @Test
    @DisplayName("멤버 레포지토리에 멤버가 정상적으로 수정되는지 테스트")
    void update() {
        Member expectedMember = memberRepository.save(
                new Member(
                        "test@email.com",
                        "0000",
                        RoleType.USER
                )
        );

        expectedMember.setRole(RoleType.ADMIN);

        Member actualMember = memberRepository.findByEmail(expectedMember.getEmail()).get();

        assertAll(
                () -> assertThat(actualMember.getId()).isNotNull(),
                () -> assertThat(actualMember.getEmail()).isEqualTo(expectedMember.getEmail()),
                () -> assertThat(actualMember.getPassword()).isEqualTo(expectedMember.getPassword()),
                () -> assertThat(actualMember.getRole()).isEqualTo(expectedMember.getRole())
        );
    }

    @Test
    @DisplayName("멤버 레포지토리에 멤버가 정상적으로 삭제되는지 테스트")
    void delete() {
        Member expectedMember = memberRepository.save(
                new Member(
                        "test@email.com",
                        "0000",
                        RoleType.USER
                )
        );

        Long id = expectedMember.getId();
        memberRepository.deleteById(id);

        assertThat(memberRepository.existsById(id)).isEqualTo(false);
    }
}
