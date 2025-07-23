package gift.dto.product;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

public class ProductRequestDto {


    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 15, message = "상품명은 최대 15자를 가질 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]*$",
            message = "상품명에 허용하지 않는 특수문자가 포함되어 있습니다. 허용하는 특수문자: (), [], +, -, &, /, _")
    private final String name;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0원 이상입니다.")
    private final Long price;

    @NotBlank(message = "이미지 URL은 필수입니다.")
    @URL(message = "유효한 이미지 URL 형식이 아닙니다.")
    private final String imageUrl;

    public ProductRequestDto(String name, Long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
