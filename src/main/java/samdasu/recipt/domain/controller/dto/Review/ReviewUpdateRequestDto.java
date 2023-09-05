package samdasu.recipt.domain.controller.dto.Review;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewUpdateRequestDto {
    @NotNull
    private String comment;

    public ReviewUpdateRequestDto(String comment) {
        this.comment = comment;
    }

    public static ReviewUpdateRequestDto createReviewUpdateRequestDto(String comment) {
        return new ReviewUpdateRequestDto(comment);
    }
}
