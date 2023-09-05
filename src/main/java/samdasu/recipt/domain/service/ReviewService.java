package samdasu.recipt.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samdasu.recipt.domain.controller.dto.Review.ReviewRequestDto;
import samdasu.recipt.domain.controller.dto.Review.ReviewUpdateRequestDto;
import samdasu.recipt.domain.entity.Recipe;
import samdasu.recipt.domain.entity.RegisterRecipe;
import samdasu.recipt.domain.entity.Review;
import samdasu.recipt.domain.entity.User;
import samdasu.recipt.domain.exception.DuplicateContextException;
import samdasu.recipt.domain.exception.ResourceNotFoundException;
import samdasu.recipt.domain.repository.UserRepository;
import samdasu.recipt.domain.repository.recipe.RecipeRepository;
import samdasu.recipt.domain.repository.register.RegisterRecipeRepository;
import samdasu.recipt.domain.repository.review.ReviewRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final RegisterRecipeRepository registerRecipeRepository;

    @Transactional
    public Long saveRecipeReview(Long userId, Long recipeId, ReviewRequestDto reviewRequestDto) {
        //엔티티 조회
        User user = findUserById(userId);
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fail: No Recipe Info"));

        List<Review> reviews = reviewRepository.findAll();

        Review createreview = Review.createRecipeReview(reviewRequestDto.getComment(), 0, reviewRequestDto.getInputRatingScore(), user, recipe);

        for (Review review : reviews) {
            if (review.getComment().equals(reviewRequestDto.getComment())) {
                throw new DuplicateContextException("중복된 리뷰입니다!");
            }
        }

        return reviewRepository.save(createreview).getReviewId();
    }


    @Transactional
    public Long saveRegisterRecipeReview(Long userId, Long registerRecipeId, ReviewRequestDto reviewRequestDto) {
        //엔티티 조회
        User user = findUserById(userId);
        RegisterRecipe registerRecipe = registerRecipeRepository.findById(registerRecipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fail: No Recipe Info"));

        List<Review> reviews = reviewRepository.findAll();

        Review createReview = Review.createRegisterReview(reviewRequestDto.getComment(), 0, reviewRequestDto.getInputRatingScore(), user, registerRecipe);

        for (Review review : reviews) {
            if (review.getComment().equals(reviewRequestDto.getComment())) {
                throw new DuplicateContextException("중복된 리뷰입니다!");
            }
        }
        return reviewRepository.save(createReview).getReviewId();
    }

    @Transactional
    public Long update(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto) {
        Review review = findById(reviewId);
        String inputComment = reviewUpdateRequestDto.getComment();
        review.updateReviewInfo(inputComment);
        return reviewId;
    }

    @Transactional
    public void delete(Long reviewId) {
        Review review = findById(reviewId);
        reviewRepository.delete(review);
    }

    public List<Review> findRecipeReviews(Long selectRecipeId) {
        return reviewRepository.findRecipeReviews(selectRecipeId);
    }

    /**
     * 좋아요 많은 순
     */
    public List<Review> registerOrderByLike(Long selectRegisterId) {
        return reviewRepository.registerOrderByLike(selectRegisterId);
    }

    public List<Review> recipeOrderByLike(Long selectRecipeId) {
        return reviewRepository.recipeOrderByLike(selectRecipeId);
    }

    public List<Review> findRegisterRecipeReviews(Long selectRegisterId) {
        return reviewRepository.findRegisterRecipeReviews(selectRegisterId);
    }

    /**
     * 최신 순
     */
    public List<Review> registerOrderByCreateDate(Long selectRegisterId) {
        return reviewRepository.registerOrderByCreateDate(selectRegisterId);
    }

    public List<Review> recipeOrderByCreateDate(Long selectRecipeId) {
        return reviewRepository.recipeOrderByCreateDate(selectRecipeId);
    }


    public List<Review> findReviewByWriter(String username) {
        List<Review> review = reviewRepository.findReviewByWriter(username);
        return review;
    }

    public List<Review> findReviews() {
        return reviewRepository.findAll();
    }

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Fail: No Review Info"));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Fail: No User Info"));
    }
}
