package sorokin.school.springbootwebtask.models.users;

import org.springframework.stereotype.Service;
import sorokin.school.springbootwebtask.errors.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private long idCounter;
    private final Map<Long,UserDto> userMap;

    public UserService() {
        this.idCounter = 0;
        this.userMap = new HashMap<>();
    }

    public UserDto createUser(
            UserDto userToCreate
    ) {
        var newId = ++idCounter;
        var newUser = new UserDto(
                newId,
                userToCreate.getName(),
                userToCreate.getEmail(),
                userToCreate.getAge(),
                new ArrayList<>()
        );

        userMap.put(newId, newUser);
        return newUser;
    }

    public UserDto getUserById(
            Long id
    ) {
        return Optional.ofNullable(userMap.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("No such user"));
    }

    public UserDto updateUser(
            Long id,
            UserDto userToUpdate
    ) {
       var existingUser = getUserById(id);
       var updatedUser = new UserDto(
               id,
               userToUpdate.getName(),
               userToUpdate.getEmail(),
               userToUpdate.getAge(),
               existingUser.getPets()
       );

       userMap.put(id, updatedUser);
       return updatedUser;
    }

    public void deleteUser(
            Long id
    ) {
        getUserById(id);
        userMap.remove(id);
    }
}
