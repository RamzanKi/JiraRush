package com.javarush.jira.login.internal.sociallogin.passwordreset;

import com.javarush.jira.common.AppEvent;
import com.javarush.jira.login.User;

public record PasswordResetEvent(User user, String token) implements AppEvent {
}
