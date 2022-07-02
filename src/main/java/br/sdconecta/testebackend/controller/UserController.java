package br.sdconecta.testebackend.controller;

import br.sdconecta.testebackend.dto.*;
import br.sdconecta.testebackend.service.UserService;
import br.sdconecta.testebackend.util.ParameterFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService service;


    @Transactional
    @PostMapping("/user/{userId}/new")
    public ResponseEntity<UserOutDto> persist(@RequestBody UserInDto dto, @PathVariable Long userId) {
        return service.persist(dto, userId);
    }

    @Transactional
    @PostMapping("/user/{userId}/new-crm")
    public ResponseEntity<List<CrmOutDto>> persistCrm(@PathVariable Long userId, @RequestBody List<CrmInDto> dto) {
        return service.persistCrm(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Page<UserOutDto>> findAll(@RequestParam(value = "page") Integer page,
                                                    @RequestParam(value = "size") Integer size,
                                                    @RequestParam(value = "name", required = false) String name) {
        ParameterFind parameterFind = ParameterFind.builder().page(page).size(size).name(name).build();
        return service.findAll(parameterFind);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserOutDto> findId(@PathVariable Long userId) {
        return service.findId(userId);
    }

    @Transactional
    @PutMapping("/user/{userId}/update")
    public ResponseEntity<UserOutDto> update(@PathVariable Long userId, @RequestBody UserInDto dto) {
        return service.update(userId, dto);
    }

    @Transactional
    @PutMapping("/user/{userId}/profile-type/update")
    public ResponseEntity<UserOutDto> updateProfileType(@PathVariable Long userId, @RequestBody UserProfileTypeDto dto) {
        return service.updateProfileType(userId, dto);
    }

    @DeleteMapping("/user/{userId}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @RequestBody UserDeleteDto dto) {
        return service.delete(userId, dto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenDto> login(@RequestBody UserLoginDto dto, HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return service.login(dto, authorization);
    }

}
