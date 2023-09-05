package samdasu.recipt.domain.controller.dto.Heart;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import samdasu.recipt.domain.entity.Heart;

@Getter
@Setter
public class ReviewHeartDto {

    @NotBlank
    private Long userId;

    @NotBlank
    private Long reviewId;

    private String comment;

    public ReviewHeartDto(Long userId, Long reviewId, String comment) {
        this.userId = userId;
        this.reviewId = reviewId;
        this.comment = comment;
    }

    public static ReviewHeartDto createRecipeHeartDto(Long userId, Long reviewId, String comment) {
        return new ReviewHeartDto(userId, reviewId, comment);
    }

    public ReviewHeartDto(Heart heart) {
        userId = heart.getUser().getUserId();
        reviewId = heart.getReview().getReviewId();
        comment = heart.getReview().getComment();
    }

    public static ReviewHeartDto createRecipeHeartDto(Heart heart) {
        return new ReviewHeartDto(heart);
    }

}


