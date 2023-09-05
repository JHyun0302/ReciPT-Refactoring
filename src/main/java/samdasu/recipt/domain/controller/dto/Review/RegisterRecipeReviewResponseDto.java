package samdasu.recipt.domain.controller.dto.Review;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import samdasu.recipt.domain.entity.Review;


@Getter
@Setter
public class RegisterRecipeReviewResponseDto {
    @NotEmpty
    private Long reviewId;
    @NotNull
    private String username; //전체 리뷰에서는 필요, 마이 페이지에서는 필요 X
    @NotNull
    private String comment;
    private Integer likeCount;

    private Double ratingScore;

    private byte[] registerThumbnailImage; //레시피 등록 - 썸네일 사진(마이페이지)

    public RegisterRecipeReviewResponseDto(Review review) {
        reviewId = review.getReviewId();
        username = review.getUser().getUsername();
        comment = review.getComment();
        likeCount = review.getLikeCount();
        ratingScore = review.getRatingScore();
        registerThumbnailImage = review.getRegisterRecipe().getRegisterRecipeThumbnail().getThumbnailData();
    }

    public static RegisterRecipeReviewResponseDto createRegisterRecipeReviewResponseDto(Review review) {
        return new RegisterRecipeReviewResponseDto(review);
    }
}
