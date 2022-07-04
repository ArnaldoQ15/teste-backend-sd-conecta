package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.*;
import br.sdconecta.testebackend.exception.BadRequestException;
import br.sdconecta.testebackend.exception.NotFoundException;
import br.sdconecta.testebackend.model.Crm;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.model.UserToken;
import br.sdconecta.testebackend.repository.CrmRepository;
import br.sdconecta.testebackend.repository.UserRepository;
import br.sdconecta.testebackend.util.ParameterFind;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.sdconecta.testebackend.enums.AuthorizationStatus.AUTHORIZED;
import static br.sdconecta.testebackend.enums.ProfileType.ADMIN;
import static br.sdconecta.testebackend.enums.ProfileType.NORMAL;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@Data
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
    @Mock
    private CrmRepository crmRepository;
    @Mock
    private CrmService crmService;
    @Mock
    private UserTokenService userTokenService;
    @Mock
    private User user;
    @Mock
    private User admin;
    @Mock
    private UserInDto inDto;
    @Mock
    private UserOutDto outDto;
    @Mock
    private UserUpdateDto updateDto;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ParameterFind parameterFind;
    @Mock
    private Pageable pageRequest;
    @Mock
    private Page<User> page;
    @Mock
    private Page<UserOutDto> pageOutDto;
    @Mock
    private CrmInDto crmInDto;
    @Mock
    private UserLoginDto loginDto;
    private String authorization = "Bearer teste";


    @Test
    @DisplayName("Deve salvar um usuário")
    void persist() {
        List<CrmInDto> crms = List.of(crmInDto);
        when(repository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(admin.getProfileType()).thenReturn(ADMIN);
        when(inDto.getEmail()).thenReturn("joao@email.com");
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(inDto, User.class)).thenReturn(user);
        when(inDto.getPassword()).thenReturn("123456");
        when(inDto.getCrms()).thenReturn(crms);
        service.persist(inDto, anyLong());
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar um novo usuário sem encontrar um usuário-admin registrador")
    void persistNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.persist(inDto, 1L));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar um usuário sendo um usuário com ProfileType normal")
    void persistBadRequestException() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(admin.getProfileType()).thenReturn(NORMAL);
        assertThrows(BadRequestException.class, () -> service.persist(inDto, 1L));
    }

    @Test
    @DisplayName("Deve lançar exceção ao encontrar um email já cadastrado")
    void persistBadRequestExceptionEmailRegistered() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(admin.getProfileType()).thenReturn(ADMIN);
        when(inDto.getEmail()).thenReturn("joao@email.com");
        when(repository.existsByEmail(anyString())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> service.persist(inDto, 1L));
    }

    @Test
    @DisplayName("Deve salvar um usuário administrador")
    void persistAdmin() {
        List<CrmInDto> crms = List.of(crmInDto);
        when(inDto.getEmail()).thenReturn("joao@email.com");
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(inDto, User.class)).thenReturn(user);
        when(inDto.getPassword()).thenReturn("123456");
        when(inDto.getCrms()).thenReturn(crms);
        service.persistAdmin(inDto);
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve salvar um CRM no usuário")
    void persistCrm() {
        List<CrmInDto> crms = List.of(crmInDto);
        when(repository.findById(1L)).thenReturn(Optional.of(admin));
        when(admin.getProfileType()).thenReturn(ADMIN);
        when(repository.findById(crmInDto.getUserId())).thenReturn(Optional.of(user));
        when(modelMapper.map(crmInDto, Crm.class)).thenReturn(new Crm());
        service.persistCrm(1L, crms);
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve buscar todos os usuários do sistema")
    void findAll() {
        Page<User> newPage = new PageImpl<>(List.of(user));
        when(parameterFind.getPage()).thenReturn(0);
        when(parameterFind.getSize()).thenReturn(10);
        when(service.getPageUser(parameterFind)).thenReturn(newPage);
        service.findAll(parameterFind);
        Assertions.assertNotNull(pageOutDto);
    }

    @Test
    @DisplayName("Deve buscar todos os usuários do sistema com filtro de nome")
    void findAllByName() {
        Page<User> newPage = new PageImpl<>(List.of(user));
        when(parameterFind.getPage()).thenReturn(0);
        when(parameterFind.getSize()).thenReturn(10);
        when(parameterFind.getName()).thenReturn("João");
        when(service.getPageUser(parameterFind)).thenReturn(newPage);
        service.findAll(parameterFind);
        Assertions.assertNotNull(pageOutDto);
    }

    @Test
    @DisplayName("Deve buscar um usuário pelo ID")
    void findId() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        service.findId(1L);
        Assertions.assertNotNull(outDto);
    }

    @Test
    @DisplayName("Deve atualizar o cadastro de um usuário")
    void update() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(admin.getProfileType()).thenReturn(ADMIN);
        when(updateDto.getEmail()).thenReturn("joao@email.com");
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(updateDto.getPassword()).thenReturn("123456");
        when(modelMapper.map(updateDto, User.class)).thenReturn(user);
        service.update(anyLong(), updateDto);
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar o status de admin de um usuário")
    void updateProfileType() {
        when(repository.findById(1L)).thenReturn(Optional.of(admin));
        when(admin.getProfileType()).thenReturn(ADMIN);
        when(repository.findById(2L)).thenReturn(Optional.of(user));
        service.updateProfileType(1L, 2L, ADMIN);
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve deletar um cadastro de usuário")
    void delete() {
        List<Crm> crmList = new ArrayList<>();
        when(repository.findById(1L)).thenReturn(Optional.of(admin));
        when(admin.getProfileType()).thenReturn(ADMIN);
        when(crmRepository.findByUserId(2L)).thenReturn(crmList);
        service.delete(1L, 2L);
        verify(repository).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar buscar um usuário com ID inexistente")
    void findUserNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findUser(1L));
    }

    @Test
    @DisplayName("Deve conseguir fazer o login com sucesso")
    void login() {
        UserToken tokenNew = new UserToken();
        tokenNew.setAuthorizationStatus(AUTHORIZED);
        when(repository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(sha256Hex("123456"));
        when(loginDto.getPassword()).thenReturn("123456");
        when(user.getToken()).thenReturn(tokenNew);
        service.login(loginDto, authorization);
        Assertions.assertNotNull(tokenNew);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao enviar um token Bearer inválido")
    void loginWithoutBearer() {
        assertThrows(BadRequestException.class, () -> service.login(loginDto, "teste"));
    }

    @Test
    @DisplayName("Não deve encontrar o usuário ao fazer o login com e-mail sem cadastro")
    void loginWithoutRegistry() {
        when(repository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.login(loginDto, authorization));
    }

    @Test
    @DisplayName("Não deve conseguir fazer o login com a senha errada")
    void loginWrongPassword() {
        when(repository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn("123456");
        when(loginDto.getPassword()).thenReturn("123");
        assertThrows(NotFoundException.class, () -> service.login(loginDto, authorization));
    }

}