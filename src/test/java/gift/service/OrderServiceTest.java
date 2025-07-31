package gift.service;

import gift.config.KakaoClient;
import gift.dto.order.OrderRequestDto;
import gift.dto.order.OrderResponseDto;
import gift.entity.*;
import gift.exception.product.option.SoldOutException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import gift.service.kakao.KakaoOAuthService;
import gift.service.order.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private KakaoClient kakaoClient;

    @Mock
    private KakaoOAuthService kakaoOAuthService;

    private Member member;
    private Product product;
    private Option option;
    private MemberKakaoToken kakaoToken;

    @BeforeEach
    void setUp() {
        member = new Member("test@example.com", "password", RoleType.USER);
        product = new Product("Test Product", 10000L, "test.jpg", true, "");
        option = new Option("Test Option", 100L, product);
    }

    @DisplayName("정상적으로 상품 주문이 이뤄지는지 테스트")
    @Test
    void 정상적인_상품_주문() {
        // given
        OrderRequestDto requestDto = new OrderRequestDto(1L, 10, "정상적인 상품 주문");
        Order savedOrder = new Order(option, member, requestDto.quantity(), requestDto.message());

        given(optionRepository.findById(requestDto.optionId())).willReturn(Optional.of(option));
        given(wishRepository.existsByMemberIdAndProductId(member.getId(), product.getId())).willReturn(true);
        doNothing().when(wishRepository).deleteByMemberIdAndProductId(member.getId(), product.getId());
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);
        given(kakaoOAuthService.getValidAccessToken(member)).willReturn("valid-access-token");
        doNothing().when(kakaoClient).sendOrderToMe("valid-access-token", savedOrder);

        // when
        OrderResponseDto responseDto = orderService.createOrder(requestDto, member);

        // then
        assertThat(responseDto.optionId()).isEqualTo(option.getId());
        assertThat(responseDto.quantity()).isEqualTo(requestDto.quantity());
        assertThat(option.getQuantity()).isEqualTo(90);
    }

    @Test
    @DisplayName("해당 옵션의 재고 부족으로 주문이 실패하는지 테스트")
    void 재고_부족으로_인한_주문_실패() {
        // given
        String errorMessage = "해당 상품의 재고가 부족합니다.";
        OrderRequestDto requestDto = new OrderRequestDto(1L, 101, "Exceed quantity");
        given(optionRepository.findById(requestDto.optionId())).willReturn(Optional.of(option));

        // when, then
        SoldOutException exception = assertThrows(
                SoldOutException.class,
                () -> orderService.createOrder(requestDto, member)
        );

        assertThat(exception.getMessage()).isEqualTo(errorMessage);
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.CONFLICT);
    }
}
