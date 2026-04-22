package sorokin.school.springbootwebtask.models.pets;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetDto {

    @Null
    private Long id;

    @NotBlank(message = "Pet name must not be blank")
    @Size(min = 1, max = 30)
    private String name;

    @Null
    private Long userId;

    public PetDto(Long id, String name, Long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "PetDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                '}';
    }
}
