package br.com.evasion.src.service;

import br.com.evasion.src.dto.UserDTO;
import br.com.evasion.src.entity.UserEntity;
import br.com.evasion.src.enums.ErrorsEnum;
import br.com.evasion.src.exception.GeneralException;
import br.com.evasion.src.repository.UserEntityRepository;
import br.com.evasion.src.response.FindUserResponse;
import br.com.evasion.src.utils.BuilderObjectsUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public String authUser (String userEmail, String userPassword) {
        try {
            UserDTO userDTO = returnUserDTOByEntity(userEntityRepository.findUserByUserEmailAndUserPassword(userEmail, userPassword));
            return userDTO.getUserName();
        } catch (RuntimeException e){
            throw new GeneralException(ErrorsEnum.USER_OR_PASS_ERROR);
        }
    }

    public UserDTO createUser (UserDTO dto) {
        try {
            userCreateValidation(dto);
            userAlreadyExist(dto);
            dto.setUserDateCreation(LocalDateTime.now());
            UserEntity entity = BuilderObjectsUtils.buildUserEntity(dto);
            userEntityRepository.save(entity);
            return dto;
        } catch (RuntimeException e){
            throw e;
        }
    }

    public void deleteUser (String userEmail) {
        try {
            userEntityRepository.delete(userEntityRepository.findUserByUserEmail(userEmail).get());
        } catch (RuntimeException e){
            throw new GeneralException(ErrorsEnum.USER_NOT_EXIST);
        }
    }

    public List<FindUserResponse> findAllUsers () {
        try {
            List<FindUserResponse> listFindUserResponse = new ArrayList<>();
            BuilderObjectsUtils
                    .buildFindUserResponseList(BuilderObjectsUtils
                            .buildUserDTOList(userEntityRepository.findAll()));
            return listFindUserResponse;
        } catch (RuntimeException e){
            throw new GeneralException(ErrorsEnum.INTERNAL);
        }
    }
    private void userAlreadyExist (UserDTO dto) {
        UserDTO userDto = returnUserDTOByEntity(userEntityRepository.findUserByUserEmail(dto.getUserEmail()));
        if (userDto != null)
            throw new GeneralException(ErrorsEnum.USER_ALREADY_EXIST);
    }

    private void userCreateValidation (UserDTO dto) {
        if (null == dto) {
            throw new GeneralException(ErrorsEnum.USER_CREATE_VALIDATION_ERROR);
        } else if (null == dto.getUserName()) {
            throw new GeneralException(ErrorsEnum.USER_CREATE_VALIDATION_ERROR);
        } else if (null == dto.getUserType()) {
            throw new GeneralException(ErrorsEnum.USER_CREATE_VALIDATION_ERROR);
        } else if (null == dto.getUserPassword()) {
            throw new GeneralException(ErrorsEnum.USER_CREATE_VALIDATION_ERROR);
        } else if (null == dto.getUserEmail()) {
            throw new GeneralException(ErrorsEnum.USER_CREATE_VALIDATION_ERROR);
        }

    }

    private UserDTO returnUserDTOByEntity(Optional<UserEntity> userEntity) {
        return userEntity.map(BuilderObjectsUtils::buildUserDTO).orElse(null);
    }

}
