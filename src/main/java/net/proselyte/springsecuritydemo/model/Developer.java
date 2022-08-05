package net.proselyte.springsecuritydemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data//ломбок добавляет все геттеры и сеттеры
@AllArgsConstructor//ломбок добавляет все конструкторы
//в этом классе мы просто создаем модель объекта:например developerа
//возможно ломбок не сработает(я так понял, что ломбок не работает в 15 версии, в 11 всё работает
public class Developer {
    private Long id;
    private String firstName;
    private String lastName;
}
