package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.RoleType;
import gift.entity.Wish;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("위시리스트에 상품이 정상적으로 저장되는지 테스트")
    void save() {

        Member expectedMember = memberRepository.save(
                new Member(
                        "test@example.com",
                        "0000",
                        RoleType.USER));

        Product expectedProduct = productRepository.save(
                new Product(
                        "테스트",
                        10000L,
                        "https://test.com",
                        true,
                        ""
                ));

        Wish expectedWish = new Wish(expectedMember, expectedProduct);

        Wish actualWish = wishRepository.save(expectedWish);

        assertAll(
                () -> assertThat(actualWish.getId()).isNotNull(),
                () ->assertThat(actualWish.getMember()).isEqualTo(expectedMember),
                () -> assertThat(actualWish.getProduct()).isEqualTo(expectedProduct)
        );
    }

    @Test
    @DisplayName("위시리스트에 상품이 정상적으로 조회되는지 테스트")
    void find() {

        Member expectedMember = memberRepository.save(
                new Member(
                        "test@example.com",
                        "0000",
                        RoleType.USER));

        Product expectedProduct = productRepository.save(
                new Product(
                        "테스트",
                        10000L,
                        "https://test.com",
                        true,
                        ""
                ));

        Wish expectedWish = new Wish(expectedMember, expectedProduct);

        Long id = wishRepository.save(expectedWish).getId();
        Wish actualWish = wishRepository.findById(id).get();

        assertAll(
                () -> assertThat(actualWish.getId()).isNotNull(),
                () -> assertThat(actualWish.getMember()).isEqualTo(expectedMember),
                () -> assertThat(actualWish.getProduct()).isEqualTo(expectedProduct)
        );
    }

    @DisplayName("페이지네이션을 통한 위시 조회가 정상적으로 이뤄지는지 테스트")
    @Test
    void findAllByMemberId() {

        String email = "user@example.com";
        Member member = memberRepository.findByEmail(email).get();

        Pageable pageable = PageRequest.of(0, 4);
        Page<Wish> wishes = wishRepository.findAllByMemberId(member.getId(), pageable);

        assertAll(
                () -> assertThat(wishes).isNotNull(),
                () -> assertThat(wishes.getContent().size()).isEqualTo(4),
                () -> assertThat(wishes.getTotalElements()).isEqualTo(5L),
                () -> assertThat(wishes.getTotalPages()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("위시리스트에 상품이 정상적으로 삭제되는지 테스트")
    void delete() {
        boolean expectedResult = false;

        Member member = memberRepository.save(
                new Member(
                        "test@example.com",
                        "0000",
                        RoleType.USER));

        Product product = productRepository.save(
                new Product(
                        "테스트",
                        10000L,
                        "https://test.com",
                        true,
                        ""
                ));

        Wish wish = new Wish(member, product);
        Long id = wishRepository.save(wish).getId();
        wishRepository.deleteById(id);

        boolean actualResult = wishRepository.existsByMemberIdAndProductId(member.getId(), product.getId());

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
