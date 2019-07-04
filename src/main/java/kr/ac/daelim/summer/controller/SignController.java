package kr.ac.daelim.summer.controller;

import kr.ac.daelim.summer.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sign")
public class SignController {

    /**
     * 로그인 폼
     * @param userVO
     * @return
     */
    @GetMapping("/sign-in")
    public String signInForm(@ModelAttribute UserVO userVO) {
        return "sign/sign-in";
    }

    /**
     * 회원가입폼
     * @param userVO
     * @return
     */
    @GetMapping("/sign-up")
    public String signUpForm(@ModelAttribute UserVO userVO) {
        return "sign/sign-up";
    }

    @PostMapping("/sign-up")
    public String signInFormExecute(@ModelAttribute UserVO userVO) {

        System.out.println(userVO.toString());

        return "sign/sign-up";
    }
}
