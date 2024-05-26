package com.example.testcode

import com.example.testcode.user.steps.UserLoginSteps
import com.example.testcode.user.steps.UserSaveSteps
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

object AuthSteps {
    fun 유저생성_로그인_토큰추출(): String {
        val response = UserSaveSteps.유저생성요청(UserSaveSteps.유저생성모델생성())
        response.statusCode() shouldBe 201
        val loginResponse = UserLoginSteps.유저로그인요청("email1", "password")
        loginResponse.statusCode() shouldBe 200
        loginResponse.header("Authorization") shouldNotBe null
        return loginResponse.header("Authorization").substring("Bearer ".length)
    }
}