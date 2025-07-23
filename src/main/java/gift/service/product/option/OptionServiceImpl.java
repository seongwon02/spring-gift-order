package gift.service.product.option;

import gift.dto.product.option.OptionRequestDto;
import gift.dto.product.option.OptionResponseDto;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.product.ProductNotFoundException;
import gift.exception.product.option.DuplicatedOptionNameException;
import gift.exception.product.option.OptionNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionServiceImpl implements OptionService{

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;


    public OptionServiceImpl(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<OptionResponseDto> findAllOptionByProductId(Long productId) {

        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("해당 ID의 상품은 존재하지 않습니다.");
        }

        List<Option> options = optionRepository.findAllByProductId(productId);

        return options.stream()
                .map(option -> new OptionResponseDto(
                        option.getId(),
                        option.getName(),
                        option.getQuantity()
                ))
                .toList();
    }

    @Override
    public OptionResponseDto findOptionById(Long optionId) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException(
                        "해당 ID의 옵션은 존재하지 않습니다."
                ));

        return new OptionResponseDto(option.getId(), option.getName(), option.getQuantity());
    }

    @Transactional
    @Override
    public OptionResponseDto saveOption(Long productId, OptionRequestDto dto) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        "해당 ID의 상품은 존재하지 않습니다."
                ));

        if (optionRepository.existsByProductIdAndName(productId, dto.getName())) {
            throw new DuplicatedOptionNameException(
                    "해당 상품에 동일한 옵션이 존재합니다."
            );
        }

        Option option = new Option(dto.getName(), dto.getQuantity(), product);
        Option savedOption = optionRepository.save(option);

        return new OptionResponseDto(savedOption.getId(), savedOption.getName(), savedOption.getQuantity());
    }

    @Transactional
    @Override
    public OptionResponseDto updateOption(Long optionId, OptionRequestDto dto) {

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException(
                        "해당 ID의 옵션은 존재하지 않습니다."
                ));

        option.update(dto.getName(), dto.getQuantity());

        return new OptionResponseDto(option.getId(), option.getName(), option.getQuantity());
    }

    @Transactional
    @Override
    public void deleteOption(Long optionId) {

        if (!optionRepository.existsById(optionId)) {
            throw new OptionNotFoundException(
                    "해당 ID의 옵션은 존재하지 않습니다."
            );
        }

        optionRepository.deleteById(optionId);
    }
}
