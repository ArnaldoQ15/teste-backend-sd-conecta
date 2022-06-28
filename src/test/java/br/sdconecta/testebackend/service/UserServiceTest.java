package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.CrmInDto;
import br.sdconecta.testebackend.dto.UserInDto;
import br.sdconecta.testebackend.dto.UserOutDto;
import br.sdconecta.testebackend.exception.BadRequestException;
import br.sdconecta.testebackend.exception.NotFoundException;
import br.sdconecta.testebackend.model.Crm;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.repository.UserRepository;
import br.sdconecta.testebackend.util.ParameterFind;
import lombok.Data;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@Data
@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private CrmService crmService;

    @Mock
    private User user;

    @Mock
    private UserInDto inDto;

    @Mock
    private UserOutDto outDto;

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


    @BeforeEach
    public void init() {
        initMocks(this);
    }


    @Test
    @DisplayName("Deve salvar um usuário")
    void persist() {
        List<CrmInDto> crms = List.of(crmInDto);
        when(inDto.getEmail()).thenReturn("joao@email.com");
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(inDto, User.class)).thenReturn(user);
        when(inDto.getCrms()).thenReturn(crms);
        service.persist(inDto);
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao encontrar um email já cadastrado")
    void persistBadRequest() {
        when(inDto.getEmail()).thenReturn("joao@email.com");
        when(repository.existsByEmail(anyString())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> service.persist(inDto));
    }

    @Test
    @DisplayName("Deve salvar um CRM no usuário")
    void persistCrm() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(crmInDto, Crm.class)).thenReturn(new Crm());
        service.persistCrm(1L, crmInDto);
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
        Assert.assertNotNull(pageOutDto);
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
        Assert.assertNotNull(pageOutDto);
    }

    @Test
    @DisplayName("Deve buscar um usuário pelo ID")
    void findId() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        service.findId(1L);
        Assert.assertNotNull(outDto);
    }

    @Test
    @DisplayName("Deve atualizar o cadastro de um usuário")
    void update() {
        when(inDto.getEmail()).thenReturn("joao@email.com");
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(inDto, User.class)).thenReturn(user);
        service.update(1L, inDto);
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar o status de admin de um usuário")
    void updateAdmin() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        service.updateAdmin(1L);
        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve deletar um cadastro de usuário")
    void delete() {
        service.delete(1L);
        verify(repository).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar buscar um usuário com ID inexistente")
    void findUserNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findUser(1L));
    }

}