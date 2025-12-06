# Authio – Secure Authentication System

Authio is a production-ready, full-stack authentication system designed with real-world security and developer ergonomics in mind. It implements a multi-step registration flow that uses a temporary user table and SendGrid OTP email verification, so only verified users are migrated into the main user table. Authentication is powered by JWT (access + refresh tokens) and the project is organized for clarity and easy deployment.

---

## 🚀 Features

- JWT Authentication (Access + Refresh tokens)  
- Email verification using SendGrid (OTP-based)  
- Temporary user storage before verification (temp_user)  
- OAuth2-ready architecture (Google/GitHub integration ready)  
- MySQL database with Spring Data JPA  
- Environment-based configuration (12-factor friendly)  
- Clean, modular project structure suitable for production

---

## 🏗️ Project Structure

```
authio-backend/
│── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── in/nikhilsaini/authify/
│   │   │       ├── controller/        # REST Controllers (API endpoints)
│   │   │       ├── service/           # Service interfaces
│   │   │       ├── service/impl/      # Service implementations
│   │   │       ├── config/            # Security, JWT, CORS configs
│   │   │       ├── entity/            # JPA Entities (User, TempUser, Role...)
│   │   │       ├── repository/        # Spring Data JPA repositories
│   │   │       ├── dto/               # Request / Response DTOs
│   │   │       └── AuthioApplication.java
│   │   └── resources/
│   │       ├── application.properties  # local config
│   │       └── static/ (optional)
│   └── test/
│
├── .gitignore
├── Dockerfile (optional)
├── README.md
└── pom.xml
```

---

## 🛠️ Tech Stack

**Backend**
- Java 17  
- Spring Boot  
- Spring Security + JWT  
- Spring Data JPA (Hibernate)  
- MySQL

**Email**
- SendGrid (OTP delivery)

**Build & Deployment**
- Maven
- Docker (optional)
- Host on Render / Railway / Heroku / any cloud provider
- but free tier is not good and it also doesnt support raw javaMailSender (blocks SMTP direct and PORT 587) 

---

## Signup & Verification Flow

1. User submits the registration form.  
2. A temporary record is created in the `temp_user` table.  
3. An OTP is sent to the user’s email using SendGrid.  
4. User verifies the OTP.  
5. Data is migrated from `temp_user` to the main `user` table.  
6. User can login and receive JWT tokens (access + refresh).

---

## ⚙️ Environment Variables

Add these to your environment or `.env` file (used by your deployment platform):

```
DB_URL=jdbc:mysql://<host>:<port>/<database>?useSSL=false&...
DB_USERNAME=
DB_PASSWORD=

MAIL_USERNAME=
MAIL_PASSWORD=
SENDGRID_API_KEY=

JWT_SECRET=
JWT_EXPIRATION_MS=
REFRESH_TOKEN_SECRET=
REFRESH_TOKEN_EXPIRATION_MS=
```

---

##  Run Locally

1. Clone the repository
```bash
git clone <your-repo-url>
cd authio-backend
```

2. Configure environment variables (export or use a .env loader).

3. Run with Maven:
```bash
mvn clean install
mvn spring-boot:run
```

Or build a jar and run:
```bash
mvn package
java -jar target/authio-backend-0.0.1-SNAPSHOT.jar
```

---

## 🧪 Tests

Run unit tests with:
```bash
mvn test
```

---

## 📦 Docker (optional)

Example Dockerfile steps (outline):
1. Build with Maven
2. Use a slim OpenJDK base image
3. Copy the built jar into the image and run it

---

##  Common API Endpoints (example)

- `POST /api/v1/auth/register` — Register (creates temp_user and sends OTP)  
- `POST /api/v1/auth/verify` — Verify OTP and migrate user to main table  
- `POST /api/v1/auth/login` — Login (returns JWT access + refresh token)  
- `POST /api/v1/auth/refresh` — Refresh access token using refresh token  
- `POST /api/v1/auth/logout` — Invalidate refresh token / logout

(Adapt paths/names to match your implementation.)

---

## 🔒 Security Notes

- Store secrets (JWT secret, DB credentials, SendGrid API key) in secure environment vars, **never** commit them to source control.  
- Use HTTPS in production and set secure cookie flags if you use cookies.  
- Rotate tokens and secrets periodically.

---


## Further Improvements

- Add email verification expiry and retry limits.  
- Implement rate-limiting on auth endpoints.  
- Add 2FA (TOTP) as an optional second factor.  
- Add frontend sample (React) to demonstrate flows.  
- Add CI (GitHub Actions) and automated deployments.

---

## 🧩  Authio – Project Workflow

A clear, structured explanation of how Authio handles registration, OTP verification, authentication, and token management.

---

##  1. Registration Workflow

```
User → Submit Registration Form
        ↓
 Validate Input (email, password)
        ↓
Store User in TempUser Table
        ↓
 Generate OTP
        ↓
Send OTP via SendGrid
```

### **Steps**
1. User enters email, password, and profile details.
2. Backend validates input.
3. A temporary user record is inserted into `temp_user`.
4. A 6‑digit OTP is generated.
5. OTP is emailed to the user using SendGrid.

---

##  2. OTP Verification Workflow

```
User → Enter OTP
        ↓
Validate OTP (match + expiry)
        ↓
   If valid:
Move data from TempUser → User Table
        ↓
Delete temp entry
```

### **Steps**
- Check OTP correctness.
- Check if OTP is expired.
- If valid, migrate user to the main `user` table.
- Remove temporary entry.

---

##  3. Login & Token Workflow

```
User → Login Request
        ↓
Validate Credentials
        ↓
Generate Access Token + Refresh Token
        ↓
Return Tokens to Client
```

### **Steps**
- Validate email + password.
- Issue:
  - **Access Token** (short-lived)
  - **Refresh Token** (long-lived)
- Client uses tokens for further authenticated requests.

---

##  4. Token Refresh Workflow

```
Access Token Expired
        ↓
Client Sends Refresh Token
        ↓
Validate Refresh Token
        ↓
Issue New Access Token
```

### **Steps**
- User sends refresh token to `/refresh`.
- Backend validates it.
- Backend issues a new access token without requiring login again.

---

##  5. Logout Workflow

```
User → Logout
        ↓
Invalidate Refresh Token
        ↓
Access Token Naturally Expires
```

### **Steps**
- Refresh token is deleted/invalidated.
- Access token becomes unusable after expiry.

---

##  6. Error Handling Workflow

```
Invalid Email → 400
User Exists → 409
Invalid OTP → 400
Expired OTP → 410
Wrong Password → 401
Invalid JWT → 403
Expired JWT → 401
```

---

##  7. Deployment Workflow (Render / Railway)

```
Setup Environment Variables
        ↓
Add SendGrid API Key
        ↓
Connect MySQL Database
        ↓
Deploy Spring Boot App
        ↓
Configure CORS + Base URL
```

---

## 📌 Summary Diagram

```
Signup
  ↓
TempUser Created → OTP Sent
  ↓
Verify OTP → Migrate to User Table
  ↓
Login → Issue JWT Tokens
  ↓
Access Protected APIs
  ↓
Refresh Token → New Access Token
```

---

This workflow clearly explains every step of Authio’s authentication lifecycle.

## 1. Dependencies (pom.xml) + User Entity

---

## 📌 Step 1: Add Dependencies (pom.xml)

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>in.nikhilsaini</groupId>
    <artifactId>authio-backend</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.0</version>
    </parent>

    <properties>
        <java.version>17</java.version>
        <jjwt.version>0.11.5</jjwt.version>
    </properties>

    <dependencies>

        <!-- Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- JPA + MySQL -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Spring Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!-- OAuth2 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-client</artifactId>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

    </dependencies>

</project>
```

---

## 📌 Step 2: Create User Entity

Create the file:  
`src/main/java/in/nikhilsaini/authify/entity/User.java`

```java
package in.nikhilsaini.authify.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    private String password;

    // LOCAL, GOOGLE, GITHUB, etc.
    private String provider = "LOCAL";

    private boolean emailVerified = false;

    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_name")
    private Set<String> roles = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```
## 2. Spring Security Configuration (Spring Boot 3 / Security 6)

This file contains a clean and production-ready Spring Security configuration using the new **SecurityFilterChain** approach.

---

## 📌 Folder Structure for This Step

```
src/main/java/in/nikhilsaini/authify/config/SecurityConfig.java
src/main/java/in/nikhilsaini/authify/security/CustomUserDetailsService.java
src/main/java/in/nikhilsaini/authify/security/PasswordEncoderConfig.java
```

---

#  1. Password Encoder Configuration

Create file:  
`src/main/java/in/nikhilsaini/authify/security/PasswordEncoderConfig.java`

```java
package in.nikhilsaini.authify.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

#  2. CustomUserDetailsService

This tells Spring Security how to load users from the database.

Create file:  
`src/main/java/in/nikhilsaini/authify/security/CustomUserDetailsService.java`

```java
package in.nikhilsaini.authify.security;

import in.nikhilsaini.authify.entity.User;
import in.nikhilsaini.authify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}
```

---

#  3. Spring Security Configuration (Main File)

Create file:  
`src/main/java/in/nikhilsaini/authify/config/SecurityConfig.java`

```java
package in.nikhilsaini.authify.config;

import in.nikhilsaini.authify.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/oauth2/**",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
```
---

## 3. JWT Implementation (JWT Utils, Token Provider, Filter, Integration)

This step provides a complete JWT setup compatible with **Spring Boot 3 / Spring Security 6**.

---

# 📁 Folder Structure for JWT

```
src/main/java/in/nikhilsaini/authify/security/jwt/JwtUtils.java
src/main/java/in/nikhilsaini/authify/security/jwt/JwtAuthenticationFilter.java
src/main/java/in/nikhilsaini/authify/security/jwt/JwtTokenProvider.java
```

---

#  1. JwtTokenProvider (Generate + Validate Tokens)

Create file:  
`src/main/java/in/nikhilsaini/authify/security/jwt/JwtTokenProvider.java`

```java
package in.nikhilsaini.authify.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (JwtException e) {
            System.out.println("Invalid token");
        }
        return false;
    }
}
```

---

#  2. JwtUtils (Extract Bearer Token)

Create file:  
`src/main/java/in/nikhilsaini/authify/security/jwt/JwtUtils.java`

```java
package in.nikhilsaini.authify.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    public String extractJwtFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
```

---

#  3. JwtAuthenticationFilter (Main JWT Filter)

Create file:  
`src/main/java/in/nikhilsaini/authify/security/jwt/JwtAuthenticationFilter.java`

```java
package in.nikhilsaini.authify.security.jwt;

import in.nikhilsaini.authify.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

    private final JwtUtils jwtUtils;
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = jwtUtils.extractJwtFromRequest(request);

            if (token != null && tokenProvider.isTokenValid(token)) {

                String email = tokenProvider.extractEmail(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            System.out.println("JWT FILTER ERROR: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
```

---

#  4. Add JWT Filter to SecurityConfig

Modify your existing `SecurityConfig`:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/v1/auth/**", "/oauth2/**", "/error").permitAll()
                    .anyRequest().authenticated()
            )

            .authenticationProvider(authenticationProvider())

            // ADD THIS LINE
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```

Make sure to inject the filter:

```java
private final JwtAuthenticationFilter jwtAuthenticationFilter;
```

---

# 📌 5. Add JWT Properties to application.properties

```
jwt.secret=YOUR256BITSECRETKEYHERE1234567890123456
jwt.expiration=86400000   # 24 hours in ms
```

---

## 4. OAuth2 Login Implementation (Google Login)

This step adds **Google OAuth2 authentication** to Authio.  
Users can log in using Google, and the system will automatically create or update their account in your database.

---

# 📁 Folder Structure for OAuth2

```
src/main/java/in/nikhilsaini/authify/security/oauth/CustomOAuth2User.java
src/main/java/in/nikhilsaini/authify/security/oauth/CustomOAuth2UserService.java
src/main/java/in/nikhilsaini/authify/security/oauth/OAuth2SuccessHandler.java
```

---

#  1. CustomOAuth2User (Wrapper for OAuth Attributes)

Create file:  
`src/main/java/in/nikhilsaini/authify/security/oauth/CustomOAuth2User.java`

```java
package in.nikhilsaini.authify.security.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;

    public CustomOAuth2User(OAuth2User oauth2User) {
        this.oauth2User = oauth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    public String getPicture() {
        return oauth2User.getAttribute("picture");
    }
}
```

---

#  2. CustomOAuth2UserService (Load + Save OAuth Users)

Create file:  
`src/main/java/in/nikhilsaini/authify/security/oauth/CustomOAuth2UserService.java`

```java
package in.nikhilsaini.authify.security.oauth;

import in.nikhilsaini.authify.entity.User;
import in.nikhilsaini.authify.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User user = super.loadUser(request);

        CustomOAuth2User oAuth2User = new CustomOAuth2User(user);
        String email = oAuth2User.getEmail();

        // If user doesn't exist → create new
        User existing = userRepository.findByEmail(email).orElse(null);

        if (existing == null) {
            User newUser = User.builder()
                    .fullName(oAuth2User.getName())
                    .email(email)
                    .provider("GOOGLE")
                    .emailVerified(true)
                    .enabled(true)
                    .roles(Set.of("USER"))
                    .build();

            userRepository.save(newUser);
        }

        return oAuth2User;
    }
}
```

---

#  3. OAuth2 Success Handler (Redirect + JWT Issue Option)

Create file:  
`src/main/java/in/nikhilsaini/authify/security/oauth/OAuth2SuccessHandler.java`

```java
package in.nikhilsaini.authify.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        // TODO: Optional — Generate JWT and redirect with token
        response.sendRedirect("http://localhost:3000/auth/success");
    }
}
```

---

#  4. Update SecurityConfig for OAuth2 Login

Modify your `SecurityConfig.java`:

Add required injections:

```java
private final CustomOAuth2UserService customOAuth2UserService;
private final OAuth2SuccessHandler oAuth2SuccessHandler;
```

Update security filter chain:

```java
.oauth2Login(oauth -> oauth
        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
        .successHandler(oAuth2SuccessHandler)
)
```

Full example snippet:

```java
.authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/v1/auth/**", "/oauth2/**", "/error").permitAll()
        .anyRequest().authenticated()
)
.oauth2Login(oauth -> oauth
        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
        .successHandler(oAuth2SuccessHandler)
)
```

---

#  5. Add OAuth2 Properties (application.properties)

```
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_SECRET
spring.security.oauth2.client.registration.google.scope=email,profile
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google
```

---
## 5. Auth Service + Auth Controller (Register, OTP Verify, Login, Refresh)

This step wires everything together:

- `TempUser` entity (for pre-verification users)  
- `TempUserRepository`  
- DTOs for auth APIs  
- `AuthService` + `AuthServiceImpl`  
- `AuthController` with main endpoints  

---

## 📁 Folder Structure for This Step

```
src/main/java/in/nikhilsaini/authify/entity/TempUser.java
src/main/java/in/nikhilsaini/authify/repository/TempUserRepository.java

src/main/java/in/nikhilsaini/authify/dto/RegisterRequest.java
src/main/java/in/nikhilsaini/authify/dto/LoginRequest.java
src/main/java/in/nikhilsaini/authify/dto/VerifyOtpRequest.java
src/main/java/in/nikhilsaini/authify/dto/AuthResponse.java

src/main/java/in/nikhilsaini/authify/service/AuthService.java
src/main/java/in/nikhilsaini/authify/service/impl/AuthServiceImpl.java

src/main/java/in/nikhilsaini/authify/controller/AuthController.java
```

---

#  1. TempUser Entity

Create file:  
`src/main/java/in/nikhilsaini/authify/entity/TempUser.java`

```java
package in.nikhilsaini.authify.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "temp_users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotBlank
    private String password;

    private String otpCode;

    private LocalDateTime otpExpiresAt;

    private int otpAttempts;

    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

---

#  2. TempUserRepository

Create file:  
`src/main/java/in/nikhilsaini/authify/repository/TempUserRepository.java`

```java
package in.nikhilsaini.authify.repository;

import in.nikhilsaini.authify.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempUserRepository extends JpaRepository<TempUser, Long> {

    Optional<TempUser> findByEmail(String email);

    void deleteByEmail(String email);
}
```

---

#  3. DTOs for Auth Endpoints

Create folder:  
`src/main/java/in/nikhilsaini/authify/dto/`

## 3.1 RegisterRequest

```java
package in.nikhilsaini.authify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
```

## 3.2 LoginRequest

```java
package in.nikhilsaini.authify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
```

## 3.3 VerifyOtpRequest

```java
package in.nikhilsaini.authify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otp;
}
```

## 3.4 AuthResponse

```java
package in.nikhilsaini.authify.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
}
```

---

#  4. AuthService Interface

Create file:  
`src/main/java/in/nikhilsaini/authify/service/AuthService.java`

```java
package in.nikhilsaini.authify.service;

import in.nikhilsaini.authify.dto.*;

public interface AuthService {

    void register(RegisterRequest request);

    void verifyOtp(VerifyOtpRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken);
}
```

---

#  5. AuthServiceImpl (Core Auth Logic)

Create file:  
`src/main/java/in/nikhilsaini/authify/service/impl/AuthServiceImpl.java`

```java
package in.nikhilsaini.authify.service.impl;

import in.nikhilsaini.authify.dto.*;
import in.nikhilsaini.authify.entity.TempUser;
import in.nikhilsaini.authify.entity.User;
import in.nikhilsaini.authify.repository.TempUserRepository;
import in.nikhilsaini.authify.repository.UserRepository;
import in.nikhilsaini.authify.security.jwt.JwtTokenProvider;
import in.nikhilsaini.authify.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TempUserRepository tempUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        TempUser tempUser = tempUserRepository.findByEmail(request.getEmail())
                .orElse(TempUser.builder()
                        .email(request.getEmail())
                        .build());

        tempUser.setFullName(request.getFullName());
        tempUser.setPassword(passwordEncoder.encode(request.getPassword()));

        String otp = generateOtp();
        tempUser.setOtpCode(otp);
        tempUser.setOtpExpiresAt(LocalDateTime.now().plusMinutes(10));
        tempUser.setOtpAttempts(0);

        tempUserRepository.save(tempUser);

        // TODO: integrate your EmailService to send OTP
        System.out.println("OTP for " + tempUser.getEmail() + " is: " + otp);
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {

        TempUser tempUser = tempUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("No registration found for this email"));

        if (tempUser.getOtpExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!tempUser.getOtpCode().equals(request.getOtp())) {
            tempUser.setOtpAttempts(tempUser.getOtpAttempts() + 1);
            tempUserRepository.save(tempUser);
            throw new RuntimeException("Invalid OTP");
        }

        User user = User.builder()
                .fullName(tempUser.getFullName())
                .email(tempUser.getEmail())
                .password(tempUser.getPassword())
                .provider("LOCAL")
                .emailVerified(true)
                .enabled(true)
                .roles(Set.of("USER"))
                .build();

        userRepository.save(user);
        tempUserRepository.deleteByEmail(tempUser.getEmail());
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String accessToken = jwtTokenProvider.generateToken(request.getEmail());

        // For now simple refresh token = another JWT with longer expiry (you can separate config)
        String refreshToken = jwtTokenProvider.generateToken(request.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {

        if (!jwtTokenProvider.isTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = jwtTokenProvider.extractEmail(refreshToken);
        String newAccessToken = jwtTokenProvider.generateToken(email);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    private String generateOtp() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }
}
```

---

#  6. AuthController

Create file:  
`src/main/java/in/nikhilsaini/authify/controller/AuthController.java`

```java
package in.nikhilsaini.authify.controller;

import in.nikhilsaini.authify.dto.*;
import in.nikhilsaini.authify.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("OTP sent to your email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        authService.verifyOtp(request);
        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
```

---

## 6.  Email Service (SendGrid OTP Delivery)

This step adds real OTP email sending using **SendGrid**, completing the registration and verification workflow.

---

# 📁 Folder Structure

```
src/main/java/in/nikhilsaini/authify/service/EmailService.java
src/main/java/in/nikhilsaini/authify/service/impl/EmailServiceImpl.java
src/main/resources/application.properties
```

---

#  1. EmailService Interface

Create file:  
`src/main/java/in/nikhilsaini/authify/service/EmailService.java`

```java
package in.nikhilsaini.authify.service;

public interface EmailService {
    void sendVerificationOtp(String toEmail, String otp);
}
```

---

#  2. EmailServiceImpl (SendGrid Implementation)

Create file:  
`src/main/java/in/nikhilsaini/authify/service/impl/EmailServiceImpl.java`

```java
package in.nikhilsaini.authify.service.impl;

import com.sendgrid.*;
import in.nikhilsaini.authify.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${mail.from}")
    private String fromEmail;

    @Override
    public void sendVerificationOtp(String toEmail, String otp) {

        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        String subject = "Your Authio Verification OTP";

        String body = "<h3>Your OTP Code</h3>"
                + "<p>Use this OTP to verify your email:</p>"
                + "<h2>" + otp + "</h2>"
                + "<p>This OTP is valid for 10 minutes.</p>";

        Content content = new Content("text/html", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            System.out.println("Email sent! Status = " + response.getStatusCode());

        } catch (IOException e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }
}
```

---

#  3. Add Properties to `application.properties`

```
sendgrid.api.key=YOUR_SENDGRID_API_KEY
mail.from=your-email@example.com
```

---

#  4. Integrate Email Service in AuthServiceImpl

Inside your `AuthServiceImpl.register()` method:

```java
emailService.sendVerificationOtp(tempUser.getEmail(), otp);
```

Inject the service:

```java
private final EmailService emailService;
```

---

## 7. Global Exception Handling (ControllerAdvice + Custom Error Response)

This step provides clean, production-ready **global exception handling** using:

- `ApiException` (custom exception)
- `ErrorResponse` (standardized JSON response format)
- `GlobalExceptionHandler` (`@ControllerAdvice`)

---

# 📁 Folder Structure

```
src/main/java/in/nikhilsaini/authify/exception/ApiException.java
src/main/java/in/nikhilsaini/authify/exception/ErrorResponse.java
src/main/java/in/nikhilsaini/authify/exception/GlobalExceptionHandler.java
```

---

#  1. ErrorResponse (Standard JSON Format)

Create file:  
`src/main/java/in/nikhilsaini/authify/exception/ErrorResponse.java`

```java
package in.nikhilsaini.authify.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
```

---

#  2. ApiException (Custom Runtime Exception)

Create file:  
`src/main/java/in/nikhilsaini/authify/exception/ApiException.java`

```java
package in.nikhilsaini.authify.exception;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}
```

---

#  3. GlobalExceptionHandler (`@ControllerAdvice`)

Create file:  
`src/main/java/in/nikhilsaini/authify/exception/GlobalExceptionHandler.java`

```java
package in.nikhilsaini.authify.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildError(
            HttpStatus status, String message, HttpServletRequest req) {

        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(req.getRequestURI())
                .build();
    }

    // Handle ApiException (custom)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(
            ApiException ex, HttpServletRequest req) {

        ErrorResponse error = buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                req
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Handle validation errors (DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        ErrorResponse error = buildError(
                HttpStatus.BAD_REQUEST,
                message,
                req
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Fallback for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex, HttpServletRequest req) {

        ErrorResponse error = buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                req
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

---

#  4. How to use ApiException

Example inside `AuthServiceImpl`:

```java
if (userRepository.findByEmail(request.getEmail()).isPresent()) {
    throw new ApiException("Email already exists");
}
```

---


















## 📝 License
NIKHIL SAINI

```
MIT License
Copyright (c) 2025 <Your Name>
```


