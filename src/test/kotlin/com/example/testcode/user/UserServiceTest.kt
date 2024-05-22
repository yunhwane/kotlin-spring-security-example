package com.example.testcode.user

import com.example.testcode.user.application.api.UserRequest
import com.example.testcode.user.application.mapper.UserMapper
import com.example.testcode.user.application.service.UserService
import com.example.testcode.user.core.domain.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserServiceTest: DescribeSpec({
    val userService: UserService = mockk()

    describe("유저 서비스 테스트") {
        context("회원 등록 서비스 성공 케이스") {
            it("회원 등록 성공 시 회원 도메인 반환") {
                val request = createSaveStubRequest()
                val user = UserMapper.toUser(request)
                val savedUser = createSaveStubUser()

                every { userService.execute(user) } returns savedUser
                val result = userService.execute(user)
                result shouldBe savedUser

                // Verify that saveUser was called exactly once
                verify(exactly = 1) { userService.execute(user) }
            }
        }
        context("회원 등록 서비스 실패 케이스") {
            it("이미 존재하는 이메일일 경우 예외 발생") {
                val request = createSaveStubRequest()
                val user = UserMapper.toUser(request)

                every { userService.execute(user) } throws IllegalArgumentException("이미 존재하는 이메일입니다.")

                // Method call and exception assertion
                shouldThrow<IllegalArgumentException> {
                    userService.execute(user)
                }.message shouldBe "이미 존재하는 이메일입니다."

                // Verify that saveUser was called exactly once
                verify(exactly = 1) { userService.execute(user) }
            }
        }
    }
}){
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf
    companion object {
        fun createSaveStubRequest() = UserRequest("email", "password", "name")
        fun createSaveStubUser() = User(1L, "email", "password", "name")
    }
}


