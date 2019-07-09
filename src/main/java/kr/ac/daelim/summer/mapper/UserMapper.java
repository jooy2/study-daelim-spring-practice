package kr.ac.daelim.summer.mapper;

import kr.ac.daelim.summer.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    /**
     * 회원가입
     * @param userVO
     */
    void insertUser(UserVO userVO);

    /**
     * 로그인
     */
    UserVO selectUser(UserVO userVO);
}
