package sorokin.school.springbootwebtask.models.pets;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }


    @PostMapping("/users/{usersId}")
    public ResponseEntity<PetDto> createPet(
            @PathVariable("usersId") Long userId,
            @RequestBody @Valid PetDto petToCreate

    ) {
        var createdPet = petService.createPet(userId, petToCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPet);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetDto> updatePet(
            @PathVariable("petId") Long userId,
            @RequestBody @Valid PetDto petToUpdate
    ) {
        return ResponseEntity
                .ok(petService.updatePet(userId, petToUpdate));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(
            @PathVariable("petId") Long petId
    ) {
        petService.deletePet(petId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetDto> getPetById(
            @PathVariable("petId") Long petId
    ) {
        return ResponseEntity
                .ok(petService.getPetById(petId));
    }
}
