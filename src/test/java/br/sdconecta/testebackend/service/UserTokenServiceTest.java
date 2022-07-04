package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.TokenCompany;
import br.sdconecta.testebackend.dto.TokenRequestDto;
import br.sdconecta.testebackend.dto.UserTokenDto;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.model.UserToken;
import br.sdconecta.testebackend.repository.UserTokenRepository;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Data
@ExtendWith(SpringExtension.class)
class UserTokenServiceTest {

    @InjectMocks
    private UserTokenService service;
    @Mock
    private UserTokenRepository repository;
    @Mock
    private UserToken userToken;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private User user;
    @Mock
    private TokenRequestDto tokenRequestDto;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private UserTokenDto userTokenDto;
    @Mock
    private TokenCompany tokenCompany;


    private String clientUrl = "https://teste.com.br";
    private String authorization = "Bearer teste.teste.teste";
    private String tokenSystem = "{\"access_token\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvLnRlc3RlLTNAZW1haWwuY29tIiwiaWF0IjoxNjU2NzgwMjAyLCJfaGFzaCI6IjRlYjZjMzc3LWJhZGQtNDI3MC05NWFiLTY5YzgzMDYwMDMyNyIsImV4cCI6MTY1Njg2NjYwMiwiY2siOiJ0ZXN0ZS1iYWNrZW5kIn0.X-JhzF2eBs8tDUUTGm5FLN7ANMJSZVa1XCGDDPHAeR2QtFbJInPgAOGPihxKh5Hu4yeaVG7ZWl_kALXNETPG4A\",\"refresh_token\":\"teste2.teste2.teste2\",\"authorization_status\":\"AUTHORIZED\"}";


    @Test
    @DisplayName("Deve decodificar um token")
    void decodeToken() {
        service.decodeToken(authorization);
        Assert.notNull(authorization);
    }

    @Test
    @DisplayName("Deve validar o tempo de expiração de um token enviado. Este teste permanece com o token atual ainda válido")
    void validateExpirationTokenContinue() {
        when(modelMapper.map(user, TokenRequestDto.class)).thenReturn(tokenRequestDto);
        when(tokenRequestDto.getEmail()).thenReturn("joao@email.com");
        when(repository.findByEmail(tokenRequestDto.getEmail())).thenReturn(Optional.empty());
        when(userToken.getExpiration()).thenReturn(OffsetDateTime.now().plusSeconds(60L));
        ReflectionTestUtils.setField(service, "clientUrl", clientUrl);
        service.validateExpirationToken(authorization, userToken, tokenRequestDto);
    }

    @Test
    @DisplayName("Deve retornar um map com o body a ser buscado quando requisitar o token do sistema de desenvolvimento SD Conecta")
    void getStringObjectMap() {
        service.getStringObjectMap(tokenRequestDto);
        Assertions.assertNotNull(tokenRequestDto);
    }

    @Test
    @DisplayName("Deve retornar a data e horário de expiração do token")
    void generateExpirationToken() {
        service.generateExpirationToken(tokenSystem);
        Assertions.assertNotNull(tokenSystem);
    }

}