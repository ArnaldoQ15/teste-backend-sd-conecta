package br.sdconecta.testebackend.controller;

import br.sdconecta.testebackend.dto.*;
import br.sdconecta.testebackend.enums.ProfileType;
import br.sdconecta.testebackend.service.UserService;
import br.sdconecta.testebackend.util.ParameterFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;


    @Transactional
    @PostMapping("/{userId}/new")
    public ResponseEntity<UserOutDto> persist(@RequestBody @Valid UserInDto dto, @PathVariable Long userId) {
        return service.persist(dto, userId);
    }

    @Transactional
    @PostMapping("/new-admin")
    public ResponseEntity<UserOutDto> persistAdmin(@RequestBody @Valid UserInDto dto) {
        return service.persistAdmin(dto);
    }

    @Transactional
    @PostMapping("/{userId}/new-crm")
    public ResponseEntity<List<CrmOutDto>> persistCrm(@PathVariable Long userId, @RequestBody @Valid List<CrmInDto> dto) {
        return service.persistCrm(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Page<UserOutDto>> findAll(@RequestParam(value = "page", required = false) Integer page,
                                                    @RequestParam(value = "size", required = false) Integer size,
                                                    @RequestParam(value = "name", required = false) String name) {
        ParameterFind parameterFind = ParameterFind.builder().page(page).size(size).name(name).build();
        return service.findAll(parameterFind);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserOutDto> findId(@PathVariable Long userId) {
        return service.findId(userId);
    }

    @Transactional
    @PutMapping("/{userId}/update")
    public ResponseEntity<UserOutDto> update(@PathVariable Long userId, @RequestBody @Valid UserUpdateDto dto) {
        return service.update(userId, dto);
    }

    @Transactional
    @PutMapping("/{userId}/profile-type/update")
    public ResponseEntity<UserOutDto> updateProfileType(@PathVariable Long userId, @RequestParam Long userIdChange,
                                                        @RequestParam ProfileType profileType) {
        return service.updateProfileType(userId, userIdChange, profileType);
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @RequestParam Long userIdDelete) {
        return service.delete(userId, userIdDelete);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenDto> login(@RequestBody @Valid UserLoginDto dto, HttpServletRequest request) {
        return service.login(dto, request.getHeader("Authorization"));
    }

}