package gift.service.wish;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishService {

    List<WishResponseDto> findWishList(Long memberId);
    Page<WishResponseDto> findPageWishes(Long memberId, Pageable pageable);
    WishResponseDto saveWish(Long memberId, WishRequestDto dto);
    void deleteWish(Long wishId, Member member);
}
