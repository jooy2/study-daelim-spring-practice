package kr.ac.daelim.summer.vo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
public class SignInValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserVO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        UserVO userVO = (UserVO)o;

        // email 유효성
        if(!StringUtils.hasText(userVO.getEmail())){
            log.debug("로그인 - email 유효성 검사 실패");
            errors.rejectValue("email", "empty");
        }

        // 비밀번호 유효성
        if(!StringUtils.hasText(userVO.getPassword())){
            log.debug("로그인 - 비밀번호 유효성 검사 실패");
            errors.rejectValue("password", "empty");
        }

    }
}
