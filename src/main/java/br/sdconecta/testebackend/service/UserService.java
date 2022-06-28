package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.dto.CrmInDto;
import br.sdconecta.testebackend.dto.CrmOutDto;
import br.sdconecta.testebackend.dto.UserInDto;
import br.sdconecta.testebackend.dto.UserOutDto;
import br.sdconecta.testebackend.exception.BadRequestException;
import br.sdconecta.testebackend.exception.NotFoundException;
import br.sdconecta.testebackend.model.Crm;
import br.sdconecta.testebackend.model.User;
import br.sdconecta.testebackend.repository.UserRepository;
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

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static br.sdconecta.testebackend.model.AuthorizationStatus.WAITING_FOR_AUTHORIZATION;
import static br.sdconecta.testebackend.util.ExceptionMessages.*;
import static org.springframework.http.HttpStatus.CREATED;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CrmService crmService;

    @Autowired
    private ModelMapper modelMapper;


    public ResponseEntity<UserOutDto> persist(UserInDto dto) {
        validateEmailExists(dto.getEmail());
        User entityNew = modelMapper.map(dto, User.class);

        entityNew.setCrms(crmService.convertDtoListToEntityList(dto.getCrms(), entityNew));
        entityNew.setAdmin(false);
        entityNew.setAuthorizationStatus(WAITING_FOR_AUTHORIZATION);

        repository.save(entityNew);
        return ResponseEntity.status(CREATED).build();
    }

    public ResponseEntity<CrmOutDto> persistCrm(Long userId, CrmInDto dto) {
        User user = findUser(userId);
        Crm entityNew = modelMapper.map(dto, Crm.class);
        entityNew.setUser(user);
        user.getCrms().add(entityNew);
        repository.save(user);
        return ResponseEntity.status(CREATED).build();
    }

    public ResponseEntity<Page<UserOutDto>> findAll(ParameterFind parameterFind) {
        Page<User> userEntities = getPageUser(parameterFind);
        UserOutDto outDto = new UserOutDto();
        Page<UserOutDto> userFinalList = userEntities.map(user -> {
            modelMapper.map(user, outDto);
            return outDto;
        });
        return ResponseEntity.ok(userFinalList);
    }

    public ResponseEntity<UserOutDto> findId(Long userId) {
        User user = findUser(userId);
        return ResponseEntity.ok(modelMapper.map(user, UserOutDto.class));
    }

    public ResponseEntity<UserOutDto> update(Long userId, UserInDto dto) {
        User user = findUser(userId);
        if (!dto.getEmail().equals(user.getEmail())) validateEmailExists(dto.getEmail());

        User entityNew = new User();
        BeanUtils.copyProperties(user, entityNew);

        entityNew.setEmail(dto.getEmail());
        entityNew.setMobilePhone(dto.getMobilePhone());
        entityNew.setName(dto.getName());
        entityNew.setSurname(dto.getSurname());

        repository.save(entityNew);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<UserOutDto> updateAdmin(Long userId) {
        User user = findUser(userId);
        user.setAdmin(true);
        repository.save(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<UserOutDto> delete(Long userId) {
        repository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    public void validateEmailExists(String email) {
        if (Boolean.TRUE.equals(repository.existsByEmail(email))) throw new BadRequestException(EMAIL_IN_USE);
    }

    public User findUser(Long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException(USER_NOT_FOUND);
        return user.get();
    }

    public Page<User> getPageUser(ParameterFind parameterFind) {
        Pageable pageRequest = PageRequest.of(parameterFind.getPage(), parameterFind.getSize(), Sort.by("name").ascending());
        if (parameterFind.getName() != null || (Objects.nonNull(parameterFind.getSpecialty()) && !parameterFind.getName().isBlank()))
            return repository.findByName(parameterFind.getName().toLowerCase(Locale.ROOT), pageRequest);
        return repository.findAll(pageRequest);
    }

}