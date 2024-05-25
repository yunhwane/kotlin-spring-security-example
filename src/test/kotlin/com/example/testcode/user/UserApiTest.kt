package com.example.testcode.user

import com.example.testcode.ApiTest
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
    }
}