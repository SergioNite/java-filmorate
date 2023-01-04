package ru.yandex.practicum.filmorate.model;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
public class User {
    @NonFinal
    Long id;
    @Email
    @NotBlank
    String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    String login;
    @NonFinal
    String name;
    @PastOrPresent
    private LocalDate birthday;

}
