package samdasu.recipt.domain.controller.dto.Heart;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import samdasu.recipt.domain.entity.Heart;


@Getter
@Setter
public class RegisterHeartDto {

    @NotBlank
    private Long userId;

    @NotBlank
    private Long registerId;

    private String foodName;

    private String category;

    private String ingredient;

    public RegisterHeartDto(Long userId, Long registerId, String foodName, String category, String ingredient) {
        this.userId = userId;
        this.registerId = registerId;
        this.foodName = foodName;
        this.category = category;
        this.ingredient = ingredient;
    }

    public static RegisterHeartDto createRegisterHeartDto(Long userId, Long registerId, String foodName, String category, String ingredient) {
        return new RegisterHeartDto(userId, registerId, foodName, category, ingredient);
    }

    public RegisterHeartDto(Heart heart) {
        userId = heart.getUser().getUserId();
        registerId = heart.getRegisterRecipe().getRegisterId();
        foodName = heart.getRegisterRecipe().getFoodName();
        category = heart.getRegisterRecipe().getCategory();
        ingredient = heart.getRegisterRecipe().getIngredient();
    }

    public static RegisterHeartDto createRegisterHeartDto(Heart heart) {
        return new RegisterHeartDto(heart);
    }


}


