package in.nikhilsaini.authify.oauth;

import in.nikhilsaini.authify.entity.User;
import in.nikhilsaini.authify.enums.AuthProvider;
import in.nikhilsaini.authify.enums.Role;
import in.nikhilsaini.authify.repository.UserRepository;
import in.nikhilsaini.authify.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler  implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response , Authentication authentication) throws IOException
    {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");
        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null){
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setRole(Role.USER);
            user.setEmailVerified(true);
            user.setProvider(AuthProvider.GOOGLE);
            userRepository.save(user);



        }
        String role = user.getRole() !=null ? user.getRole().name(): Role.USER.name();

        String token = jwtUtil.generateToken(email,role);
        String refreshToken = jwtUtil.generateRefreshToken(email);


        // URL encode fields for safety
        String redirectURL = "http://localhost:5173/oauth2/redirect"
                + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&refreshToken=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8)
                + "&name=" + URLEncoder.encode(user.getName(), StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8)
                + "&role=" + URLEncoder.encode(role, StandardCharsets.UTF_8);

        response.sendRedirect(redirectURL);
    }
}
