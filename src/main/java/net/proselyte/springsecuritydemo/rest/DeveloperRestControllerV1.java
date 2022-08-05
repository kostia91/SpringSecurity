package net.proselyte.springsecuritydemo.rest;

import net.proselyte.springsecuritydemo.model.Developer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/developers")
//этот класс нам нужен взамен ещё не созданной коллекции
//в будущем мы его удалим когда подключим бд
public class DeveloperRestControllerV1 {
    private List<Developer> DEVELOPERS = Stream.of(
            new Developer(1L, "Ivan", "Ivanov"),
            new Developer(2L, "Sergey", "Sergeev"),
            new Developer(3L, "Petr", "Petrov")
    ).collect(Collectors.toList());//это конструкция закидывает в коллекцию

    @GetMapping//я не очень понимаю зачем эта аннотация
    //вероятно, что если мы ставим аннотацию которая у нас есть в основании класса
    //@RequestMapping("/api/v1/developers"), то аннотация @GetMapping позволяет делать то,
    //что ты в методе getAll() вызвал
    public List<Developer> getAll() {
        return DEVELOPERS;
    }

    @GetMapping("/{id}")//здесь тоже самое, но уже если ты на сервер добавляешь /номер idка
    // например "/3", то тебе вызовет того, кто в коллекции будет стоять под 3 номером
    //повторюсь, эта конструкция при существовании БД, нам не нужна
    @PreAuthorize("hasAuthority('developers:read')")//аннотация которой мы разграничиваем доступ
    //наверное потому что это getId и мы здесь берем чувачка
    public Developer getById(@PathVariable Long id) {//аннотация, которая связывает цифры id
        return DEVELOPERS.stream().filter(developer -> developer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping//другой запрос, уже запись - значит запрос будет PostMapping
    @PreAuthorize("hasAuthority('developers:write')")//аннотация которой мы разграничиваем доступ в configе
    //такая же аннотация, но уже здесь мы создаем, поэтому аусоритес другая
    public Developer create(@RequestBody Developer developer) {//мы должн принять из тела запроса
        // в конструктор передам не переменную, а объект
        this.DEVELOPERS.add(developer);//добавь разработчика
        return developer;//непонял почему мы здесь возвращаем, а не void
    }            //верни разработчика которого ты добавила

    @DeleteMapping("/{id}")//у делета своя аннотация
    //в пути мы ставим то, что должна быть переменная id
    @PreAuthorize("hasAuthority('developers:write')")//здесь write потому что юзер имеет одинаковые способности, что там,
    // что там. Так то вместо write мы могли другое писать
    public void deleteById(@PathVariable Long id) {//здесь у нас в конструкторе аннотация, потому что м передаем id
        this.DEVELOPERS.removeIf(developer -> developer.getId().equals(id));
        //в лямбдах не очень разбираюсь, но мы вероятно удаляем того, кто подходит по нашиему поиску
//из коллекции удалить девелопера, если
    }
}
