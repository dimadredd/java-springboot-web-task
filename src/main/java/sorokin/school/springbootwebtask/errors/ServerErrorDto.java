package sorokin.school.springbootwebtask.errors;

import java.time.LocalDateTime;

public record ServerErrorDto(
        String message,
        String detailedMessage,
        LocalDateTime dateTime
) {
}
