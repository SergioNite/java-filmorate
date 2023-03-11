package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import ru.yandex.practicum.filmorate.validate.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Film {
    @NonFinal
    Long id;
    @NotBlank
    @NonFinal
    String name;
    @NotBlank
    @NonFinal
    @Size(max = 200)
    String description;
    @ReleaseDate
    @NonFinal
    LocalDate releaseDate;
    @Positive
    @NonFinal
    Integer duration;
    @NonFinal
    Set<Long> likes = new HashSet<>();
    @NonFinal
    Set<Genre> genres = new HashSet<>();

    @NotNull(message = "Рейтинг MPA должен быть заполнен")
    @NonFinal
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @JsonProperty("mpa")
    private Mpaa mpa;

    public int getLikesCount() {
        return likes.size();
    }

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void deleteLike(Long userId) {
        likes.remove(userId);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("film_id", id);
        values.put("film_name", name);
        values.put("film_description", description);
        values.put("film_release_date", releaseDate);
        values.put("film_duration", duration);
        if (getMpa() != null) values.put("film_mpa_id", mpa.getId());
        return values;
    }
}
