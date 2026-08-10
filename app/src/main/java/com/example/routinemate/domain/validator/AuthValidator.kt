package com.example.routinemate.domain.validator

import android.util.Patterns
import javax.inject.Inject

class AuthValidator @Inject constructor() {

    // 로그인 입력값 검증
    fun validateLogin(
        email: String,
        password: String
    ): String? {

        if (email.isBlank()) {
            return "이메일을 입력해주세요."
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "올바른 이메일 형식을 입력해주세요."
        }

        if (password.isBlank()) {
            return "비밀번호를 입력해주세요."
        }

        return null
    }

    // 회원가입 입력값 검증
    fun validateSignup(
        email: String,
        password: String,
        nickname: String
    ): String? {

        if (email.isBlank()) {
            return "이메일을 입력해주세요."
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "올바른 이메일 형식을 입력해주세요."
        }

        if (password.length < 8) {
            return "비밀번호는 8자 이상 입력해주세요."
        }

        if (password.length > 64) {
            return "비밀번호는 64자 이하로 입력해주세요."
        }

        if (nickname.length < 2) {
            return "닉네임은 2자 이상 입력해주세요."
        }

        if (nickname.length > 20) {
            return "닉네임은 20자 이하로 입력해주세요."
        }

        return null
    }
}