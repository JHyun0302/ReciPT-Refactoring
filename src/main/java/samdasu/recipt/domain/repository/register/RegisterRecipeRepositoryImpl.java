package samdasu.recipt.domain.repository.register;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import samdasu.recipt.domain.entity.RegisterRecipe;

import java.time.LocalDateTime;
import java.util.List;

import static samdasu.recipt.domain.entity.QRegisterRecipe.registerRecipe;
import static samdasu.recipt.domain.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class RegisterRecipeRepositoryImpl implements RegisterCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public void addRegisterRecipeLikeCount(RegisterRecipe selectedRegisterRecipe) {
        queryFactory.update(registerRecipe)
                .set(registerRecipe.likeCount, registerRecipe.likeCount.add(1))
                .where(registerRecipe.eq(selectedRegisterRecipe))
                .execute();
    }

    @Override
    public void subRegisterRecipeLikeCount(RegisterRecipe selectedRegisterRecipe) {
        queryFactory.update(registerRecipe)
                .set(registerRecipe.likeCount, registerRecipe.likeCount.subtract(1))
                .where(registerRecipe.eq(selectedRegisterRecipe))
                .execute();
    }

    @Override
    public void addRegisterRecipeViewCount(RegisterRecipe selectedRegisterRecipe) {
        queryFactory.update(registerRecipe)
                .set(registerRecipe.viewCount, registerRecipe.viewCount.add(1))
                .where(registerRecipe.eq(selectedRegisterRecipe))
                .execute();
    }

    @Override
    public List<RegisterRecipe> dynamicSearching(String searchingFoodName, Integer likeCond, Long viewCond) {
        return queryFactory
                .selectFrom(registerRecipe)
                .where(searchByFoodNameContain(searchingFoodName), searchByLikeGoe(likeCond), searchByViewGoe(viewCond))
                .fetch();
    }

    private BooleanExpression searchByFoodNameContain(String searchingFoodName) {
        if (searchingFoodName == null) {
            return null;
        }
        return registerRecipe.foodName.contains(searchingFoodName);
    }

    private BooleanExpression searchByLikeGoe(Integer likeCond) {
        if (likeCond == null) {
            return null;
        }
        return registerRecipe.likeCount.goe(likeCond);
    }

    private BooleanExpression searchByViewGoe(Long viewCond) {
        if (viewCond == null) {
            return null;
        }
        return registerRecipe.viewCount.goe(viewCond);
    }


    @Override
    public List<RegisterRecipe> Top10Like() {
        return queryFactory
                .selectFrom(registerRecipe)
                .orderBy(registerRecipe.likeCount.desc(), registerRecipe.createDate.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<RegisterRecipe> Top10View() {
        return queryFactory
                .selectFrom(registerRecipe)
                .orderBy(registerRecipe.viewCount.desc(), registerRecipe.createDate.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<RegisterRecipe> Top10RatingScore() {
        return queryFactory
                .selectFrom(registerRecipe)
                .orderBy(registerRecipe.ratingScore.desc(), registerRecipe.createDate.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<RegisterRecipe> Top10RecentRegister() {
        return queryFactory
                .selectFrom(registerRecipe)
                .orderBy(registerRecipe.createDate.desc())
                .limit(10)
                .fetch();
    }


    @Override
    public void resetViewCount(LocalDateTime yesterday) {
        queryFactory
                .update(registerRecipe)
                .set(registerRecipe.viewCount, 0L)
                .where(registerRecipe.createDate.loe(yesterday))
                .execute();
    }

    @Override
    public List<String> RecommendByAge(int userAge) {
        return queryFactory
                .select(registerRecipe.foodName)
                .from(user)
                .join(registerRecipe).fetchJoin()
                .on(user.userId.eq(registerRecipe.user.userId))
                .where(user.age.goe((userAge / 10) * 10).and(user.age.lt(((userAge / 10) * 10) + 10)))
                .groupBy(registerRecipe.foodName)
                .orderBy(registerRecipe.likeCount.sum().desc())
                .limit(10)
                .fetch();

    }
}
