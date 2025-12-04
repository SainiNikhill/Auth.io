package in.nikhilsaini.authify.security;

import in.nikhilsaini.authify.repository.UserRepository;

import in.nikhilsaini.authify.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException ,IOException{
        String authHeader = request.getHeader("Authorization");

        //  ... checks if header is present and starts with bearer
        if(authHeader == null  || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request , response);
            return;
        }

        // Extract token
        String token = authHeader.substring(7);
        String userEmail = null;
        try {
            jwtUtil.extractEmail(token);
        } catch(Exception e){
            // invalid Token -> let the request continue without authentication
            filterChain.doFilter(request,response);
            return;
        }

        // ... checks if user is not authenticated yet;

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication()== null){
            // fetch user from DB
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

            // Validate Token
            if(jwtUtil.isTokenValid(token , userDetails.getUsername())) {

                // create authentication object

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( userDetails , null , userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // save auth in context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);



    }



}
