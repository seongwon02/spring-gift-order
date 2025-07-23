package gift.service.wish;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.member.MemberNotFoundException;
import gift.exception.product.ProductNotFoundException;
import gift.exception.wishList.AlreadyInWishListException;
import gift.exception.wishList.WishAccessDeniedException;
import gift.exception.wishList.WishNotFoundException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishServiceImpl implements WishService{

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishServiceImpl(WishRepository wishRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<WishResponseDto> findWishList(Long memberId) {
        List<Wish> wishes = wishRepository.findAllByMemberId(memberId);

        return wishes.stream()
                .map(wish -> {
                    Product product = wish.getProduct();

                    return new WishResponseDto(
                            wish.getId(),
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getImageUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<WishResponseDto> findPageWishes(Long memberId, Pageable pageable) {
        Page<Wish> wishes = wishRepository.findAllByMemberId(memberId, pageable);

        return wishes.map(wish -> {
            Product product = wish.getProduct();

            return new WishResponseDto(
                    wish.getId(),
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl()
            );
        });
    }

    @Transactional
    @Override
    public WishResponseDto saveWish(Long memberId, WishRequestDto dto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(
                        "해당 ID의 멤버는 존재하지 않습니다."
                ));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "해당 ID의 상품은 존재하지 않습니다."
                ));

        if (wishRepository.existsByMemberIdAndProductId(memberId, product.getId())) {
            throw new AlreadyInWishListException(
                    "해당 상품은 이미 위시 리스트에 존재합니다."
            );
        }

        Wish newWish = new Wish(member, product);

        Wish wish = wishRepository.save(newWish);
        return new WishResponseDto(
                wish.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl());
    }

    @Transactional
    @Override
    public void deleteWish(Long wishId, Member member) {

        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() ->
                        new WishNotFoundException(
                                "해당 위시 상품은 존재하지 않습니다."
                        )
                );

        if (!wish.getMember().getId().equals(member.getId())) {
            throw new WishAccessDeniedException(
                    "삭제할 권한이 없습니다."
            );
        }

        wishRepository.deleteById(wishId);
    }
}
