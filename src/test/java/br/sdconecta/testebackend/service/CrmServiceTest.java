package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.CrmInDto;
import br.sdconecta.testebackend.dto.CrmOutDto;
import br.sdconecta.testebackend.dto.CrmUpdateDto;
import br.sdconecta.testebackend.exception.NotFoundException;
import br.sdconecta.testebackend.model.Crm;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.repository.CrmRepository;
import br.sdconecta.testebackend.util.ParameterFind;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Data
@ExtendWith(SpringExtension.class)
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
    private CrmOutDto outDto;
    @Mock
    private CrmUpdateDto updateDto;
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
        when(parameterFind.getSpecialty()).thenReturn("TokenCompany");
        when(service.getPageCrm(parameterFind)).thenReturn(page);
        service.findAll(parameterFind);
        assertNotNull(pageOutDto);
    }

    @Test
    @DisplayName("Deve retornar todos os CRMs do sistema pelo user ID enviado")
    void findAllById() {
        when(parameterFind.getPage()).thenReturn(0);
        when(parameterFind.getSize()).thenReturn(10);
        when(service.getPageCrmById(parameterFind, 1L)).thenReturn(page);
        when(modelMapper.map(crm, CrmOutDto.class)).thenReturn(outDto);
        service.findAllById(parameterFind, 1L);
        assertNotNull(pageOutDto);
    }

    @Test
    @DisplayName("Deve retornar todos os CRMs do sistema pelo user ID e specialty enviados")
    void findAllByIdAndSpecialty() {
        when(parameterFind.getPage()).thenReturn(0);
        when(parameterFind.getSize()).thenReturn(10);
        when(parameterFind.getSpecialty()).thenReturn("TokenCompany");
        when(service.getPageCrmById(parameterFind, 1L)).thenReturn(page);
        when(modelMapper.map(crm, CrmOutDto.class)).thenReturn(outDto);
        service.findAllById(parameterFind, 1L);
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
    @DisplayName("Deve retornar uma exceção ao procurar um CRM pelo ID inexistente")
    void findIdNotFoundException() {
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findId(1L));
    }

    @Test
    @DisplayName("Deve atualizar um CRM pelo ID")
    void update() {
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(crm));
        service.update(1L, updateDto);
        assertNotNull(outDto);
    }

    @Test
    @DisplayName("Deve deletar um CRM pelo ID")
    void delete() {
        service.delete(1L);
        verify(repository).deleteById(anyLong());
    }

}