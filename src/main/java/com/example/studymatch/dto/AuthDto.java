package com.example.studymatch.dto;

import lombok.Getter;
//큰 바구니: AuthDto 클래스
//이 클래스 자체는 아무런 기능이 없습니다. 그저 인증과 관련된 여러 가지
// '택배 상자(요청/응답 객체)'들을 한곳에 깔끔하게 모아두기 위한 큰 바구니 역할을 합니다.
public class AuthDto {
    //클라이언트 ➔ 서버 (Request)
    //SignupRequest (회원가입 요청): 사용자가 회원가입을 할 때 입력한 email과 password를 담아 서버로 가져옵니다.
    @Getter
    public static class SignupRequest{
        private String email;
        private String password;
    }
    //LoginRequest (로그인 요청): 사용자가 로그인을 시도할 때 입력한 email과 password를 서버로 전달합니다.
    @Getter
    public static class LoginRequest{
        private String username;
        private String password;
    }
    //서버 ➔ 클라이언트 (Response)
    @Getter
    public static class TokenResponse{
        private String token;
        public TokenResponse(String token) {
            this.token = token;
        }
    }
    //TokenResponse (토큰 반환): 로그인이 성공적으로 완료되면, 서버에서 발급한 JWT 토큰 문자열을 이 상자에 담아 프론트엔드로 보내줍니다.
    // 생성자(public TokenResponse...)가 있어서 토큰 문자열을 넣기만 하면 자동으로 상자가 포장됩니다.

}
