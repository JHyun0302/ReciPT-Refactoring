package samdasu.recipt.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samdasu.recipt.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long reviewId;
    @Column(nullable = false)
    private String comment;
    private Integer likeCount;
    private Double ratingScore;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "register_id")
    private RegisterRecipe registerRecipe;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @OneToMany(mappedBy = "review")
    private List<Heart> hearts = new ArrayList<>();

    //== 연관관계 편의 메서드 ==//

    //==생성 메서드==//
    public Review(String comment, Integer likeCount, Double ratingScore, User user, Recipe recipe) {
        this.comment = comment;
        this.likeCount = likeCount;
        this.ratingScore = ratingScore;
        this.user = user;
        this.recipe = recipe;
    }

    public static Review createRecipeReview(String comment, Integer likeCount, Double ratingScore, User user, Recipe recipe) {
        Review review = new Review(comment, likeCount, ratingScore, user, recipe);
        return review;
    }

    public Review(String comment, Integer likeCount, Double ratingScore, User user, RegisterRecipe registerRecipe) {
        this.comment = comment;
        this.likeCount = likeCount;
        this.ratingScore = ratingScore;
        this.user = user;
        this.registerRecipe = registerRecipe;
    }

    public static Review createRegisterReview(String comment, Integer likeCount, Double ratingScore, User user, RegisterRecipe registerRecipe) {
        Review review = new Review(comment, likeCount, ratingScore, user, registerRecipe);
        return review;
    }

    //==비지니스 로직==//

    public void updateReviewInfo(String inputComment) {
        this.comment = inputComment;
    }

}
