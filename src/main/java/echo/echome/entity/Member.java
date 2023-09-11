package echo.echome.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

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


    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void inValidRefreshToken() {
        this.refreshToken = null;
    }
    // 나이 계산

}
