package echo.echome.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_email",nullable = false,unique = true)
    private String email;

    @Column(name = "member_enc_pwd",nullable = false)
    private String encryptedPwd;

    @Column(name = "member_name")
    private String name;

    @Column(name = "member_phone_num",nullable = false,unique = true)
    private String phoneNum;

    @Column(name = "member_createdAt")
    private LocalDateTime createdAt;

    @Column(name = "refreshToken")
    private String refreshToken;

    @Column(name = "unique_link")
    private UUID unique;


    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void inValidRefreshToken() {
        this.refreshToken = null;
    }
    // 나이 계산

    public void updateName(String name){
        this.name = name;
    }

}
