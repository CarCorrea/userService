package org.example.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.entity.Phone;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String name;

    @NotBlank(message = "Debe ingresar un correo electrónico")
    private String email;

    @NotBlank(message = "Debe ingresar la contraseña")
    private String password;

    private List<Phone> userPhones;
}
