package br.com.evasion.src.request;


import br.com.evasion.src.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {

    private String userName;

    private String userPassword;

    private String userEmail;

    private UserTypeEnum userType;
}
