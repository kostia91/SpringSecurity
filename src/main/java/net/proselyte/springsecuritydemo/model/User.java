package net.proselyte.springsecuritydemo.model;

import lombok.Data;

import javax.persistence.*;

//класс для работы с БД
@Data//ломбок(должен сработать
@Entity//аннотация для работы с БД
@Table(name = "users")//название нашей БД
public class User {

    @Id//айдишник
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    //так как это 2 енама, то м их должны пометить
    // и перевести в стринг
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
