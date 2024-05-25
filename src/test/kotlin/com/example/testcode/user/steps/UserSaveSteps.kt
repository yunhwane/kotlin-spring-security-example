package com.example.testcode.user.steps

import com.example.testcode.user.application.api.UserRequest
import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response

object UserSaveSteps {

    fun 유저생성요청(request: UserRequest): ExtractableResponse<Response> {
        return RestAssured.given().log().all()
            .contentType("application/json")
            .body(request)
            .`when`()
            .post("/api/v1/users/create")
            .then().log().all()
            .extract()
    }

    fun 유저생성모델생성() = UserRequest("email1", "password", "name")
}