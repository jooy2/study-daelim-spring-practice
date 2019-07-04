package kr.ac.daelim.summer.service;

import kr.ac.daelim.summer.vo.UserVO;

public interface UserService {

    /**
     * 회원가입에 필요한 데이터 등록 처리
     * @param userVO
     */
    void insertUser(UserVO userVO);

    /**
     * 로그인시 회원정보 조회
     * @param userVO
     * @return
     */
    UserVO selectUser(UserVO userVO);
}
