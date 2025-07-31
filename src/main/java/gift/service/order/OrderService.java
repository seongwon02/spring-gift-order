package gift.service.order;

import gift.dto.order.OrderRequestDto;
import gift.dto.order.OrderResponseDto;
import gift.entity.Member;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Member member);
}
