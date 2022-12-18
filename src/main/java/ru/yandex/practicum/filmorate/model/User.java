package ru.yandex.practicum.filmorate.model;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
public class User {
    private Long id;
    @Email
    @NonNull
    @NotBlank
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public User(Long id, @NonNull @Valid String email, @NonNull @Valid String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
