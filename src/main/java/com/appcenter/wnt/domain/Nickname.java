package com.appcenter.wnt.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Nickname {
    private static final Pattern NICKNAME_REGEX = Pattern.compile("^(?!\\\\s+$)[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9._ ]+$");
    private static final int MAX_LENGTH = 20;

    @Column(nullable = false, length = MAX_LENGTH)
    private String nickname;

    public Nickname(String nickname) {
        String trimmedNickname = nickname.trim();
        validateRegex(trimmedNickname);
        this.nickname = trimmedNickname;
    }

    private static void validateRegex(String nickname) {
        if (!NICKNAME_REGEX.matcher(nickname).matches()) {
            throw new RuntimeException("올바르지 않은 닉네임 형식입니다.");
        }
    }
}
