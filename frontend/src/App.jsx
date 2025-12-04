import { Routes, Route, Navigate } from "react-router-dom";

import Register from "./pages/Register";
import VerifyOtp from "./pages/OtpVerification";
import Login from "./pages/Login";
import ForgotPassword from "./pages/ForgotPassword";
import ResetPassword from "./pages/ResetPassword";
import OAuth2Redirect from "./pages/OAuth2Redirect";
import Dashboard from "./pages/Dashboard";

import ProtectedRoute from "./components/ProtectedRoutes";

import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function App() {
  return (
    
    < div className="font-['neue_montreal']">
      <ToastContainer theme="light" position="top-left" />

      <Routes>
        {/* Default redirect */}
        <Route path="/" element={<Navigate to="/login" />} />

        {/* Public pages */}
        <Route path="/register" element={<Register />} />
        <Route path="/verify-otp" element={<VerifyOtp />} />
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/oauth2/redirect" element={<OAuth2Redirect />} />

        {/* Protected pages */}
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />

        {/* 404 */}
        <Route
          path="*"
          element={
            <div className="p-10 text-center text-2xl">404 Not Found</div>
          }
        />
      </Routes>
    </div>
  );
}
