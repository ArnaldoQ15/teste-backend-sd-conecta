package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.CrmInDto;
import br.sdconecta.testebackend.dto.CrmOutDto;
import br.sdconecta.testebackend.dto.CrmUpdateDto;
import br.sdconecta.testebackend.exception.NotFoundException;
import br.sdconecta.testebackend.model.Crm;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.repository.CrmRepository;
import br.sdconecta.testebackend.util.ParameterFind;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static br.sdconecta.testebackend.util.Constants.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class CrmService {

    @Autowired
    private CrmRepository repository;
    @Autowired
    private ModelMapper modelMapper;


    public List<Crm> convertDtoListToEntityList(List<CrmInDto> crmInDtos, User user) {
        List<Crm> crms = new ArrayList<>();
        crmInDtos.forEach(dto -> crms.add(modelMapper.map(dto, Crm.class)));
        crms.forEach(crm -> crm.setUser(user));
        return crms;
    }

    public ResponseEntity<Page<CrmOutDto>> findAll(ParameterFind parameterFind) {
        Page<Crm> crmEntities = getPageCrm(parameterFind);
        Page<CrmOutDto> crmFinalList = crmEntities.map(crm -> modelMapper.map(crm, CrmOutDto.class));
        return ResponseEntity.ok(crmFinalList);
    }

    public ResponseEntity<Page<CrmOutDto>> findAllById(ParameterFind parameterFind, Long userId) {
        Page<Crm> crmEntities = getPageCrmById(parameterFind, userId);
        Page<CrmOutDto> crmFinalList = crmEntities.map(crm -> modelMapper.map(crm, CrmOutDto.class));
        return ResponseEntity.ok(crmFinalList);
    }

    public ResponseEntity<CrmOutDto> findId(Long crmId) {
        Crm crm = findCrmToEntity(crmId);
        return ResponseEntity.ok(modelMapper.map(crm, CrmOutDto.class));
    }

    public ResponseEntity<CrmOutDto> update(Long crmId, CrmUpdateDto dto) {
        Crm crm = findCrmToEntity(crmId);

        crm.setCrm(isNull(dto.getCrm()) ? crm.getCrm() : dto.getCrm());
        crm.setUf(isNull(dto.getUf()) ? crm.getUf() : dto.getUf());
        crm.setSpecialty(isNull(dto.getSpecialty()) ? crm.getSpecialty() : dto.getSpecialty());

        return ResponseEntity.ok(modelMapper.map(repository.save(crm), CrmOutDto.class));
    }

    public ResponseEntity<Void> delete(Long crmId) {
        repository.deleteById(crmId);
        return ResponseEntity.noContent().build();
    }

    private Crm findCrmToEntity(Long crmId) {
        Optional<Crm> crm = repository.findById(crmId);
        if (crm.isEmpty())
            throw new NotFoundException(CRM_NOT_FOUND);
        return crm.get();
    }

    public Page<Crm> getPageCrm(ParameterFind parameterFind) {
        parameterFind.setPage(isNull(parameterFind.getPage()) ? PAGE_PAGE : parameterFind.getPage());
        parameterFind.setSize(isNull(parameterFind.getSize()) ? PAGE_SIZE : parameterFind.getSize());

        Pageable pageRequest = PageRequest.of(parameterFind.getPage(), parameterFind.getSize(), Sort.by("crmId").ascending());
        if (nonNull(parameterFind.getSpecialty()) && !parameterFind.getSpecialty().isBlank())
            return repository.findBySpecialty(parameterFind.getSpecialty().toLowerCase(Locale.ROOT), pageRequest);
        return repository.findAll(pageRequest);
    }

    public Page<Crm> getPageCrmById(ParameterFind parameterFind, Long userId) {
        parameterFind.setPage(isNull(parameterFind.getPage()) ? PAGE_PAGE : parameterFind.getPage());
        parameterFind.setSize(isNull(parameterFind.getSize()) ? PAGE_SIZE : parameterFind.getSize());

        Pageable pageRequest = PageRequest.of(parameterFind.getPage(), parameterFind.getSize(), Sort.by("crmId").ascending());
        if (nonNull(parameterFind.getSpecialty()) && !parameterFind.getSpecialty().isBlank())
            return repository.findBySpecialtyAndUserId(parameterFind.getSpecialty().toLowerCase(Locale.ROOT), userId, pageRequest);
        return repository.findByUser_Id(userId, pageRequest);
    }

}