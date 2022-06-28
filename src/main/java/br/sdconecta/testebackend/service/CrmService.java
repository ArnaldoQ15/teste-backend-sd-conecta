package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.CrmInDto;
import br.sdconecta.testebackend.dto.CrmOutDto;
import br.sdconecta.testebackend.exception.NotFoundException;
import br.sdconecta.testebackend.model.Crm;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.repository.CrmRepository;
import br.sdconecta.testebackend.util.ParameterFind;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static br.sdconecta.testebackend.util.ExceptionMessages.CRM_NOT_FOUND;

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

    public ResponseEntity<CrmOutDto> findId(Long crmId) {
        Crm crm = findCrm(crmId);
        return ResponseEntity.ok(modelMapper.map(crm, CrmOutDto.class));
    }

    public ResponseEntity<CrmOutDto> update(Long crmId, CrmInDto dto) {
        Crm crm = findCrm(crmId);
        Crm entityNew = new Crm();
        BeanUtils.copyProperties(crm, entityNew);

        entityNew.setCrm(dto.getCrm());
        entityNew.setUf(dto.getUf());
        entityNew.setSpecialty(dto.getSpecialty());

        repository.save(entityNew);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> delete(Long crmId) {
        repository.deleteById(crmId);
        return ResponseEntity.noContent().build();
    }

    private Crm findCrm(Long crmId) {
        Optional<Crm> crm = repository.findById(crmId);
        if (crm.isEmpty()) throw new NotFoundException(CRM_NOT_FOUND);
        return crm.get();
    }

    public Page<Crm> getPageCrm(ParameterFind parameterFind) {
        Pageable pageRequest = PageRequest.of(parameterFind.getPage(), parameterFind.getSize(), Sort.by("specialty").ascending());
        if (parameterFind.getSpecialty() != null || (Objects.nonNull(parameterFind.getSpecialty()) && !parameterFind.getSpecialty().isBlank()))
            return repository.findBySpecialty(parameterFind.getSpecialty().toLowerCase(Locale.ROOT), pageRequest);
        return repository.findAll(pageRequest);
    }

}