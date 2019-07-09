package kr.ac.daelim.summer.controller;

import kr.ac.daelim.summer.service.UserService;
import kr.ac.daelim.summer.vo.SignUpValidator;
import kr.ac.daelim.summer.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/sign")
public class SignController {

    @Autowired
    private UserService userService;

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
    public String signUpFormExecute(
            Model model
            , @ModelAttribute UserVO userVO
            , BindingResult bindingResult
            , HttpServletRequest request) {

        System.out.println(userVO.toString());

        SignUpValidator signUpValidator = new SignUpValidator();
        signUpValidator.validate(userVO, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute(signUpValidator);
            return "sign/sign-up";
        }

        this.userService.insertUser(userVO);

        return "sign/sign-up";
    }
}
