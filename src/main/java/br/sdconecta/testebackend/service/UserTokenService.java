package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.TokenRequestDto;
import br.sdconecta.testebackend.dto.UserTokenDto;
import br.sdconecta.testebackend.exception.BadRequestException;
import br.sdconecta.testebackend.exception.UnauthorizedException;
import br.sdconecta.testebackend.enums.AuthorizationStatus;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.model.UserToken;
import br.sdconecta.testebackend.repository.UserTokenRepository;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static br.sdconecta.testebackend.util.Constants.*;
import static java.lang.Long.parseLong;
import static java.time.OffsetDateTime.now;
import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class UserTokenService {

    @Autowired
    private UserTokenRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${sdconecta.oauth2.url}")
    private String clientUrl;


    @SneakyThrows
    public String decodeToken(String tokenNew) {
        String[] splitString = tokenNew.split("\\.");
        Base64 base64Url = new Base64(true);
        return new String(base64Url.decode(splitString[1]));
    }

    public Boolean validateBearerToken(String token) {
        if (!token.startsWith(BEARER_TOKEN)) {
            throw new BadRequestException(INVALID_TOKEN);
        }
        return true;
    }

    public UserToken getCompanyToken(String authorization, User user) {
        TokenRequestDto tokenRequestDto = modelMapper.map(user, TokenRequestDto.class);

        Optional<UserToken> userToken = repository.findByEmail(user.getEmail());
        if (userToken.isPresent())
            return validateExpirationToken(authorization, userToken.get(), tokenRequestDto);

        return modelMapper.map(createTokenDto(generateTokenCompany(authorization, tokenRequestDto)), UserToken.class);
    }

    private UserTokenDto createTokenDto(String companyTokenBody) {
        if (isNull(companyTokenBody))
            throw new UnauthorizedException(OFFLINE_SYSTEM_SD_CONECTA);

        if (companyTokenBody.split("\"access_token\":")[1].split(",")[0].equals("null"))
            return UserTokenDto.builder()
                    .authorizationStatus(AuthorizationStatus.valueOf(companyTokenBody.split("\"authorization_status\":\"")[1].split("\"}")[0]))
                    .build();

        return UserTokenDto.builder()
                .accessToken(companyTokenBody.split("\"access_token\":\"")[1].split("\",")[0])
                .refreshToken(companyTokenBody.split("\"refresh_token\":\"")[1].split("\",")[0])
                .expiration(generateExpirationToken(companyTokenBody.split("\"access_token\":\"")[1].split("\",")[0]))
                .authorizationStatus(AuthorizationStatus.valueOf(companyTokenBody.split("\"authorization_status\":\"")[1].split("\"}")[0]))
                .build();
    }

    private String generateTokenCompany(String authorization, TokenRequestDto userTokenRequest) {
        authorization = authorization.replace(BEARER_TOKEN, "");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(APPLICATION_JSON));
        headers.setBearerAuth(authorization);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(getStringObjectMap(userTokenRequest), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(clientUrl, httpEntity, String.class);

        if (isNull(response.getBody()) || response.getStatusCodeValue() != 200)
            throw new UnauthorizedException(UNAUTHORIZED_TOKEN);

        return response.getBody();
    }

    private UserToken validateExpirationToken(String authorization, UserToken userToken, TokenRequestDto tokenRequestDto) {
        if (userToken.getExpiration().isBefore(now())){
            generateTokenCompany(authorization, tokenRequestDto);
            UserTokenDto tokenDto = createTokenDto(generateTokenCompany(authorization, tokenRequestDto));
            userToken.setAccessToken(tokenDto.getAccessToken());
            userToken.setRefreshToken(tokenDto.getRefreshToken());
            userToken.setExpiration(tokenDto.getExpiration());
            userToken.setAuthorizationStatus(tokenDto.getAuthorizationStatus());
            return userToken;
        }
        return userToken;
    }

    private Map<String, Object> getStringObjectMap(TokenRequestDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", dto.getEmail());
        map.put("name", dto.getName());
        map.put("surname", dto.getSurname());
        map.put("crms", dto.getCrms());
        map.put("mobilePhone", dto.getMobilePhone());
        return map;
    }

    public OffsetDateTime generateExpirationToken(String token) {
        token = token.replace("Bearer ", "");
        String expiration = decodeToken(token);
        long segundos = (parseLong(expiration.split("\"exp\":")[1].split(",")[0]) - parseLong(expiration.split("\"iat\":")[1].split(",")[0]));
        return now().plusSeconds(segundos);
    }

}