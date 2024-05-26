package com.example.testcode.user.steps

import com.example.testcode.AuthSteps
import io.restassured.RestAssured
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response

object UserLoadSteps {

    fun 유저조회요청(): ExtractableResponse<Response> {

        val authToken: String = AuthSteps.유저생성_로그인_토큰추출()
        val id = 1L
        return RestAssured.given().log().all()
            .header("Authorization", "Bearer $authToken")
            .`when`()
            .get("/api/v1/users/$id")
            .then().log().all()
            .extract()
    }

    fun 유저조회요청_401_토큰없음(): ExtractableResponse<Response> {
        val id = 1L
        return RestAssured.given().log().all()
            .`when`()
            .get("/api/v1/users/$id")
            .then().log().all()
            .extract()
    }
}