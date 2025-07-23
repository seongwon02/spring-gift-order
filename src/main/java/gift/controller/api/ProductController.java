package gift.controller.api;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.dto.product.option.OptionRequestDto;
import gift.dto.product.option.OptionResponseDto;
import gift.service.product.ProductService;
import gift.service.product.option.OptionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> findAllProducts() {
        List<ProductResponseDto> productList = productService.findAllProducts();

        return ResponseEntity.ok(productList);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> findProducts(
            @PageableDefault(size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        int pageNumber = Math.max(pageable.getPageNumber() - 1, 0);

        Pageable adjustedPageable = PageRequest.of(pageNumber, pageable.getPageSize(), pageable.getSort());

        Page<ProductResponseDto> productPage = productService.findProducts(adjustedPageable);
        return ResponseEntity.ok(productPage);
    }


    @GetMapping("{id}")
    public ResponseEntity<ProductResponseDto> findProduct(@PathVariable Long id) {

        ProductResponseDto dto = productService.findProductById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody ProductRequestDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED).
                body(productService.saveProduct(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @Valid @RequestBody ProductRequestDto dto,
            @PathVariable Long id) {

        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}/options")
    public ResponseEntity<List<OptionResponseDto>> findAllOptions(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(optionService.findAllOptionByProductId(id));
    }

    @PostMapping("{id}/options")
    public ResponseEntity<OptionResponseDto> createOption(
            @PathVariable Long id,
            @Valid @RequestBody OptionRequestDto dto
            ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(optionService.saveOption(id, dto));
    }

    @PutMapping("{productId}/options/{optionId}")
    public ResponseEntity<OptionResponseDto> updateOption(
            @PathVariable Long productId,
            @PathVariable Long optionId,
            @Valid @RequestBody OptionRequestDto dto
    ) {

        return ResponseEntity.ok(optionService.updateOption(optionId, dto));
    }

    @DeleteMapping("{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteOption(
            @PathVariable Long productId,
            @PathVariable Long optionId
    ) {

        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }

}
