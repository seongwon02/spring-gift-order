package gift.service.product.option;

import gift.dto.product.option.OptionRequestDto;
import gift.dto.product.option.OptionResponseDto;

import java.util.List;

public interface OptionService {

    List<OptionResponseDto> findAllOptionByProductId(Long productId);
    OptionResponseDto findOptionById(Long optionId);
    OptionResponseDto saveOption(Long productId, OptionRequestDto dto);
    OptionResponseDto updateOption(Long optionId, OptionRequestDto dto);
    void deleteOption(Long optionId);
}
