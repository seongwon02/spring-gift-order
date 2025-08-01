package gift.service.product;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.entity.Product;
import gift.exception.product.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public ProductServiceImpl(ProductRepository productRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    public List<ProductResponseDto> findAllProducts(){

        List<Product> findList = productRepository.findAll();

        List<ProductResponseDto> dtoList = findList
                .stream()
                .map(x -> new ProductResponseDto(
                        x.getId(),
                        x.getName(),
                        x.getPrice(),
                        x.getImageUrl(),
                        x.getApproved(),
                        x.getDescription()
                        ))
                .toList();

        return dtoList;
    }

    @Override
    public Page<ProductResponseDto> findProducts(Pageable pageable) {

        Page<Product> products = productRepository.findAll(pageable);

        Page<ProductResponseDto> page = products
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageUrl(),
                        product.getApproved(),
                        product.getDescription()
                        )
                );

        return page;
    }

    @Override
    public ProductResponseDto findProductById(Long id) {

        return productRepository.findById(id)
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageUrl(),
                        product.getApproved(),
                        product.getDescription()))
                .orElse(null);
    }

    @Override
    public ProductResponseDto findProductByIdElseThrow(Long id) {
        return productRepository.findById(id)
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageUrl(),
                        product.getApproved(),
                        product.getDescription()))
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "해당 ID의 상품을 찾을 수 없습니다."
                        )
                );
    }

    @Transactional
    @Override
    public ProductResponseDto saveProduct(ProductRequestDto dto) {

        String name = dto.getName();
        Long price = dto.getPrice();
        String imageUrl = dto.getImageUrl();
        boolean approved = !name.contains("카카오");
        String description = "";

        if (!approved)
            description = "카카오 문구가 담긴 상품은 담당 MD와 협의 후 사용가능합니다.";

        Product newProduct = new Product(name, price, imageUrl, approved, description);
        Product savedProduct = productRepository.save(newProduct);

        return new ProductResponseDto(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getImageUrl(),
                savedProduct.getApproved(),
                savedProduct.getDescription());
    }

    @Transactional
    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {

        boolean approved = !dto.getName().contains("카카오");
        String description = "";

        if (!approved)
            description = "카카오 문구가 담긴 상품은 담당 MD와 협의 후 사용가능합니다.";

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "해당 ID의 상품은 존재하지 않습니다."
                ));

        product.updateProduct(
                dto.getName(),
                dto.getPrice(),
                dto.getImageUrl(),
                approved,
                description
        );

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getApproved(),
                product.getDescription()
        );
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(
                    "해당 ID의 상품은 존재하지 않습니다."
            );
        }

        optionRepository.deleteAllByProductId(id);
        productRepository.deleteById(id);
    }
}
