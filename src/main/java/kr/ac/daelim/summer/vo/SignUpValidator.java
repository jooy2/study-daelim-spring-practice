package kr.ac.daelim.summer.vo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
public class SignUpValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserVO.class.isAssignableFrom(aClass);
    }

    private boolean ifValidEmail(String email) {
        // 정규 표현식 사용
        if (true) {

        } else {

        }
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {

        UserVO userVO = (UserVO)o;

        // email 유효성
        if(!StringUtils.hasText(userVO.getEmail())){
            log.debug("회원가입 - email유효성 검사 실패");
            errors.rejectValue("email", "valid");
        }
        // TODO regex 사용한 이메일 유효성 검사로 변경


        // 비밀번호 유효성
        if(!StringUtils.hasText(userVO.getPassword())){
            log.debug("회원가입 - 바밀번호 유효성 검사 실패");
            errors.rejectValue("password", "empty");
        }
        else if(userVO.getPassword().length()<6){
            log.debug("회원가입 - 바밀번호 유효성 검사 실패");
            errors.rejectValue("password", "length");
        }

        // 이름 유효성
        if(!StringUtils.hasText(userVO.getName())){
            log.debug("회원가입 - 이름 유효성 검사 실패");
            errors.rejectValue("name", "empty");
        }

    }
}
