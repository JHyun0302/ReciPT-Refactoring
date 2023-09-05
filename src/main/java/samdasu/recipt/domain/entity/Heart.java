package samdasu.recipt.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samdasu.recipt.domain.common.BaseTimeEntity;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "heart")
public class Heart extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "heart_id")
    private Long heartId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "register_id")
    private RegisterRecipe registerRecipe;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    //== 연관관계 편의 메서드 ==//

    //==생성 메서드==//
    public Heart(User user, Recipe recipe) {
        this.user = user;
        this.recipe = recipe;
    }

    public static Heart createRecipeHeart(User user, Recipe recipe) {
        return new Heart(user, recipe);
    }

    public Heart(User user, RegisterRecipe registerRecipe) {
        this.user = user;
        this.registerRecipe = registerRecipe;
    }

    public static Heart createRegiterRecipeHeart(User user, RegisterRecipe registerRecipe) {
        return new Heart(user, registerRecipe);
    }

    public Heart(User user, Review review) {
        this.user = user;
        this.review = review;
    }

    public static Heart createReviewHeart(User user, Review review) {
        return new Heart(user, review);
    }
}
