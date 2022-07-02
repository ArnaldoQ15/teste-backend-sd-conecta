package br.sdconecta.testebackend.controller;

import br.sdconecta.testebackend.dto.CrmInDto;
import br.sdconecta.testebackend.dto.CrmOutDto;
import br.sdconecta.testebackend.service.CrmService;
import br.sdconecta.testebackend.util.ParameterFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crm")
public class CrmController {

    @Autowired
    private CrmService service;


    @GetMapping
    public ResponseEntity<Page<CrmOutDto>> findAll(@RequestParam(value = "page", required = false) Integer page,
                                                   @RequestParam(value = "size", required = false) Integer size,
                                                   @RequestParam(value = "specialty", required = false) String specialty) {
        ParameterFind parameterFind = ParameterFind.builder().page(page).size(size).specialty(specialty).build();
        return service.findAll(parameterFind);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CrmOutDto>> findAllById(@RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "size", required = false) Integer size,
                                                       @RequestParam(value = "specialty", required = false) String specialty,
                                                       @PathVariable Long userId) {
        ParameterFind parameterFind = ParameterFind.builder().page(page).size(size).specialty(specialty).build();
        return service.findAllById(parameterFind, userId);
    }

    @GetMapping("/{crmId}")
    public ResponseEntity<CrmOutDto> findId(@PathVariable Long crmId) {
        return service.findId(crmId);
    }

    @Transactional
    @PutMapping("/{crmId}/update")
    public ResponseEntity<CrmOutDto> update(@PathVariable Long crmId, @RequestBody CrmInDto dto) {
        return service.update(crmId, dto);
    }

    @DeleteMapping("/{crmId}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long crmId) {
        return service.delete(crmId);
    }

}