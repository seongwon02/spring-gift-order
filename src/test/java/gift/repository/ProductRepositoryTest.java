package gift.repository;

import gift.entity.Product;
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
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 레퍼지토리에 상품이 정상적으로 저장되는지 테스트")
    void save() {
        Product expectedProduct = new Product(
                "테스트",
                10000L,
                "https://test.com",
                true,
                ""
        );

        Product actualProduct = productRepository.save(expectedProduct);

        assertAll(
                () -> assertThat(actualProduct.getId()).isNotNull(),
                () -> assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName()),
                () -> assertThat(actualProduct.getPrice()).isEqualTo(expectedProduct.getPrice()),
                () -> assertThat(actualProduct.getImageUrl()).isEqualTo(expectedProduct.getImageUrl()),
                () -> assertThat(actualProduct.getApproved()).isEqualTo(expectedProduct.getApproved()),
                () -> assertThat(actualProduct.getDescription()).isEqualTo(expectedProduct.getDescription())
        );
    }

    @Test
    @DisplayName("상품 레퍼지토리에 상품이 정상적으로 조회되는지 테스트")
    void find() {
        Product expectedProduct = productRepository.save(
                new Product(
                        "테스트",
                        10000L,
                        "https://test.com",
                        true,
                        ""
                ));

        Product actualProduct = productRepository.findById(expectedProduct.getId()).get();

        assertAll(
                () -> assertThat(actualProduct.getId()).isNotNull(),
                () -> assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName()),
                () -> assertThat(actualProduct.getPrice()).isEqualTo(expectedProduct.getPrice()),
                () -> assertThat(actualProduct.getImageUrl()).isEqualTo(expectedProduct.getImageUrl()),
                () -> assertThat(actualProduct.getApproved()).isEqualTo(expectedProduct.getApproved()),
                () -> assertThat(actualProduct.getDescription()).isEqualTo(expectedProduct.getDescription())
        );
    }

    @Test
    @DisplayName("상품 레퍼지토리에 상품들이 페이지네이션을 통해 정상적으로 조회되는지 테스트")
    void findAll() {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Product> products = productRepository.findAll(pageable);

        assertAll(
                () -> assertThat(products).isNotNull(),
                () -> assertThat(products.getContent().size()).isEqualTo(4),
                () -> assertThat(products.getTotalElements()).isEqualTo(6L),
                () -> assertThat(products.getTotalPages()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("상품 레퍼지토리에 상품이 정상적으로 수정되는지 테스트")
    void update() {
        Product expectedProduct = productRepository.save(
                new Product(
                        "테스트",
                        10000L,
                        "https://test.com",
                        true,
                        ""
                ));

        expectedProduct.updateProduct(
                "수정됨",
                20000L,
                "https://test2.com",
                true,
                ""
        );

        Product actualProduct = productRepository.findById(expectedProduct.getId()).get();

        assertAll(
                () -> assertThat(actualProduct.getId()).isNotNull(),
                () -> assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName()),
                () -> assertThat(actualProduct.getPrice()).isEqualTo(expectedProduct.getPrice()),
                () -> assertThat(actualProduct.getImageUrl()).isEqualTo(expectedProduct.getImageUrl()),
                () -> assertThat(actualProduct.getApproved()).isEqualTo(expectedProduct.getApproved()),
                () -> assertThat(actualProduct.getDescription()).isEqualTo(expectedProduct.getDescription())
        );
    }

    @Test
    @DisplayName("상품 레퍼지토리에 상품이 정상적으로 삭제되는지 테스트")
    void delete() {
        Product expectedProduct = productRepository.save(
                new Product(
                        "테스트",
                        10000L,
                        "https://test.com",
                        true,
                        ""
                ));

        Long id = expectedProduct.getId();
        productRepository.deleteById(id);

        assertThat(productRepository.existsById(id)).isEqualTo(false);
    }
}