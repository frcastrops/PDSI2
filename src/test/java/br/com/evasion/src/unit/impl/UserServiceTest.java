package br.com.evasion.src.unit.impl;

import br.com.evasion.src.dto.UserDTO;
import br.com.evasion.src.entity.UserEntity;
import br.com.evasion.src.enums.ErrorsEnum;
import br.com.evasion.src.enums.UserTypeEnum;
import br.com.evasion.src.exception.GeneralException;
import br.com.evasion.src.repository.UserEntityRepository;
import br.com.evasion.src.service.UserService;
import br.com.evasion.src.unit.config.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserEntityRepository userEntityRepository;

    @Test
    public void authUserTest() {
        when(userEntityRepository.findUserByUserEmailAndUserPassword(any(),any())).thenReturn(createUserEntity());
        userService.authUser(anyString(), anyString());
        verify(userEntityRepository, times(1)).findUserByUserEmailAndUserPassword(any(),any());
    }

    @Test
    public void authUserNotFoundTest() {
        when(userEntityRepository.findUserByUserEmailAndUserPassword(any(),any())).thenThrow(RuntimeException.class);
        assertThrows(GeneralException.class, () -> userService.authUser(anyString(), anyString()));
        verify(userEntityRepository, times(0)).findUserByUserEmail(any());
    }

    @Test
    public void createUserTest() {
        when(userEntityRepository.findUserByUserEmail(any())).thenReturn(Optional.empty());
        UserDTO dto = userService.createUser(createUserDTO(null));
        verify(userEntityRepository, times(1)).findUserByUserEmail(any());
        verify(userEntityRepository, times(1)).save(any());
        assertEquals(dto, createUserDTO(dto.getUserDateCreation()));
    }

    @Test
    public void createUserIsNullTest() {
        when(userEntityRepository.findUserByUserEmail(any())).thenReturn(Optional.empty());
        assertThrows(GeneralException.class, () -> userService.createUser(null));
        verify(userEntityRepository, times(0)).findUserByUserEmail(any());
        verify(userEntityRepository, times(0)).save(any());
    }

    @Test
    public void createUserNameIsNullTest() {
        when(userEntityRepository.findUserByUserEmail(any())).thenReturn(Optional.empty());
        UserDTO dto = createUserDTO(null);
        dto.setUserName(null);
        GeneralException e = assertThrows(GeneralException.class, () -> userService.createUser(dto));
        verify(userEntityRepository, times(0)).findUserByUserEmail(any());
        verify(userEntityRepository, times(0)).save(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getHttpStatus());
    }

    @Test
    public void createUserTypeIsNullTest() {
        when(userEntityRepository.findUserByUserEmail(any())).thenReturn(Optional.empty());
        UserDTO dto = createUserDTO(null);
        dto.setUserType(null);
        GeneralException e = assertThrows(GeneralException.class, () -> userService.createUser(dto));
        verify(userEntityRepository, times(0)).findUserByUserEmail(any());
        verify(userEntityRepository, times(0)).save(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getHttpStatus());
    }

    @Test
    public void createUserPasswordIsNullTest() {
        when(userEntityRepository.findUserByUserEmail(any())).thenReturn(Optional.empty());
        UserDTO dto = createUserDTO(null);
        dto.setUserPassword(null);
        GeneralException e = assertThrows(GeneralException.class, () -> userService.createUser(dto));
        verify(userEntityRepository, times(0)).findUserByUserEmail(any());
        verify(userEntityRepository, times(0)).save(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getHttpStatus());
    }

    @Test
    public void createUserEmailIsNullTest() {
        when(userEntityRepository.findUserByUserEmail(any())).thenReturn(Optional.empty());
        UserDTO dto = createUserDTO(null);
        dto.setUserEmail(null);
        GeneralException e = assertThrows(GeneralException.class, () -> userService.createUser(dto));
        verify(userEntityRepository, times(0)).findUserByUserEmail(any());
        verify(userEntityRepository, times(0)).save(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.USER_CREATE_VALIDATION_ERROR.getHttpStatus());
    }

    @Test
    public void createUserEmailAlreadyExistTest() {
        when(userEntityRepository.findUserByUserEmail(any())).thenReturn(createUserEntity());
        GeneralException e = assertThrows(GeneralException.class, () -> userService.createUser(createUserDTO(null)));
        verify(userEntityRepository, times(1)).findUserByUserEmail(any());
        verify(userEntityRepository, times(0)).save(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.USER_ALREADY_EXIST.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_ALREADY_EXIST.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_ALREADY_EXIST.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.USER_ALREADY_EXIST.getHttpStatus());
    }

    @Test
    public void deleteUserTest() {
        when(userEntityRepository.findUserByUserEmail(any())).thenReturn(createUserEntity());
        doNothing().when(userEntityRepository).delete(any());
        userService.deleteUser(any());
        verify(userEntityRepository, times(1)).findUserByUserEmail(any());
        verify(userEntityRepository, times(1)).delete(any());
    }

    @Test
    public void deleteUserNotFoundTest() {
        when(userEntityRepository.findUserByUserEmail(any())).thenReturn(Optional.empty());
        doNothing().when(userEntityRepository).delete(any());
        GeneralException e = assertThrows(GeneralException.class, () -> userService.deleteUser(any()));
        verify(userEntityRepository, times(1)).findUserByUserEmail(any());
        verify(userEntityRepository, times(0)).delete(any());
        assertEquals(e.getError().getMessage(), ErrorsEnum.USER_NOT_EXIST.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_NOT_EXIST.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.USER_NOT_EXIST.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.USER_NOT_EXIST.getHttpStatus());
    }

    @Test
    public void findAllUsersTest() {
        List<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(createUserEntity().get());
        when(userEntityRepository.findAll()).thenReturn(userEntityList);
        userService.findAllUsers();
        verify(userEntityRepository, times(1)).findAll();
    }

    @Test
    public void findAllUsersExceptionTest() {
        List<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(createUserEntity().get());
        when(userEntityRepository.findAll()).thenThrow(RuntimeException.class);
        GeneralException e = assertThrows(GeneralException.class, () -> userService.findAllUsers());
        verify(userEntityRepository, times(1)).findAll();
        assertEquals(e.getError().getMessage(), ErrorsEnum.INTERNAL.getMessage());
        assertEquals(e.getError().getCode(), ErrorsEnum.INTERNAL.getCode());
        assertEquals(e.getError().getCode(), ErrorsEnum.INTERNAL.getCode());
        assertEquals(e.getError().getHttpStatus(), ErrorsEnum.INTERNAL.getHttpStatus());
    }

    private Optional<UserEntity> createUserEntity() {
        return Optional.ofNullable(UserEntity.builder()
                        .userEmail("teste@teste")
                        .userPassword("12345")
                        .userDateCreation(LocalDateTime.now())
                        .build());
    }

    private UserDTO createUserDTO(LocalDateTime data) {
        return UserDTO.builder()
                .userName("Teste")
                .userPassword("12345")
                .idUser(new BigInteger("12345678"))
                .userType(UserTypeEnum.ADMINISTRATOR)
                .userEmail("teste@teste")
                .userDateCreation(data)
                .build();
    }
}
