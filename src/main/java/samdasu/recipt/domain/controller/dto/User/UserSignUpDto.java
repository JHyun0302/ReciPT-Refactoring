package samdasu.recipt.domain.controller.dto.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import samdasu.recipt.domain.entity.Profile;


@Getter
@Setter
@NoArgsConstructor
public class UserSignUpDto {

    @NotBlank(message = "이름을 입력해주세요")
    private String username;
    private byte[] profileData;
    @NotNull(message = "나이를 입력해주세요")
    private Integer age;
    @NotBlank(message = "아이디를 입력해주세요")
    private String loginId;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
    @NotBlank(message = "비밀번호가 일치하지 않습니다.")
    private String passwordConfirm;

    public UserSignUpDto(String username, Profile profile, Integer age, String loginId, String password, String passwordConfirm) {
        this.username = username;
        profileData = profile.getProfileData();
        this.age = age;
        this.loginId = loginId;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
}
