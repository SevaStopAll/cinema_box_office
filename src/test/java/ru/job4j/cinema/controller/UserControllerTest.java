package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserService userService;

    private UserController userController;

    private HttpSession httpSession;
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        httpSession = mock(HttpSession.class);
        httpServletRequest = mock(HttpServletRequest.class);
    }

    @Test
    public void whenRequestRegisterPageThenGetRegisterPage() {
        var view = userController.getRegistrationPage();
        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenPostUserThenRedirectToIndexPage() throws Exception {
        var user = new User(1, "test1@mail.ru", "testName1", "password");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/index");
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void whenRegisterUserThenRedirectToPage404() {
        Optional<User> emptyUser = Optional.empty();
        var expectedMessage = "Пользователь с такой почтой уже существует";
        var user = new User(1, "test1@mail.ru", "testName1", "password");
        when(userService.save(user)).thenReturn(emptyUser);

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenRequestLoginPageThenGetLoginPage() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginUserThenError() {
        var user = new User(1, "test1@mail.ru", "testName1", "password");
        Optional<User> dbUser = Optional.of(user);
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());
        var expectedMessage = "Почта или пароль введены неверно";

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, httpServletRequest);
        var actualMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenLoginUserThenRegirectIndexPage() {
        var user = new User(1, "test1@mail.ru", "testName1", "password");
        var optUser = Optional.of(user);
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(optUser);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getSession().getAttribute("user")).thenReturn(optUser);

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, httpServletRequest);

        Optional<User> actualUser = (Optional<User>) httpServletRequest.getSession().getAttribute("user");
        assertThat(view).isEqualTo("redirect:/index");
        assertThat(actualUser.get()).isEqualTo(user);

    }

    @Test
    public void whenRequestLogoutThenRedirectToLoginPage() {
        var view = userController.logout(httpSession);
        assertThat(view).isEqualTo("redirect:/users/login");
    }
}


