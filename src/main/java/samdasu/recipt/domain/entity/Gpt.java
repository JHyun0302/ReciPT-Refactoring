package samdasu.recipt.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samdasu.recipt.domain.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gpt extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "gpt_id")
    private Long gptId;

    @Column(nullable = false)
    private String foodName;
    @Column(nullable = false)
    private String ingredient;
    @Column(nullable = false, length = 1000)
    private String context;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "gpt")
    private List<RegisterRecipe> registerRecipes = new ArrayList<>();

    //== 연관관계 편의 메서드 ==//

    //==생성 메서드==/
    public Gpt(String foodName, String ingredient, String context, User user) {
        this.foodName = foodName;
        this.ingredient = ingredient;
        this.context = context;
        this.user = user;
    }

    public static Gpt createGpt(String foodName, String ingredient, String context, User user) {
        Gpt gpt = new Gpt(foodName, ingredient, context, user);
        return gpt;
    }
}
