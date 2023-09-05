package samdasu.recipt.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import samdasu.recipt.domain.controller.dto.Review.ReviewRequestDto;
import samdasu.recipt.domain.entity.Recipe;
import samdasu.recipt.domain.repository.recipe.RecipeRepository;
import samdasu.recipt.domain.service.RecipeService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecipeServiceTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    RecipeRepository dbRecipeRepository;
    @Autowired
    RecipeService recipeService;

    @Test
    public void 평점_갱신() throws Exception {
        //given
        Recipe recipe = createRecipe();
        ReviewRequestDto reviewRequestDto = ReviewRequestDto.createReviewRequestDto("계란찜 맛있다.", 3.0);

        //when
        recipeService.updateRatingScore(recipe.getRecipeId(), reviewRequestDto);

        //then
        assertThat(recipe.getRatingPeople()).isEqualTo(2);
        assertThat(recipe.getRatingScore()).isEqualTo(4.0);
    }

    @Test
    public void 레시피_음식이름_단건조회() throws Exception {
        //given
        Recipe recipe = createRecipe();

        //when
        Recipe findRecipe = recipeService.findByFoodName("새우두부계란찜");

        //then
        assertThat(findRecipe.getFoodName()).isEqualTo("새우두부계란찜");
    }

    @Test
    public void 검색조건_동적쿼리() throws Exception {
        //given
        Recipe recipe = createRecipe();

        //when
        List<Recipe> findByFoodName = recipeService.searchDynamicSearching("계란찜", 0, 0L);

        //then
        assertThat(findByFoodName.size()).isEqualTo(1);
    }

    @Test
    public void 레시피_전체조회() throws Exception {
        //given

        //when
        List<Recipe> recipes = recipeService.findRecipes();

        //then
        assertThat(recipes.size()).isEqualTo(4);
    }

    private Recipe createRecipe() {
        Recipe recipe = Recipe.createRecipe("새우두부계란찜", "연두부 75g(3/4모), 칵테일새우 20g(5마리), 달걀 30g(1/2개), 생크림 13g(1큰술), 설탕 5g(1작은술), 무염버터 5g(1작은술), 고명, 시금치 10g(3줄기)",
                "찌기", "http://www.foodsafetykorea.go.kr/uploadimg/cook/10_00028_1.png", "1. 손질된 새우를 끓는 물에 데쳐 건진다. 2. 연두부, 달걀, 생크림, 설탕에 녹인 무염버터를 믹서에 넣고 간 뒤 새우(1)를 함께 섞어 그릇에 담는다. 3. 시금치를 잘게 다져 혼합물 그릇(2)에 뿌리고 찜기에 넣고 중간 불에서 10분 정도 찐다.",
                "http://www.foodsafetykorea.go.kr/uploadimg/cook/10_00028_1.png, http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00028_2.png, http://www.foodsafetykorea.go.kr/uploadimg/cook/20_00028_3.png",
                0L, 0, 5.0, 1);
        em.persist(recipe);

        return recipe;
    }
}