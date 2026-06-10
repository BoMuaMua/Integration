package com.example.signin.service;

import com.example.signin.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface SigninService {
    ResponseResult createSignIn(SigninMessage signinMessage) throws JsonProcessingException;
    
    ResponseResult signinList();

    ResponseResult getDetail(String activityName);

    ResponseResult stopSignIn(ActivityName activityName);

    ResponseResult delateActivity(Activity activityName);

    ResponseResult updateActivity(upActivity activity);

    ResponseResult SignIn(UserSignIn userSignIn, HttpServletRequest request) throws JsonProcessingException;

    ResponseResult changeUserSignInStatus1(SignInChange signInChange);

    ResponseResult changeUserSignInStatus0(SignInChange signInChange);

    ResponseResult getStringForQRCode(String activityName);

    ResponseResult userList(HttpServletRequest request);

    ResponseResult getSignInStatus(String activityName, HttpServletRequest request);

    ResponseResult statistics(HttpServletResponse response) throws IOException;
}
