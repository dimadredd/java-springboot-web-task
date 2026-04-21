package sorokin.school.springbootwebtask.models.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import sorokin.school.springbootwebtask.models.pets.PetDto;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @Null
    private Long id;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 1, max = 30)
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email
    private String email;

    @NotNull
    @Min(value = 10, message = "Age should not be lower than 10.")
    private Integer age;

    private List<PetDto> pets;

    public UserDto(Long id, String name, String email, Integer age, List<PetDto> pets) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.pets = pets;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public List<PetDto> getPets() {
        return pets;
    }


    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", pets=" + pets +
                '}';
    }
}
