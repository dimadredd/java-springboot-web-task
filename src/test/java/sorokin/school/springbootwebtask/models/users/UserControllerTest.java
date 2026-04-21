package sorokin.school.springbootwebtask.models.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreateUser() throws Exception {
        var user = new UserDto(
                null,
                "John",
                "john@mail.ru",
                10,
                null
        );

        String userToJson = objectMapper.writeValueAsString(user);

        String createdUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userResponse = objectMapper.readValue(createdUserJson, UserDto.class);

        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertEquals(user.getName(), userResponse.getName());
    }

    @Test
    void shouldNotCreateUserWhenRequestNotValid() throws Exception {
        var user = new UserDto(
                null,
                null,
                "john@mail.ru",
                10,
                null
        );

        String userToJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson)
                )
                .andExpect(status().is(400));
    }

    @Test
    void shouldSuccessGetUserById() throws Exception {
        var user = new UserDto(
                null,
                "John",
                "john@mail.ru",
                10,
                null
        );

        user = userService.createUser(user);

        String findUserJson = mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto foundUser = objectMapper.readValue(findUserJson, UserDto.class);

        org.assertj.core.api.Assertions.assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(foundUser);
    }

    @Test
    void shouldReturnNotFoundWhenUserNotPresent() throws Exception {
        mockMvc.perform(get("/users/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404));
    }
}