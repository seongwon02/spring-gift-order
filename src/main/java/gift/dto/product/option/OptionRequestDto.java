package gift.dto.product.option;

import jakarta.validation.constraints.*;

public class OptionRequestDto {

    @NotBlank(message = "옵션명은 필수입니다.")
    @Size(max = 50, message = "옵션명은 최대 50자를 가질 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]*$",
            message = "상품명에 허용하지 않는 특수문자가 포함되어 있습니다. 허용하는 특수문자: (), [], +, -, &, /, _")
    private final String name;

    @NotNull(message = "상품 수량은 필수입니다.")
    @Min(value = 1, message = "상품 수량은 최소 1개 이상입니다.")
    @Max(value = 99999999, message = "상품 수량은 1억개 미만입니다.")
    private final Long quantity;

    public OptionRequestDto(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

}
