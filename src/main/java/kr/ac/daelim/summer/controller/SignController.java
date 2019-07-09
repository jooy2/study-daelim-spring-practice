package kr.ac.daelim.summer.controller;

import kr.ac.daelim.summer.service.UserService;
import kr.ac.daelim.summer.vo.SignUpValidator;
import kr.ac.daelim.summer.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/sign")
public class SignController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    MessageSource messageSource;

    /**
     * 로그인 폼
     * @param userVO
     * @return
     */
    @GetMapping("/sign-in")
    public String signInForm(@ModelAttribute UserVO userVO) {
        return "sign/sign-in";
    }

    @PostMapping("/sign-in")
    public String signInFormExecute(
            @ModelAttribute UserVO userVO
            , Model model
            , BindingResult bindingResult
            , HttpServletRequest request) {

        UserVO loginUserVO = this.userService.selectUser(userVO);

        // 로그인 정보가 없는 경우
        if (loginUserVO == null) {
            // TODO 메시지 처리 후 로그인 폼으로 다시 이동
            model.addAttribute("message",
                    messageSource.getMessage("error.login", null, null));
            return "sign/sign-in";
        }
        else {

            // 로그인 세션 생성
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("userVO", loginUserVO);

            // 로그인 결과 화면으로 forward
            return "sign/sign-in-success";
        }
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

        // welcome mail
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(userVO.getEmail());

        msg.setSubject("회원가입이 정상 처리되었습니다.");
        msg.setText("Hello World \n Spring Boot Email");

        this.javaMailSender.send(msg);

        /**
         * Database 에 등록
         */
        this.userService.insertUser(userVO);

        // 새로고침 시 데이터 중복 등록 방지하기 위해 redirect 처리
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put("userVO", userVO);

        return "redirect:/sign/sign-up-success";
    }

    /**
     * 회원가입 결과 화면
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/sign-up-success")
    public String signUpSuccess(Model model, HttpServletRequest request) {

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        // 새로고침 한 경우 flashMap 이 null 입니다.
        if (flashMap != null) {
            model.addAttribute("userVO", flashMap.get("userVO"));
            return "sign/sign-up-success";
        }
        else {
            return "forward:/sign/sign-up";
        }
    }
}
