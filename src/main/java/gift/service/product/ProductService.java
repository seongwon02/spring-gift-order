package gift.service.product;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> findAllProducts();
    Page<ProductResponseDto> findProducts(Pageable pageable);
    ProductResponseDto findProductById(Long id);
    ProductResponseDto findProductByIdElseThrow(Long id);
    ProductResponseDto saveProduct(ProductRequestDto dto);
    ProductResponseDto updateProduct(Long id, ProductRequestDto dto);
    void deleteProduct(Long id);
}
