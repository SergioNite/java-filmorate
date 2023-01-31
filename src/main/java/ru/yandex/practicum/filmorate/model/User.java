package ru.yandex.practicum.filmorate.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDate birthday;
    @NonFinal
    Set<Long> friends = new HashSet<>();

    public void addFriend(User user) {
        this.friends.add(user.getId());
    }
}
