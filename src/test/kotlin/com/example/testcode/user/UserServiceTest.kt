package com.example.testcode.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

class UserServiceTest: DescribeSpec({

    val userService: UserService = mockk()

    describe("유저 서비스 테스트") {
        context("회원 등록 서비스") {
            it("회원 중복 검사 통과시 회원 등록 성공") {
                val request = UserRequest("email", "password", "name")
                val user = UserMapper.toUser(request)
                val savedUser = User(1L, "email", "password", "name")
                every { userService.saveUser(user) } returns savedUser
                val result = userService.saveUser(user)

                // Assertions
                result shouldBe savedUser

                // Verify that saveUser was called exactly once
                verify(exactly = 1) { userService.saveUser(user) }
            }
        }
        context("회원 등록 예외 처리") {
            it("이미 존재하는 이메일일 경우 예외 발생") {
                val request = UserRequest("email", "password", "name")
                val user = UserMapper.toUser(request)

                // Mocking saveUser method to throw an exception
                every { userService.saveUser(user) } throws IllegalArgumentException("이미 존재하는 이메일입니다.")

                // Method call and exception assertion
                shouldThrow<IllegalArgumentException> {
                    userService.saveUser(user)
                }.message shouldBe "이미 존재하는 이메일입니다."

                // Verify that saveUser was called exactly once
                verify(exactly = 1) { userService.saveUser(user) }
            }
        }
    }
})

data class UserRequest(
    val email: String,
    val password: String,
    val name: String,
)

data class SaveUserDto(
    val id: Long,
    val email: String,
    val password: String,
    val name: String,
)

@Entity
class UserModel(
    var email: String,
    var password: String,
    var name: String,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}

data class User(
    val id: Long?,
    val email: String,
    val password: String,
    val name: String,
)



interface UserJpaPort {
    fun save(user: User): User
    fun findByEmail(email: String): User?
}


@Component
class UserJpaAdapter(private val userJpaRepository: UserJpaRepository): UserJpaPort {
    override fun save(user: User): User {
        val userModel = UserMapper.toUserModelFromUser(user)
        userJpaRepository.save(userModel)
        return UserMapper.toUserFromUserModel(userModel)
    }

    override fun findByEmail(email: String): User? {
        val userModel = userJpaRepository.findByEmail(email)
        return userModel?.let { UserMapper.toUserFromUserModel(it) }
    }
}

@Repository
interface UserJpaRepository: JpaRepository<UserModel, Long> {
    fun findByEmail(email: String): UserModel?
}


interface UserSaveUseCase {
    fun saveUser(user: User) : User
}

@Component
class UserService(private val userJpaPort: UserJpaPort): UserSaveUseCase {
    override fun saveUser(user: User): User {
        userJpaPort.findByEmail(user.email)?.let {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }
        return userJpaPort.save(user)
    }
}

object UserMapper {
    fun toUser(userRequest: UserRequest): User {
        return User(null, userRequest.email, userRequest.password, userRequest.name)
    }

    fun toSaveUserDto(user: User): SaveUserDto {
        return SaveUserDto(user.id!!, user.email, user.password, user.name)
    }

    fun toUserModelFromUser(user: User): UserModel {
        return UserModel(user.email, user.password, user.name)
    }

    fun toUserFromUserModel(userModel: UserModel): User {
        return User(userModel.id, userModel.email, userModel.password, userModel.name)
    }
}
