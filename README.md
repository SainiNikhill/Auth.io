 Authio: Full-Stack Secure Authentication System

Authio is a modern, feature-rich authentication and authorization solution built for robust, real-world applications. It provides multiple secure login methods, including traditional email/password, social login (OAuth 2.0), and essential account recovery features.

✨ Features

Standard Registration & Verification: Secure sign-up with email-based OTP verification.

Secure Login: Access control using authenticated credentials.

Account Activation: Mandatory email OTP verification to move user data from a temporary store to the permanent database.

OAuth 2.0 Integration: Seamless sign-in and sign-up using Google on both login and registration pages.

JWT Security: State management and secure session handling using JSON Web Tokens (JWT).

Password Reset: Secure password recovery using a timed, email-based OTP mechanism.

Transactional Emails: Automated welcome and password reset emails via SendGrid.

💻 Tech Stack

Component

Technology

Description

Frontend

React

Building the dynamic and responsive user interfaces (Registration, Login, Dashboard).

Backend/API

(Specify your backend language/framework, e.g., Java/Spring Boot, Node.js/Express, Python/Django)

Handling all business logic, security, and data persistence.

Database

MySQL

Permanent and temporary (for unverified users) data storage.

Security

JWT and OAuth 2.0

Token-based authentication and social login integration.

Email Service

SendGrid

Reliable delivery of transactional emails (OTP, Welcome, Reset Links).

🚀 Detailed Project Flow

The core strength of Authio lies in its secure and multi-step processes.

1. Standard Registration and Email Verification

This multi-step flow ensures that only users with validated email addresses are saved in the primary database, enhancing security and data integrity.

User Registration: The user submits their name, email, and password on the registration page.

Temporary Storage: The backend immediately saves the user's data (including the hashed password) into a dedicated Temporary Table in the MySQL database.

OTP Generation & Email: The backend generates a unique, time-sensitive One-Time Password (OTP) and uses SendGrid to dispatch it to the user's provided email address.

Verification Step: The user receives the email and enters the OTP back into the Authio application.

OTP Validation: The backend validates the entered OTP against the temporary record.

Database Migration: Upon successful verification, the user record is migrated from the Temporary Table to the Main User Table.

Welcome Email: A personalized Welcome Email is immediately sent to the newly verified user via SendGrid.

Dashboard Redirection: The user is logged in (a JWT is issued) and redirected to the main Dashboard.

2. User Login

The traditional login path involves simple credential verification and JWT issuance.

Credential Submission: User enters email and password on the login page.

Credential Validation: The backend checks the credentials against the Main User Table.

JWT Issuance: If valid, a JWT is generated, signed, and returned to the client.

Dashboard Redirection: The user is redirected to the Dashboard, authenticated via the provided JWT.

3. Password Reset Flow (Using OTP)

This secure process allows users to regain access to their account without knowing the current password.

Request Reset: User submits their email on the "Forgot Password" page.

OTP Generation & Email: A new, time-sensitive OTP is generated and stored temporarily (linked to the user's ID) and sent via SendGrid.

OTP Submission: The user enters the OTP they received via email.

Verification: The backend verifies the submitted OTP.

New Password Submission: Upon successful OTP verification, the user submits and confirms their new password.

Password Update: The backend securely updates the user's password hash in the Main User Table.

4. OAuth 2.0 (Google Integration)

Authio supports Google login on both the Registration and Login pages, simplifying the sign-up and access process.

Initiation: The user clicks "Sign in with Google" on either the Login or Registration page.

Google Authentication: The client initiates the OAuth flow, redirecting the user to Google.

Token Exchange: Upon successful authentication, the client receives an authorization code/token from Google, which is sent to the Authio backend.

User Check: The backend uses the Google profile information (e.g., email) to check if the user already exists in the Main User Table.

Login/Registration:

Existing User: A standard Authio JWT is generated for the existing user.

New User: A new user entry is created in the Main User Table and a Welcome Email is sent (the verification step is skipped as the email is verified by Google).

Dashboard Redirection: The user is redirected to the Dashboard with the Authio JWT.

🛠️ Project Structure & Boilerplate Order

To ensure a clean separation of concerns and a secure application, the backend is structured using common architectural patterns (e.g., MVC/layered architecture), with data strictly passing via Data Transfer Objects (DTOs).

Boilerplate Setup Order

The following order is recommended for setting up the backend components:

Order

Component Type

File/Concept

Purpose

1

Database & Entity

User.java (or similar Entity file)

Defines the structure of the main and temp user tables in MySQL.

2

Security Config

SecurityConfig.java

Configures HTTP security, CORS, session management (stateless), and defines authentication filters.

3

JWT Utility

JwtUtil.java

Utility class for generating, validating, and extracting claims from JWTs.

4

DTOs (Data Transfer Objects)

UserRegistrationRequest.java, LoginRequest.java, UserResponse.java, etc.

Defines the exact data structures for receiving data from the frontend and sending responses back, ensuring type safety.

5

Services

AuthService.java, EmailService.java

Contains all business logic: password hashing, database migration, OTP generation/validation, JWT generation, and interaction with SendGrid.

6

OAuth Configuration

OAuth2Config.java (and custom handlers)

Configures the OAuth client details and defines the logic for processing user data after Google sign-in.

7

Controllers

AuthController.java

Exposes the REST API endpoints (e.g., /register, /login, /verify, /forgot-password) that interface with the Service layer.

⚙️ Dependencies & Configuration

Frontend (.env file)

# Backend API base URL
REACT_APP_API_URL=http://localhost:8080/api/auth

# Google OAuth Client ID
REACT_APP_GOOGLE_CLIENT_ID=YOUR_GOOGLE_CLIENT_ID


Backend (application.properties/yml)

# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/authio_db
spring.datasource.username=db_user
spring.datasource.password=db_password

# JWT Configuration
jwt.secret=YOUR_HIGHLY_SECURE_32_BYTE_SECRET_KEY
jwt.expiration=3600000 # 1 hour

# SendGrid Email Service
sendgrid.api.key=YOUR_SENDGRID_API_KEY
sendgrid.from.email=noreply@authio.com

# Google OAuth Settings
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
