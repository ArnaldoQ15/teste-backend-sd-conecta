package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.CrmInDto;
import br.sdconecta.testebackend.dto.CrmOutDto;
import br.sdconecta.testebackend.model.Crm;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.repository.CrmRepository;
import br.sdconecta.testebackend.util.ParameterFind;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Data
@RunWith(MockitoJUnitRunner.class)
class CrmServiceTest {

    @InjectMocks
    private CrmService service;

    @Mock
    private CrmRepository repository;

    @Mock
    private Crm crm;

    @Mock
    private User user;

    @Mock
    private CrmInDto inDto;

    @Mock
    private CrmInDto outDto;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ParameterFind parameterFind;

    @Mock
    private Pageable pageRequest;

    @Mock
    private Page<Crm> page;

    @Mock
    private Page<CrmOutDto> pageOutDto;


    @BeforeEach
    public void init() {
        initMocks(this);
    }


    @Test
    @DisplayName("Deve converter uma lista de CrmInDto para uma lista de Crm")
    void convertDtoListToEntityList() {
        List<CrmInDto> crmInDtoList = new ArrayList<>();
        List<Crm> crms = new ArrayList<>();
        crmInDtoList.add(inDto);
        when(modelMapper.map(inDto, Crm.class)).thenReturn(crm);
        service.convertDtoListToEntityList(crmInDtoList, user);
        assertNotNull(crms);
    }

    @Test
    @DisplayName("Deve retornar todos os CRMs do sistema")
    void findAll() {
        when(parameterFind.getPage()).thenReturn(0);
        when(parameterFind.getSize()).thenReturn(10);
        when(service.getPageCrm(parameterFind)).thenReturn(page);
        service.findAll(parameterFind);
        assertNotNull(pageOutDto);
    }

    @Test
    @DisplayName("Deve retornar todos os CRMs do sistema com filtro de specialty")
    void findAllByEspeciality() {
        when(parameterFind.getPage()).thenReturn(0);
        when(parameterFind.getSize()).thenReturn(10);
        when(parameterFind.getSpecialty()).thenReturn("Teste");
        when(service.getPageCrm(parameterFind)).thenReturn(page);
        service.findAll(parameterFind);
        assertNotNull(pageOutDto);
    }

    @Test
    @DisplayName("Deve retornar um CRM pelo ID")
    void findId() {
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(crm));
        service.findId(1L);
        assertNotNull(outDto);
    }

    @Test
    @DisplayName("Deve atualizar um CRM pelo ID")
    void update() {
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(crm));
        service.update(1L, inDto);
        assertNotNull(outDto);
    }

    @Test
    @DisplayName("Deve deletar um CRM pelo ID")
    void delete() {
        service.delete(1L);
        verify(repository).deleteById(anyLong());
    }

}