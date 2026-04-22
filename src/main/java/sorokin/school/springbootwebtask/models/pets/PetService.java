package sorokin.school.springbootwebtask.models.pets;

import org.springframework.stereotype.Service;
import sorokin.school.springbootwebtask.errors.ResourceNotFoundException;
import sorokin.school.springbootwebtask.models.users.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PetService {

    private AtomicLong idCounter = new AtomicLong();
    private final UserService userService;
    private final Map<Long, PetDto> petMap;


    public PetService(UserService userService) {

        this.userService = userService;
        this.petMap = new HashMap<>();
    }


    public PetDto createPet(
            Long userId,
            PetDto petToCreate) {

        var user = userService.getUserById(userId);
        var newPetID = idCounter.incrementAndGet();

        var createdPet = new PetDto(
                newPetID,
                petToCreate.getName(),
                user.getId()
        );

        user.getPets().add(createdPet);
        petMap.put(newPetID, createdPet);

        return createdPet;
    }

    public PetDto getPetById(Long petId) {
        return Optional.ofNullable(petMap.get(petId))
                .orElseThrow(() -> new ResourceNotFoundException("No such pet"));
    }


    public PetDto updatePet(
            Long petId,
            PetDto petToUpdate
    ) {
        var existingPet = getPetById(petId);

        var updatedPet = new PetDto(
                petId,
                petToUpdate.getName(),
                existingPet.getUserId()
        );

        var ownerUser = userService.getUserById(existingPet.getUserId());

        var petList = ownerUser.getPets();

        petList.removeIf(pet -> pet.getId().equals(petId));
        petList.add(updatedPet);

        petMap.put(petId, updatedPet);

        return updatedPet;
    }

    public void deletePet(
            Long petId
    ) {
        var existingPet = getPetById(petId);
        var ownerUser = userService.getUserById(existingPet.getUserId());

        ownerUser.getPets().removeIf(pet -> pet.getId().equals(petId));
        petMap.remove(petId);
    }
}
