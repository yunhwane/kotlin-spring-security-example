package com.example.testcode.user

import com.example.testcode.user.infrastucture.persistence.UserModel
import com.example.testcode.user.infrastucture.repository.UserJpaRepository
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor


@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserRepositoryTest(val userJpaRepository: UserJpaRepository): ShouldSpec({

    val savedUserModel = userJpaRepository.save(createSaveStubUserModel())

    context("유저 등록 테스트") {
        should("유저를 저장한다.") {
            saveShouldBe(savedUserModel)
        }
    }
    context("유저 조회 테스트") {
        should("이메일로 유저를 조회한다") {
            val foundUserModel = userJpaRepository.findByEmail("email")
            findShouldBe(foundUserModel)
        }
    }
    context("userId로 유저를 조회한다") {
        should("userId로 유저를 조회한다") {
            val foundUserModel = userJpaRepository.findUserModelById(1L)
            findShouldBe(foundUserModel)
        }
    }
})
{
    companion object{
        fun createSaveStubUserModel() = UserModel(email = "email", password = "password", name = "name")

        fun saveShouldBe(savedUserModel: UserModel) {
            savedUserModel.id shouldBe 1L
            savedUserModel.email shouldBe "email"
            savedUserModel.password shouldBe "password"
            savedUserModel.name shouldBe "name"
        }

        fun findShouldBe(foundUserModel: UserModel?) {
            foundUserModel?.id shouldBe 1L
            foundUserModel?.email shouldBe "email"
            foundUserModel?.password shouldBe "password"
            foundUserModel?.name shouldBe "name"
        }
    }
}