package com.hertz.model;

public enum Response{
    signUpSuccess,
    emailAlreadyExist,
    usernameAlreadyExist,

    logInSuccess,
    userNotFound,
    incorrectPassword,
    logOutSuccess,
    profileUpdateSuccess,
    profileUpdateFailed,
    passwordUpdateSuccess,
    passwordUpdateFailed,
    InvalidRequest,
    USER_ALREADY_EXIST,
}

