package samdasu.recipt.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samdasu.recipt.domain.controller.dto.Recipe.RecipeResponseDto;
import samdasu.recipt.domain.controller.dto.Review.ReviewRequestDto;
import samdasu.recipt.domain.entity.Recipe;
import samdasu.recipt.domain.exception.ResourceNotFoundException;
import samdasu.recipt.domain.repository.recipe.RecipeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    /**
     * 평점 평균 계산
     */
    @Transactional
    public RecipeResponseDto updateRatingScore(Long recipeId, ReviewRequestDto reviewRequestDto) {
        Recipe recipe = findById(recipeId);
        recipe.updateRating(reviewRequestDto.getInputRatingScore());

        recipe.calcRatingScore(recipe);

        RecipeResponseDto recipeResponseDto = RecipeResponseDto.createRecipeResponseDto(recipe);

        return recipeResponseDto;
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    @Transactional
    public void resetViewCount() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime threeSecondsAgo = LocalDateTime.now().minusSeconds(3);

//        registerRecipeRepository.resetViewCount(threeSecondsAgo);
        recipeRepository.resetViewCount(yesterday);
    }

    public Recipe findByFoodName(String foodName) {
        return recipeRepository.findByFoodName(foodName)
                .orElseThrow(() -> new ResourceNotFoundException("Fail:No Recipe Info"));
    }

    public List<Recipe> findByCategory(String category) {
        return recipeRepository.findByCategory(category)
                .orElseThrow(() -> new ResourceNotFoundException("Fail:No Recipe Info For Category"));
    }

    public List<Recipe> searchDynamicSearching(String searchingFoodName, Integer likeCond, Long viewCond) {
        return recipeRepository.dynamicSearching(searchingFoodName, likeCond, viewCond);
    }

    @Transactional
    public void IncreaseViewCount(Long recipeId) {
        Recipe recipe = findById(recipeId);
        recipeRepository.addRecipeViewCount(recipe);
    }

    public List<Recipe> findRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe findById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fail: No Recipe Info"));
    }

    public List<Recipe> findTop10RecentRegister() {
        return recipeRepository.Top10RecentRegister();
    }

    public List<Recipe> findTop10Like() {
        return recipeRepository.Top10Like();
    }

    public List<Recipe> findTop10View() {
        return recipeRepository.Top10View();
    }

    public List<Recipe> findTop10RatingScore() {
        return recipeRepository.Top10RatingScore();
    }

    public List<Long> RandomRecommend() {
        List<Recipe> recipes = recipeRepository.findAll();
        List<Long> recipesId = new ArrayList<>();
        List<Long> result = new ArrayList<>();

        for (Recipe recipe : recipes) {
            recipesId.add(recipe.getRecipeId());
        }

        if (recipesId.isEmpty()) {
            throw new IllegalArgumentException("Error : No recipe data!");
        }

        Random random = new Random();

        while (result.size() < 10) {
            result.add(recipesId.get(random.nextInt(recipesId.size())));
        }

        return result;
    }
}