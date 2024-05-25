package com.example.testcode.user

import com.example.testcode.ApiTest
import com.example.testcode.user.steps.UserLoginSteps
import com.example.testcode.user.steps.UserSaveSteps
import io.kotest.matchers.shouldBe

class UserApiTest: ApiTest(){
    init {
        describe("유저 생성 api test") {
            context("사용자가 유저 생성 api를 호출한다.") {
                it("유저를 생성한다.") {
                    val request = UserSaveSteps.유저생성모델생성()
                    val response = UserSaveSteps.유저생성요청(request)
                    response.statusCode() shouldBe 201
                }
            }
        }

        describe("유저 로그인 api test") {
            context("사용자가 유저 로그인 api를 호출한다.") {
                it("유저를 로그인한다.") {
                    val response = UserSaveSteps.유저생성요청(UserSaveSteps.유저생성모델생성())
                    response.statusCode() shouldBe 201
                    val loginResponse = UserLoginSteps.유저로그인요청("email1", "password")
                    loginResponse.statusCode() shouldBe 200
                }

                it("유저를 로그인한다. (실패)") {
                    val loginResponse = UserLoginSteps.유저로그인요청("email1", "password")
                    loginResponse.statusCode() shouldBe 401
                }
            }
        }
    }
}