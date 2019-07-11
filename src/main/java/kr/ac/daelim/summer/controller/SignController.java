package kr.ac.daelim.summer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.daelim.summer.service.UserService;
import kr.ac.daelim.summer.vo.SignUpValidator;
import kr.ac.daelim.summer.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Base64;
import java.util.Map;

@Controller
@RequestMapping("/sign")
@Slf4j
public class SignController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private GoogleConnectionFactory googleConnectionFactory;

    @Autowired
    private OAuth2Parameters googleOAuth2Parameters;

    /**
     * 로그인 폼
     * @param userVO
     * @return
     */
    @GetMapping("/sign-in")
    public String signInForm(Model model
            ,@ModelAttribute UserVO userVO){
        // DAY3 - Google login
        /**
         * 구글code 발행
         */
        OAuth2Operations oauthOperations
                = googleConnectionFactory.getOAuthOperations();
        /**
         * 로그인페이지 이동 url생성
         */
        String url = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);
        model.addAttribute("google_url", url);

        // DAY3 - Google login
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
     * Social login - google redirect 처리
     * @param model
     * @return
     */
    @RequestMapping("/oauth/google")
    public String oauthGoogle(Model model
            , HttpServletRequest request
            , @ModelAttribute UserVO userVO) throws Exception{
        /**
         * Google 인중 auth_code 를 이용하여 access_token GET !!
         */
        String code = request.getParameter("code");
        log.debug(code);
        /**
         * RestTemplate을 사용하여 Access Token 및 profile을 요청
         */
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("code", code);
        parameters.add("client_id", clientIdGoogleSocial);
        parameters.add("client_secret", clientSecretGoogleSocial);
        parameters.add("redirect_uri", googleOAuth2Parameters.getRedirectUri());
        parameters.add("grant_type", "authorization_code");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/oauth2/v4/token", HttpMethod.POST, requestEntity, Map.class);
        Map<String, Object> responseMap = responseEntity.getBody();
        // id_token 라는 키에 사용자가 정보가 존재한다.
        // 받아온 결과는 JWT (Json Web Token) 형식으로 받아온다. 콤마 단위로 끊어서 첫 번째는 현 토큰에 대한 메타 정보, 두 번째는 우리가 필요한 내용이 존재한다.
        // 세번째 부분에는 위변조를 방지하기 위한 특정 알고리즘으로 암호화되어 사이닝에 사용한다.
        //Base 64로 인코딩 되어 있으므로 디코딩한다.
        /**
         * id_token에 사용자가 정보가 존재
         */
        String[] tokens = ((String)responseMap.get("id_token")).split("\\.");
        String body = new String(Base64.getDecoder().decode(tokens[1]));
        log.debug(String.valueOf(tokens.length));
        log.debug(new String(Base64.getDecoder().decode(tokens[0]), "utf-8"));
        log.debug(new String(Base64.getDecoder().decode(tokens[1]), "utf-8"));
        /**
         * Jackson을 사용하여 JSON을 Java Map 형식으로 변환
         */
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> result = mapper.readValue(body, Map.class);
        /**
         * 로그인한 사용자 정보를 UserVO 에 set
         */
        userVO.setEmail(result.get("email"));
        userVO.setName(result.get("family_name")+", "+result.get("given_name"));
        /**
         * 세션에 로그인 정보 등록
         */
        request.getSession().setAttribute("userVO", userVO);
        return "sign/sign-in-success";
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
