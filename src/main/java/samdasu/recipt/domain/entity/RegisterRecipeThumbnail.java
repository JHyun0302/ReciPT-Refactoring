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
public class RegisterRecipeThumbnail extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "thumbnail_id")
    private Long thumbnailId;
    private String filename;
    private String type;

    @Lob
    @Column(name = "thumbnail", length = 1000)
    private byte[] thumbnailData;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "register_id")
    private RegisterRecipe registerRecipe;

    //== 연관관계 편의 메서드 ==//
    public void setRegisterRecipe(RegisterRecipe registerRecipe) {
        this.registerRecipe = registerRecipe;
    }

    //==생성 메서드==//
    public RegisterRecipeThumbnail(String filename, String type, byte[] thumbnailData) {
        this.filename = filename;
        this.type = type;
        this.thumbnailData = thumbnailData;
    }

    public static RegisterRecipeThumbnail createThumbnail(String filename, String type, byte[] thumbnailData) {
        return new RegisterRecipeThumbnail(filename, type, thumbnailData);
    }

    //==비지니스 로직==//

}
