package net.proselyte.springsecuritydemo.repository;

import net.proselyte.springsecuritydemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//интерфейс для взаимодействия с бд
public interface UserRepository extends JpaRepository<User, Long> {
    //наш главный индификатор емайл(почему не пароль не понятно), поэтому и метод так называется и возвращает
    //наверное если бы было другое, то другое бы возвращал
    Optional<User> findByEmail(String email);
}
