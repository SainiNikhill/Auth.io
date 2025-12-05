# Authio – Secure Authentication System

Authio is a full-stack authentication system built using **Spring Boot**, **React**, **JWT**, **SendGrid**, and **MySQL**. It provides a complete user authentication workflow including registration, login, email verification, password reset, and OAuth support.

---

## 🚀 Features

* **User Registration & Login** (Spring Security + JWT)
* **Email Verification using OTP** (SendGrid API)
* **Password Reset with OTP**
* **JWT Access & Refresh Tokens**
* **Google OAuth Login**
* **Welcome Email After Verification**
* **MySQL Database (Aiven Cloud)**
* **Secure Backend Architecture**
* **Exception Handling & Validation**

---

## 🛠️ Tech Stack

### **Backend (Spring Boot)**

* Spring Boot 3+
* Spring Security
* JWT Authentication
* SendGrid Email API
* MySQL (Aiven)
* Lombok
* Java 17+

### **Frontend (React)**

* React + Vite
* Axios
* React Router
* Tailwind CSS UI

---

## 🔐 Authentication Flow

### **1. User Registration**

* User submits name, email, password.
* Backend creates a user with `UNVERIFIED` status.
* OTP is sent to user email using SendGrid.

### **2. Email Verification**

* User enters OTP.
* Backend validates OTP and updates status to `VERIFIED`.
* Sends a **Welcome Email**.

### **3. Login**

* User enters email + password.
* Server validates credentials.
* Generates:

  * **Access Token (Short-lived)**
  * **Refresh Token (Long-lived)**

### **4. Password Reset**

* User requests reset.
* Server sends OTP via SendGrid.
* User submits OTP + new password.

### **5. Google OAuth Login**

* User logs in with Google.
* If first login → creates new account.
* Issues JWT tokens.

---

## 📬 Email Service (SendGrid)

OTP and transactional emails are sent using **SendGrid API** instead of SMTP due to Render free tier restrictions.

### Benefits:

* Faster delivery
* No SMTP blocking
* Better reliability

---

## 🗄️ Database Schema (Simplified)

**User Table**

* id
* name
* email
* password
* isVerified
* createdAt
* roles

**OTP Table**

* id
* email
* otp
* expiry

---

## ⚙️ Environment Variables

```
SENDGRID_API_KEY=
APP_MAIL_FROM=
DB_URL=
DB_USERNAME=
DB_PASSWORD=
JWT_SECRET=
JWT_ACCESS_EXP=
JWT_REFRESH_EXP=
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
```

---

## 🏗️ Deployment Notes

Authio was tested on Render free tier but:

* SMTP does **not** work (blocked)
* Cold starts slow down performance

Recommended deployments:

* AWS EC2 (Best for Spring Boot)
* Railway Pro
* Oracle Cloud Always Free

---

## 📄 Future Improvements

* Multi-Factor Authentication (MFA)
* Refresh Token Rotation
* Role-Based Authorization (Admin/User)
* Rate Limiting
* Custom Domain + DKIM for email deliverability

---

## 👨‍💻 Author

**Nikhil Saini** – Full Stack Developer

---

## ⭐ Conclusion

Authio is a complete and secure authentication system suitable for production environments with minor enhancements. It showcases strong backend architecture, email verification workflow, security best practices, and cloud integration.

Feel free to expand this README with screenshots, API documentation, or deployment instructions.
