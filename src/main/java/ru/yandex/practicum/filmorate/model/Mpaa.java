package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mpaa {
    private int id;
    @NotNull(message = "Имя должно быть заполненно")
    @NotBlank(message = "Имя не должно быть пустой строкой")
    private String name;
}
