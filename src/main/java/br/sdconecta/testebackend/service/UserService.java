package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.*;
import br.sdconecta.testebackend.exception.BadRequestException;
import br.sdconecta.testebackend.exception.NotFoundException;
import br.sdconecta.testebackend.model.Crm;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.model.UserToken;
import br.sdconecta.testebackend.repository.CrmRepository;
import br.sdconecta.testebackend.repository.UserRepository;
import br.sdconecta.testebackend.repository.UserTokenRepository;
import br.sdconecta.testebackend.util.Constants;
import br.sdconecta.testebackend.util.ParameterFind;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static br.sdconecta.testebackend.model.AuthorizationStatus.*;
import static br.sdconecta.testebackend.model.ProfileType.ADMIN;
import static br.sdconecta.testebackend.model.ProfileType.NORMAL;
import static br.sdconecta.testebackend.util.Constants.*;
import static br.sdconecta.testebackend.util.Constants.PAGE_SIZE;
import static java.util.Objects.*;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import static org.springframework.http.HttpStatus.CREATED;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CrmRepository crmRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private CrmService crmService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ModelMapper modelMapper;


    public ResponseEntity<UserOutDto> persist(UserInDto dto, Long userId) {
        validateAdminStatus(userId);

        validateEmailExists(dto.getEmail());
        User entityNew = modelMapper.map(dto, User.class);

        entityNew.setPassword(sha256Hex(dto.getPassword()));
        entityNew.setCrms(crmService.convertDtoListToEntityList(dto.getCrms(), entityNew));
        entityNew.setProfileType(NORMAL);
        entityNew.setAuthorizationStatus(WAITING_FOR_AUTHORIZATION);

        repository.save(entityNew);
        return ResponseEntity.status(CREATED).body(modelMapper.map(entityNew, UserOutDto.class));
    }

    public ResponseEntity<List<CrmOutDto>> persistCrm(Long userId, List<CrmInDto> dto) {
        validateAdminStatus(userId);

        dto.forEach(crm -> {
            User user = findUser(crm.getUserId());
            Crm entityNew = modelMapper.map(crm, Crm.class);
            entityNew.setUser(user);
            user.getCrms().add(entityNew);
            repository.save(user);
        });

        User userCrm = findUser(dto.stream().map(CrmInDto::getUserId).findFirst().get());
        List<CrmOutDto> finalList = crmRepository.findByUserId(userCrm.getUserId()).stream()
                .map(crm -> modelMapper.map(crm, CrmOutDto.class)).toList();
        return ResponseEntity.status(CREATED).body(finalList);
    }

    public ResponseEntity<Page<UserOutDto>> findAll(ParameterFind parameterFind) {
        Page<User> userEntities = getPageUser(parameterFind);

        Page<UserOutDto> userFinalList = userEntities.map(user -> modelMapper.map(user, UserOutDto.class));
        return ResponseEntity.ok(userFinalList);
    }

    public ResponseEntity<UserOutDto> findId(Long userId) {
        User user = findUser(userId);
        return ResponseEntity.ok(modelMapper.map(user, UserOutDto.class));
    }

    public ResponseEntity<UserOutDto> update(Long userId, UserInDto dto) {
        validateAdminStatus(userId);

        User user = findUser(dto.getUserIdChange());
        if (!dto.getEmail().equals(user.getEmail()))
            validateEmailExists(dto.getEmail());

        if (nonNull(dto.getPassword()) && !dto.getPassword().isBlank()) {
            user.setPassword(sha256Hex(dto.getPassword()));
        }

        user.setEmail(dto.getEmail());
        user.setMobilePhone(dto.getMobilePhone());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());

        repository.save(user);
        return ResponseEntity.ok(modelMapper.map(user, UserOutDto.class));
    }

    public ResponseEntity<UserOutDto> updateProfileType(Long userId, UserProfileTypeDto dto) {
        validateAdminStatus(userId);
        User user = findUser(dto.getUserIdChange());
        user.setProfileType(dto.getProfileType());
        return ResponseEntity.ok(modelMapper.map(repository.save(user), UserOutDto.class));
    }

    public ResponseEntity<Void> delete(Long userId, UserDeleteDto dto) {
        validateAdminStatus(userId);

        crmRepository.deleteAll(crmRepository.findByUserId(dto.getUserId()));
        repository.deleteById(dto.getUserId());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<UserTokenDto> login(UserLoginDto dto, String authorization) {
        Optional<User> user = repository.findByEmail(dto.getEmail());
        if (user.isEmpty()) {
            throw new NotFoundException(Constants.WRONG_EMAIL_PASSWORD);
        }

        if (sha256Hex(dto.getPassword()).equals(user.get().getPassword()) && Boolean.TRUE.equals(tokenService
                .validateBearerToken(authorization))) {

            user.get().setToken(tokenService.getCompanyToken(authorization, user.get()));
            user.get().setAuthorizationStatus(user.get().getAuthorizationStatus());
            repository.save(user.get());
        } else {
            throw new NotFoundException(Constants.WRONG_EMAIL_PASSWORD);
        }
        return ResponseEntity.ok().build();
    }

    private void validateAdminStatus(Long userId) {
        Optional<User> admin = repository.findById(userId);
        if (admin.isEmpty()) {
            throw new NotFoundException(Constants.USER_NOT_FOUND);
        }
        if (!admin.get().getProfileType().equals(ADMIN)) {
            throw new BadRequestException(Constants.ADMIN_ONLY);
        }
    }

    public void validateEmailExists(String email) {
        if (Boolean.TRUE.equals(repository.existsByEmail(email)))
            throw new BadRequestException(EMAIL_IN_USE);
    }

    public User findUser(Long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty())
            throw new NotFoundException(USER_NOT_FOUND);
        return user.get();
    }

    public Page<User> getPageUser(ParameterFind parameterFind) {
        parameterFind.setPage(Objects.isNull(parameterFind.getPage()) ? PAGE_PAGE : parameterFind.getPage());
        parameterFind.setSize(Objects.isNull(parameterFind.getSize()) ? PAGE_SIZE : parameterFind.getSize());

        Pageable pageRequest = PageRequest.of(parameterFind.getPage(), parameterFind.getSize(), Sort.by("name").ascending());
        if (nonNull(parameterFind.getName()) && !parameterFind.getName().isBlank())
            return repository.findByName(parameterFind.getName().toLowerCase(Locale.ROOT), pageRequest);
        return repository.findAll(pageRequest);
    }

}