package gift.controller.api;

import gift.annotation.CurrentMember;
import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.Member;
import gift.service.wish.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<WishResponseDto>> findWishList(@CurrentMember Member member) {

        return ResponseEntity.ok(wishService.findWishList(member.getId()));
    }

    @GetMapping
    public ResponseEntity<Page<WishResponseDto>> findPageWishes(
            @CurrentMember Member member,
            @PageableDefault(size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(wishService.findPageWishes(member.getId(), pageable));
    }

    @PostMapping
    public ResponseEntity<WishResponseDto> createWish(
            @CurrentMember Member member,
            @Valid @RequestBody WishRequestDto dto
            ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(wishService.saveWish(member.getId(), dto));
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable Long wishId,
            @CurrentMember Member member
    ) {
        wishService.deleteWish(wishId, member);
        return ResponseEntity.noContent().build();
    }

}
