package kr.co.mostx.japi.entity;

import jakarta.persistence.*;
import kr.co.mostx.japi.entity.common.DateField;
import kr.co.mostx.japi.type.Platform;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "mostxServey")
public class Servey extends DateField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk;

    private int serveyOne;
    private int serveyTwo;
    private int serveyThree;
    private int serveyFour;
    private int serveyFive;
    private String userName;
    private String userPhone;
    private String consultantName;
    @Column
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Column(unique = true)
    private String serveyNumber;
    private double avgScore;
    private int totalScore;

    @Builder
    public Servey(int serveyOne, int serveyTwo, int serveyThree, int serveyFour, int serveyFive, String userName, String userPhone, String consultantName,
                  Platform platform, String serveyNumber, double avgScore, int totalScore, LocalDateTime createdDate) {
        this.serveyOne = serveyOne;
        this.serveyTwo = serveyTwo;
        this.serveyThree = serveyThree;
        this.serveyFour = serveyFour;
        this.serveyFive = serveyFive;
        this.userName = userName;
        this.userPhone = userPhone;
        this.consultantName = consultantName;
        this.platform = platform;
        this.serveyNumber = serveyNumber;
        this.avgScore = avgScore;
        this.totalScore = totalScore;
        super.setCreatedDate(createdDate);
    }

    public static Servey of(int serveyOne, int serveyTwo, int serveyThree, int serveyFour, int serveyFive, String userName, String userPhone, String consultantName,
                            Platform platform, String serveyNumber, double avgScore, int totalScore, LocalDateTime createdDate) {
        return new Servey(serveyOne, serveyTwo, serveyThree, serveyFour, serveyFive, userName, userPhone,
                consultantName, platform, serveyNumber, avgScore, totalScore, createdDate);
    }
}
