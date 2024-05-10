package com.ftn.sbnz.app.feature.auth.dto.login;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class TokenResponseDto {

    private String token;
}
