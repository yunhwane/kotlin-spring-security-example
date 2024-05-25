package com.example.testcode.user.steps

import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import java.util.*

object UserLoginSteps {

    fun 유저로그인요청(email: String, password: String): ExtractableResponse<Response> {

        val authHeader = 베이직토큰생성(email, password)

        return RestAssured.given().log().all()
            .header("Authorization", authHeader)
            .contentType("application/json")
            .`when`()
            .post("/api/v1/users/login")
            .then().log().all()
            .extract()
    }

    fun 베이직토큰생성(email: String, password: String): String {
        val credentials = "$email:$password"
        val encodedCredentials = Base64.getEncoder().encodeToString(credentials.toByteArray(Charsets.UTF_8))
        return "Basic $encodedCredentials"
    }

}