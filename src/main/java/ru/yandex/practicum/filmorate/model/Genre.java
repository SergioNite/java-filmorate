package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private int id;
    @NotNull(message = "Имя должно быть заполненно")
    @NotBlank(message = "Имя не должно быть пустой строкой")
    private String name;
    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("genre_id", id);
        values.put("genre_name", name);
        return values;
    }
}
