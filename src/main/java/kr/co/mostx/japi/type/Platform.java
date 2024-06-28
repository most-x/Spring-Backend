package kr.co.mostx.japi.type;

import lombok.Getter;

@Getter
public enum Platform {
    WRMS("WRMS"), ILSANG("ILSANG"),;

    private String platformName;
    Platform(String platformName) {
        this.platformName = platformName;
    }

    public String getCode() {
        return name();
    }

    public String getPlatformName() {
        return platformName;
    }
}
