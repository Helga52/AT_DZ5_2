package ru.netology.javaqa.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.javaqa.data.DataGenerator.*;
import static ru.netology.javaqa.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.javaqa.data.DataGenerator.Registration.getUser;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("h2")
                .shouldBe(Condition.visible, Duration.ofSeconds(2))
                .shouldHave(exactText("Личный кабинет"));
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id ='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(4))
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id ='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(4))
                .shouldHave(exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id ='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(4))
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id ='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(4))
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }
}