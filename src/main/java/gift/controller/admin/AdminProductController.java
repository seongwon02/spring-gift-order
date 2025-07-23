package gift.controller.admin;

import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.dto.product.option.OptionRequestDto;
import gift.dto.product.option.OptionResponseDto;
import gift.service.product.ProductService;
import gift.service.product.option.OptionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public AdminProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    // 상품 추가 페이지로 이동
    @GetMapping("/new")
    public String goNewProductForm() {
        return "admin/newProductForm";
    }

    // 상품 수정 페이지로 이동
    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {

        ProductResponseDto product = productService.findProductById(id);
        model.addAttribute("product", product);

        return "admin/editProductForm";
    }

    // 옵션 추가 페이지로 이동
    @GetMapping("{productId}/options/new")
    public String goNewOptionForm(@PathVariable Long productId, Model model) {

        ProductResponseDto product = productService.findProductById(productId);
        model.addAttribute("product", product);
        return "admin/newOptionForm";
    }

    // 옵션 수정 페이지로 이동
    @GetMapping("{productId}/options/{optionId}/edit")
    public String editOptionForm(
            @PathVariable Long productId,
            @PathVariable Long optionId,
            Model model) {

        ProductResponseDto product = productService.findProductById(productId);
        OptionResponseDto option = optionService.findOptionById(optionId);

        model.addAttribute("product", product);
        model.addAttribute("option", option);
        return "admin/editOptionForm";
    }

    // 상품 목록 조회 기능
    @GetMapping
    public String findProducts(
            @RequestParam(value = "id", required = false) Long id,
            Model model)
    {
        if (id == null) {
            List<ProductResponseDto> dtoList = productService.findAllProducts();
            model.addAttribute("products", dtoList);

            return "admin/products";
        }
        
        ProductResponseDto dto = productService.findProductById(id);

        if (dto != null)
            model.addAttribute("products", List.of(dto));
        else
            model.addAttribute("products", Collections.emptyList());

        return "admin/products";
    }

    // 상품 추가 기능 구현
    @PostMapping
    public String saveProduct(@Valid @ModelAttribute ProductRequestDto dto) {

        productService.saveProduct(dto);
        return "redirect:/admin/products";
    }

    // 상품 수정 기능 구현
    @PutMapping("{id}")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductRequestDto dto) {

        productService.updateProduct(id, dto);
        return "redirect:/admin/products";
    }

    // 상품 삭제  기능 구현
    @DeleteMapping("{id}")
    public String deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("{id}/options")
    public String findOptions(@PathVariable Long id, Model model) {

        ProductResponseDto product = productService.findProductById(id);
        List<OptionResponseDto> options = optionService.findAllOptionByProductId(id);

        model.addAttribute("product", product);
        model.addAttribute("options", options);

        return "admin/options";
    }

    @PostMapping("{id}/options")
    public String saveOption(
            @PathVariable Long id,
            @Valid @ModelAttribute OptionRequestDto dto) {

        optionService.saveOption(id, dto);
        return "redirect:/admin/products/" + id + "/options";
    }

    @PutMapping("{productId}/options/{optionId}")
    public String updateOption(
            @PathVariable Long productId,
            @PathVariable Long optionId,
            @Valid @ModelAttribute OptionRequestDto dto) {

        optionService.updateOption(optionId, dto);
        return "redirect:/admin/products/" + productId + "/options";
    }

    @DeleteMapping("{productId}/options/{optionId}")
    public String deleteOption(
            @PathVariable Long productId,
            @PathVariable Long optionId) {

        optionService.deleteOption(optionId);
        return "redirect:/admin/products/" + productId + "/options";
    }
}
