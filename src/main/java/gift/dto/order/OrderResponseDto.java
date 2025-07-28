package gift.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record OrderResponseDto(
    Long id,

    Long optionId,

    Integer quantity,

    LocalDateTime orderDateTime,

    String message
) {}
