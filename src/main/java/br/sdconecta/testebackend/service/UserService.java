package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.*;
import br.sdconecta.testebackend.enums.ProfileType;
import br.sdconecta.testebackend.exception.BadRequestException;
import br.sdconecta.testebackend.exception.NotFoundException;
import br.sdconecta.testebackend.model.Crm;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.repository.UserRepository;
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
import java.util.Objects;
import java.util.Optional;

import static br.sdconecta.testebackend.enums.AuthorizationStatus.WAITING_FOR_AUTHORIZATION;
import static br.sdconecta.testebackend.enums.ProfileType.ADMIN;
import static br.sdconecta.testebackend.enums.ProfileType.NORMAL;
import static br.sdconecta.testebackend.util.Constants.*;
import static java.util.Locale.ROOT;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import static org.springframework.http.HttpStatus.CREATED;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private CrmService crmService;
    @Autowired
    private UserTokenService userTokenService;
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

    public ResponseEntity<UserOutDto> persistAdmin(UserInDto dto) {
        validateEmailExists(dto.getEmail());
        User entityNew = modelMapper.map(dto, User.class);

        entityNew.setPassword(sha256Hex(dto.getPassword()));
        entityNew.setCrms(crmService.convertDtoListToEntityList(dto.getCrms(), entityNew));
        entityNew.setProfileType(ADMIN);
        entityNew.setAuthorizationStatus(WAITING_FOR_AUTHORIZATION);

        repository.save(entityNew);
        return ResponseEntity.status(CREATED).body(modelMapper.map(entityNew, UserOutDto.class));
    }

    public ResponseEntity<List<CrmOutDto>> persistCrm(Long userId, List<CrmInDto> dto) {
        validateAdminStatus(userId);

        List<CrmOutDto> finalList = new ArrayList<>();
        dto.forEach(crm -> {
            User user = findUser(crm.getUserId());
            Crm entityNew = modelMapper.map(crm, Crm.class);
            entityNew.setUser(user);
            user.getCrms().add(entityNew);
            repository.save(user);
            finalList.add(modelMapper.map(entityNew, CrmOutDto.class));
        });

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

    public ResponseEntity<UserOutDto> update(Long userId, UserUpdateDto dto) {
        validateAdminStatus(userId);

        User user = findUser(dto.getUserIdChange());
        if (nonNull(dto.getEmail()) && !dto.getEmail().equals(user.getEmail())) {
            validateEmailExists(dto.getEmail());
            user.setEmail(dto.getEmail());
        }

        if (nonNull(dto.getPassword()) && !sha256Hex(dto.getPassword()).equals(user.getPassword())) {
            user.setPassword(sha256Hex(dto.getPassword()));
        }

        user.setName(isNull(dto.getName()) ? user.getName() : dto.getName());
        user.setMobilePhone(isNull(dto.getMobilePhone()) ? user.getMobilePhone() : dto.getMobilePhone());
        user.setSurname(isNull(dto.getSurname()) ? user.getSurname() : dto.getSurname());

        return ResponseEntity.ok(modelMapper.map(repository.save(user), UserOutDto.class));
    }

    public ResponseEntity<UserOutDto> updateProfileType(Long userId, Long userIdChange, ProfileType profileType) {
        validateAdminStatus(userId);
        User user = findUser(userIdChange);
        user.setProfileType(profileType);
        return ResponseEntity.ok(modelMapper.map(repository.save(user), UserOutDto.class));
    }

    public ResponseEntity<Void> delete(Long userId, Long userIdDelete) {
        validateAdminStatus(userId);

        repository.deleteById(userIdDelete);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<UserTokenDto> login(UserLoginDto dto, String authorization) {
        if (isNull(authorization) || authorization.isBlank() ||
                !authorization.startsWith(BEARER_TOKEN))
            throw new BadRequestException(INVALID_TOKEN);

        Optional<User> user = repository.findByEmail(dto.getEmail());
        if (user.isEmpty())
            throw new NotFoundException(WRONG_EMAIL_PASSWORD);

        if (sha256Hex(dto.getPassword()).equals(user.get().getPassword())) {
            user.get().setToken(userTokenService.getCompanyToken(authorization, user.get()));
            user.get().setAuthorizationStatus(user.get().getToken().getAuthorizationStatus());
            repository.save(user.get());
        } else {
            throw new NotFoundException(WRONG_EMAIL_PASSWORD);
        }
        return ResponseEntity.ok().build();
    }

    private void validateAdminStatus(Long userId) {
        Optional<User> admin = repository.findById(userId);
        if (admin.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        if (!admin.get().getProfileType().equals(ADMIN))
            throw new BadRequestException(ADMIN_ONLY);
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
            return repository.findByName(parameterFind.getName().toLowerCase(ROOT), pageRequest);
        return repository.findAll(pageRequest);
    }

}