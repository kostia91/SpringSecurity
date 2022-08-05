package net.proselyte.springsecuritydemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//Зачем нам этот класс???Чтобы установить пароль и разграничить им права
@EnableGlobalMethodSecurity(prePostEnabled = true)//типо глобально security прописано в методах и
//без этой аннотации не сработает@PreAuthorize("hasAuthority('developers:read')")//аннотация которой мы разграничиваем доступ
//в контроллере
//вместо antMatchers
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //вариант не очень удобен и не очень гибок
//поэтому мы полностью переписываем роли этого метода и добавляем пермишионы
//не создаем юзера,админа и их друзей,а создаем возможность читать READ,
//WRITE и там уже
//    @Override
//    //метод по раздаче ролей/ осторожно вызвай. там 2 метода, один http, второй web
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()//это защита от csrf угрозы от которой spring sec предостовляет защиту по умолчанию
//                //аутентификация -это друг/враг.
//                // авторизация - к каким страницам имеет доступ или не имеет доступ
//                .authorizeRequests()
//                .antMatchers("/")//для того, чтоб указвать кто имеет доступ используем antMatchers
//                .permitAll()//доступ имеют все
//                //далее мы здесь хотим распределить роли на чтение и запись
//        //для этого  мы используем перегруженный метод antMatchers(), который принимает http?
//        //а мы в него запускаем типо метод GET
//                // звездочки** типа любое, что будет после api должно быть с определенными ролями
//                .antMatchers(HttpMethod.GET,"/api/**").hasAnyRole(Role.ADMIN.name()
//        , Role.USER.name())//hasAnyRole - это то, доступ имет любой, кто совпадает по ролям
//                //на чтение права доступа есть у admina и usera
//           .antMatchers(HttpMethod.POST,"/api/**").hasAnyRole(Role.ADMIN.name())
//           .antMatchers(HttpMethod.DELETE,"/api/**").hasAnyRole(Role.ADMIN.name())
//        //на удаление и внесение данных права только у admina
//            .anyRequest()//"каждый запрос"
//            .authenticated()//"должен быть аутентифицирован"
//            .and()//"и"
//            .httpBasic(); //"я хочу запоролит по бэзе64//я хочу использовать httpBasic"
//
//    }


    @Override//меняем метод
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                //у нас есть@PreAuthorize
//поэтому эти аннотации нам не нужны больше
//                .antMatchers(HttpMethod.GET,"/api/**").hasAuthority(Permission.DEVELOPERS_READ.getPermission())
//                //                        //на печать доступ
//                .antMatchers(HttpMethod.POST,"/api/**").hasAuthority(Permission.DEVELOPERS_WRITE.getPermission())
//                .antMatchers(HttpMethod.DELETE,"/api/**").hasAuthority(Permission.DEVELOPERS_WRITE.getPermission())

                .anyRequest()
                .authenticated()
                .and()
                //            .httpBasic();
                .formLogin()//я так понимаю, что это первая страница входа в программу
                .loginPage("/auth/login").permitAll()//важно поставить здесь пермитАлл(доступ для всех,
                //потому что выскочит ошибка. Я правда не понял, почему нельзя это поумолчании подразумевать, ведь тогда
                //это лишний код писать
                .defaultSuccessUrl("/auth/success")//если всё добра, то переход на этот URL
                .and()//и
                .logout()//я хочу настроить логаут, конкретно:
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
                .invalidateHttpSession(true)//инвалидируй сессию(чтобы это не значило
                .clearAuthentication(true)//уничтожь аутенцификацию(всю мою информацию:кто я,откуда,зачем,для чего)
                .deleteCookies("JSESSIONID")//при выходе удаляй куки мои
                .logoutSuccessUrl("/auth/login");//ну и переход после всего этого сюда
    }

    //!!!!мы удаляем этот метод и создаем сами Usera и UserDetailsServiceImpl в пакете security
//    @Bean//чтобы можно получить было доступ или //эта аннотация связывает друг с другом помеченых как бины
//    @Override
//    //у спринга есть его класс юзер, у которого по умолчании есть некоторе поля, которе можно использовать
//    // в аутсоритес.
//    //метод который вроде как разграничивает права пользователей: создает админа с паролем
//    //тут есть метод getаутсоритес, который разграничивает права пользователей типа admin и обычный user
//    protected UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager(
//                //В нашего менеджера мы запихиваем созданного юзера
//                User.builder()//юзер создаём
//                        .username("admin")//дай ему имя админ
//                        .password(passwordEncoder().encode("admin"))//дай ему пароль
//                        //откртым текстом пароли не храним, а кодируем его
//                        //вместо ролей ставим пермишшионсы
////                        .roles(Role.ADMIN.name())//роль - админ
//                        .authorities(Role.ADMIN.getAuthorites())
//                        .build(),//создай
//                //добавляем нового юзера
//                User.builder()
//                        .username("user")
//                        .password(passwordEncoder().encode("user"))
//                        .authorities(Role.USER.getAuthorites())
//                        .build()
//        );
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean//метод по кодировке пароля который мы используем в методе userDetailsService
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
