package br.sdconecta.testebackend.controller;

import br.sdconecta.testebackend.dto.CrmInDto;
import br.sdconecta.testebackend.dto.CrmOutDto;
import br.sdconecta.testebackend.dto.UserInDto;
import br.sdconecta.testebackend.dto.UserOutDto;
import br.sdconecta.testebackend.service.UserService;
import br.sdconecta.testebackend.util.ParameterFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;


    @Transactional
    @PostMapping("/new")
    public ResponseEntity<UserOutDto> persist(@RequestBody UserInDto dto) {
        return service.persist(dto);
    }

    @Transactional
    @PostMapping("/{userId}/new-crm")
    public ResponseEntity<CrmOutDto> persistCrm(@Valid @PathVariable Long userId, @RequestBody CrmInDto dto) {
        return service.persistCrm(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Page<UserOutDto>> findAll(@RequestParam(value = "page") Integer page,
                                                    @RequestParam(value = "size") Integer size,
                                                    @RequestParam(value = "name", required = false) String name) {
        ParameterFind parameterFind = ParameterFind.builder().page(page).size(size).name(name).build();
        return service.findAll(parameterFind);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserOutDto> findId(@Valid @PathVariable Long userId) {
        return service.findId(userId);
    }

    @Transactional
    @PutMapping("/{userId}/update")
    public ResponseEntity<UserOutDto> update(@Valid @PathVariable Long userId, @RequestBody UserInDto dto) {
        return service.update(userId, dto);
    }

    @Transactional
    @PutMapping("/{userId}/admin")
    public ResponseEntity<UserOutDto> updateAdmin(@Valid @PathVariable Long userId) {
        return service.updateAdmin(userId);
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<UserOutDto> delete(@Valid @PathVariable Long userId) {
        return service.delete(userId);
    }

}
