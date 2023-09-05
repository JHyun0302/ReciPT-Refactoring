package samdasu.recipt.domain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import samdasu.recipt.domain.controller.dto.Heart.RecipeHeartDto;
import samdasu.recipt.domain.controller.dto.Heart.RegisterHeartDto;
import samdasu.recipt.domain.controller.dto.Register.RegisterResponseDto;
import samdasu.recipt.domain.controller.dto.User.UserResponseDto;
import samdasu.recipt.domain.controller.dto.User.UserSignUpDto;
import samdasu.recipt.domain.controller.dto.User.UserUpdateRequestDto;
import samdasu.recipt.domain.entity.Heart;
import samdasu.recipt.domain.entity.RegisterRecipe;
import samdasu.recipt.domain.entity.Review;
import samdasu.recipt.domain.entity.User;
import samdasu.recipt.domain.service.*;
import samdasu.recipt.security.config.auth.PrincipalDetails;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApiController {
    private final UserService userService;
    private final ProfileService profileService;
    private final RegisterRecipeService registerRecipeService;
    private final ReviewService reviewService;
    private final HeartService heartService;

    @PostMapping("/signup")
    public Result1 saveUser(@Valid UserSignUpDto userSignUpDto, @RequestParam(value = "profile") MultipartFile file) throws IOException {
        Long savedProfileId = profileService.uploadImage(file);
        Long joinUserId = userService.join(userSignUpDto, savedProfileId);

        User findUser = userService.findById(joinUserId);
        log.info("findUser.getProfile().getFilename() = {}", findUser.getProfile().getFilename());
        log.info("findUser.getUsername = {}", findUser.getUsername());

        byte[] downloadImage = profileService.downloadImage(findUser.getProfile().getProfileId()); //프로필 사진

        return new Result1(1, new UserResponseDto(findUser), status(OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage));
    }

    /**
     * 프로필 보기
     */
    @GetMapping("/user")
    public Result1 userInfo(Authentication authentication) throws JsonProcessingException {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User findUser = userService.findById(principal.getUser().getUserId());
        byte[] downloadImage = profileService.downloadImage(findUser.getProfile().getProfileId()); //프로필 사진

        log.info("user.getUsername() = {}", findUser.getUsername());
        log.info("user.getLoginId() = {}", findUser.getLoginId());
        log.info("user.getPassword() = {}", findUser.getPassword());
        log.info("user.getAge() = {}", findUser.getAge());

        UserResponseDto responseDto = new UserResponseDto(findUser);
        // JSON 데이터와 이미지 데이터를 하나의 JSON 객체에 담음
        Map<String, Object> data = new HashMap<>();
        data.put("image", downloadImage);

        // JSON 객체를 byte[]로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] jsonData = objectMapper.writeValueAsBytes(data);

        // HTTP 응답 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentLength(jsonData.length);

        return new Result1(1, responseDto, new ResponseEntity<>(jsonData, headers, OK));
    }

    /**
     * 프로필 수정
     */
    @PostMapping("/user/edit")
    public Result1 updateUser(Authentication authentication,
                              @Valid UserUpdateRequestDto request) throws JsonProcessingException {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long updateUserId = userService.update(principal.getUser().getUserId(), request);

        User findUser = userService.findById(updateUserId);
        byte[] downloadImage = profileService.downloadImage(findUser.getProfile().getProfileId()); //프로필 사진

        log.info("user.getPassword() = {}", findUser.getPassword());

        UserResponseDto responseDto = new UserResponseDto(findUser);
        // JSON 데이터와 이미지 데이터를 하나의 JSON 객체에 담음
        Map<String, Object> data = new HashMap<>();
        data.put("image", downloadImage);

        // JSON 객체를 byte[]로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] jsonData = objectMapper.writeValueAsBytes(data);

        // HTTP 응답 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentLength(jsonData.length);

        return new Result1(1, responseDto, new ResponseEntity<>(jsonData, headers, OK));
    }

    /**
     * 유저가 누른 좋아요 보기
     */

    @GetMapping("/user/like")
    public Result2 searchLikeInfo(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User findUser = userService.findById(principal.getUser().getUserId());

//        RecipeHeartDto recipeHeartDto = RecipeHeartDto.createRecipeHeartDto(findUser.getUserId(), 3L, "된장 부대찌개", "찌개", "다시마 1g, 두부 10g, 떡국 떡 10g, 스팸(마일드) 10g, 다진 마늘 5g, 무 20g, 김치 15g, 소시지 10g(1/2개), 우민찌 5g(1작은술), 양파 5g, 저염된장 15g(1큰술), 베이컨 5g, 대파 5g, 청양고추 5g, 홍고추 1g");
//        RecipeHeartDto recipeHeartDto1 = RecipeHeartDto.createRecipeHeartDto(findUser.getUserId(), 10L, "구운채소", "채소", "가지 20g(3cm), 호박 50g(1/3개), 새송이버섯 15g(3개), 양파 15g(1/8개), 발사믹크레마 15g(1큰술), 빨강 파프리카 3g(3×1cm), 노랑 파프리카 3g(3×1cm), 청피망 3g(3×1cm), 올리브유 약간, 저염간장 5g(1작은술), 식초 15g(1큰술), 설탕 10g(1큰술), 레몬즙 5g(1작은술)");
//        RecipeHeartDto recipeHeartDto2 = RecipeHeartDto.createRecipeHeartDto(findUser.getUserId(), 9L, "함초 냉이 국수", "면", "소면 50g, 함초 3g(1/2작은술), 노루궁뎅이버섯 25g(1/2개), 다시마 17g(15cm), 파 140g(1/2개), 양파 150g(1/2개), 무 250g(1/3개), 모시조개 150g(3/4컵), 냉이 35g, 간장 30g(2큰술), 달걀 50g(1개), 청고추 20g(1개), 실고추 2g");
//        heartService.insertRecipeHeart(recipeHeartDto);
//        heartService.insertRecipeHeart(recipeHeartDto1);
//        heartService.insertRecipeHeart(recipeHeartDto2);
//
//        RegisterHeartDto registerHeartDto = RegisterHeartDto.createRegisterHeartDto(findUser.getUserId(), 18L, "만두", "기타", "고기피, 만두피");
//        RegisterHeartDto registerHeartDto1 = RegisterHeartDto.createRegisterHeartDto(findUser.getUserId(), 25L, "버섯구이", "채소", "새송이 버섯, 올리브유, 소금");
//        heartService.insertRegisterRecipeHeart(registerHeartDto);
//        heartService.insertRegisterRecipeHeart(registerHeartDto1);

        UserResponseDto responseDto = new UserResponseDto(findUser);
//        byte[] downloadImage = profileService.downloadImage(findUser.getProfile().getProfileId()); //프로필 사진

        List<RecipeHeartDto> recipeHeart = findUser.getHearts().stream()
                .filter(heart -> heart != null && heart.getRecipe() != null && heart.getRecipe().getRecipeId() != null) // null 값 필터링
                .map(RecipeHeartDto::new)
                .collect(Collectors.toList());
        List<RegisterHeartDto> registerHeart = findUser.getHearts().stream()
                .filter(heart -> heart != null && heart.getRegisterRecipe() != null && heart.getRegisterRecipe().getRegisterId() != null) // null 값 필터링
                .map(RegisterHeartDto::new)
                .collect(Collectors.toList());


//        return new Result(recipeHeart.size() + registerHeart.size(), responseDto, ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(downloadImage));

        return new Result2(recipeHeart.size() + registerHeart.size(), responseDto);
    }

    /**
     * 유저가 등록한 레시피 보기
     */
    @GetMapping("/user/register")
    public Result1 searchRegisterInfo(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User findUser = userService.findById(principal.getUser().getUserId());
        UserResponseDto responseDto = new UserResponseDto(findUser);
        byte[] downloadImage = profileService.downloadImage(findUser.getProfile().getProfileId()); //프로필 사진

        log.info("user.getUsername() = {}", findUser.getUsername());
        log.info("user.getRegisterRecipes.getFoodName() = {}", findUser.getRegisterRecipes().stream()
                .map(RegisterRecipe::getFoodName).collect(Collectors.toList()));

        List<RegisterResponseDto> collect = findUser.getRegisterRecipes().stream()
                .map(RegisterResponseDto::new)
                .collect(Collectors.toList());

        return new Result1(collect.size(), responseDto, status(OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage));
    }

    /**
     * 등록된 레시피 삭제
     */
    @PostMapping("/user/delete")
    public ResponseEntity<String> deleteRecipe(Authentication authentication
            , @RequestParam(value = "registerRecipeId") Long registerRecipeId) {
        boolean isDelete = false;
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User findUser = userService.findById(principal.getUser().getUserId());
        List<RegisterRecipe> registerRecipes = findUser.getRegisterRecipes();

        for (RegisterRecipe registerRecipe : registerRecipes) {
            if (registerRecipe.getRegisterId().equals(registerRecipeId)) {
                isDelete = true;
                List<Heart> hearts = registerRecipe.getHearts();
                for (Heart heart : hearts) {
                    RegisterHeartDto registerHeartDto = RegisterHeartDto.createRegisterHeartDto(heart);
                    heartService.deleteRegisterRecipeHeart(registerHeartDto);
                }
                registerRecipeService.deleteRegisterRecipe(registerRecipeId);
                return ResponseEntity.status(OK).body("레시피 삭제 완료!");
            }
        }
        return status(FORBIDDEN).body("삭제 권한이 없습니다!");
    }

    /**
     * 유저가 작성한 리뷰 보기
     */
    @GetMapping("/user/review")
    public Result1 searchReviewInfo(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User findUser = userService.findById(principal.getUser().getUserId());
        UserResponseDto responseDto = new UserResponseDto(findUser);
        byte[] downloadImage = profileService.downloadImage(findUser.getProfile().getProfileId()); //프로필 사진

        List<Review> reviews = reviewService.findReviewByWriter(findUser.getUsername());

        log.info("user.getUsername() = {}", findUser.getUsername());
        log.info("user.getReviews.getComment() = {}", findUser.getReviews().stream()
                .map(Review::getComment).collect(Collectors.toList()));

        return new Result1(reviews.size(), responseDto, status(OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage));
    }

    @Data
    @AllArgsConstructor
    static class Result1<T> {
        private int count; //특정 List의 개수 (ex. 사용자가 쓴 리뷰 개수)
        private T data;
        private T profile;
    }

    @Data
    @AllArgsConstructor
    static class Result2<T> {
        private int count; //특정 List의 개수 (ex. 사용자가 쓴 리뷰 개수)
        private T data;
    }

}

