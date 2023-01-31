package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import ru.yandex.practicum.filmorate.validate.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
public class Film {
    @NonFinal
    Long id;
    @NotBlank
    String name;
    @NotBlank
    @Size(max = 200)
    String description;
    @ReleaseDate
    LocalDate releaseDate;
    @Positive
    Integer duration;
    Set<Long> likes = new HashSet<>();
    public int getLikesCount() { return likes.size(); }
    public void addLike(Long userId) {
        likes.add(userId);
    }
    public void deleteLike(Long userId) {
        likes.remove(userId);
    }
}
