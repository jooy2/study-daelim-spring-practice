package kr.ac.daelim.summer.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;

@Configuration
public class SocialLogin {

    /**
     * google clientId
     */
    @Value("${social.google.clientId}")
    private String clientIdGoogleSocial;

    /**
     * clientSecret
     */
    @Value("${social.google.clientSecret}")
    private String clientSecretGoogleSocial;

    /**
     * Google 권한
     */
    @Value("${social.google.scope}")
    private String scopeGoogleSocial;

    /**
     * redirectUri
     */
    @Value("${social.google.redirectUri}")
    private String redirectUriGoogleSocial;

    /**
     * GoogleConnectionFactory Bean 설정
     * @return
     */
    @Bean
    public GoogleConnectionFactory googleConnectionFactory(){
        GoogleConnectionFactory googleConnectionFactory
                = new GoogleConnectionFactory(clientIdGoogleSocial, clientSecretGoogleSocial);

        return googleConnectionFactory;
    }

    /**
     * 인증요청시 필요한 parameter 반환
     * @return
     */
    @Bean
    public OAuth2Parameters googleOAuth2Parameters(){
        OAuth2Parameters googleOAuth2Parameters = new OAuth2Parameters();
        googleOAuth2Parameters.setScope(scopeGoogleSocial);
        googleOAuth2Parameters.setRedirectUri(redirectUriGoogleSocial);
        return googleOAuth2Parameters;
    }
}
