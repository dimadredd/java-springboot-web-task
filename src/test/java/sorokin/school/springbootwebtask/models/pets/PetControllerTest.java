package sorokin.school.springbootwebtask.models.pets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sorokin.school.springbootwebtask.errors.ResourceNotFoundException;
import sorokin.school.springbootwebtask.models.users.UserDto;
import sorokin.school.springbootwebtask.models.users.UserService;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreatePet() throws Exception {

        var user = new UserDto(
                null,
                "John",
                "john@mail.ru",
                10,
                null
        );

        user = userService.createUser(user);

        var pet = new PetDto(
                null,
                "bobik",
                null
        );

        String petToJson = objectMapper.writeValueAsString(pet);

        String createdPetJson = mockMvc.perform(post("/pets/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petToJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto petResponse = objectMapper.readValue(createdPetJson, PetDto.class);

        Assertions.assertNotNull(petResponse.getId());
        Assertions.assertEquals(pet.getName(), petResponse.getName());
        Assertions.assertEquals(user.getId(), petResponse.getUserId());
    }

    @Test
    void shouldNotCreatePetWhenRequestNotValid() throws Exception {
        var user = new UserDto(
                null,
                "John",
                "john@mail.ru",
                10,
                null
        );

        user = userService.createUser(user);

        var pet = new PetDto(
                null,
                null,
                null
        );

        String petToJson = objectMapper.writeValueAsString(pet);

        mockMvc.perform(post("/pets/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petToJson)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotCreatePetWhenUserNotFound() throws Exception {
        var pet = new PetDto(
                null,
                "bobik",
                null
        );

        String petToJson = objectMapper.writeValueAsString(pet);

        mockMvc.perform(post("/pets/users/{userId}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petToJson)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessDeletePet() throws Exception {
        var user = userService.createUser(new UserDto(
                null,
                "John",
                "john@mail.ru",
                10,
                null
        ));

        var pet = petService.createPet(user.getId(), new PetDto(
                null,
                "bobik",
                null
        ));

        mockMvc.perform(delete("/pets/{id}", pet.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> petService.getPetById(pet.getId())
        );

        Assertions.assertTrue(
                userService.getUserById(user.getId()).getPets()
                        .stream()
                        .noneMatch(deletedPet -> deletedPet.getId().equals(pet.getId()))
        );
    }
}