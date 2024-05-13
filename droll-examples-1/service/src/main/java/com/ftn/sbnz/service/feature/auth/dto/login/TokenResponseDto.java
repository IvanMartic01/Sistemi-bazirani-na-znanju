package com.ftn.sbnz.service.feature.auth.dto.login;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class TokenResponseDto {

    private String token;
}
