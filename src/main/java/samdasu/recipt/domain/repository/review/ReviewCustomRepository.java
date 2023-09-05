package samdasu.recipt.domain.repository.review;

import samdasu.recipt.domain.entity.Review;

import java.util.List;

public interface ReviewCustomRepository {
    void addReviewLikeCount(Review review);

    void subReviewLikeCount(Review review);

    List<Review> findRecipeReviews(Long selectRecipeId);

    List<Review> findRegisterRecipeReviews(Long selectRegisterId);

    List<Review> findReviewByWriter(String username);//글쓴이 조회

    List<Review> registerOrderByLike(Long selectRegisterId);

    List<Review> registerOrderByCreateDate(Long selectRegisterId);

    List<Review> recipeOrderByLike(Long selectRecipeId);

    List<Review> recipeOrderByCreateDate(Long selectRecipeId);

}