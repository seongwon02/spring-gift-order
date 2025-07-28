package gift.service.order;

import gift.config.KakaoClient;
import gift.dto.order.OrderRequestDto;
import gift.dto.order.OrderResponseDto;
import gift.entity.*;
import gift.exception.product.option.OptionNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import gift.service.kakao.KakaoOAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final KakaoClient kakaoClient;
    private final KakaoOAuthService kakaoOAuthService;

    public OrderServiceImpl(OrderRepository orderRepository, OptionRepository optionRepository, WishRepository wishRepository, KakaoClient kakaoClient, KakaoOAuthService kakaoOAuthService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoOAuthService = kakaoOAuthService;
        this.kakaoClient = kakaoClient;
    }

    @Transactional
    @Override
    public OrderResponseDto createOrder(OrderRequestDto dto, Member member) {

        Option option = optionRepository.findById(dto.optionId())
                .orElseThrow(() -> new OptionNotFoundException("해당 ID의 옵션은 존재하지 않습니다."));

        option.substract(dto.quantity());

        Product product = option.getProduct();
        if (wishRepository.existsByMemberIdAndProductId(member.getId(), product.getId()))
            wishRepository.deleteByMemberIdAndProductId(member.getId(), product.getId());

        Order order = new Order(
                option,
                member,
                dto.quantity(),
                dto.message()
        );

        Order savedOrder = orderRepository.save(order);

        String kakaoToken = kakaoOAuthService.getValidAccessToken(member);
        kakaoClient.sendOrderToMe(kakaoToken, savedOrder);

        return new OrderResponseDto(
                savedOrder.getId(),
                savedOrder.getOption().getId(),
                savedOrder.getQuantity(),
                savedOrder.getOrderDateTime(),
                dto.message());
    }
}
