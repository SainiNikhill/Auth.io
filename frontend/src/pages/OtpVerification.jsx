import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";
import { successToast, errorToast } from "../utils/toast";

export default function OtpVerification() {
  const [otp, setOtp] = useState("");
  const navigate = useNavigate();
  const { state } = useLocation();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await api.post("/verify-otp", {
        email: state.email,
        otp,
      });

      login({
        token: res.data.token,
        refreshToken: res.data.refreshToken,
        user: res.data.user,
      });

      successToast(res.data.message);
      navigate("/dashboard");
    } catch (err) {
      errorToast(err.response?.data?.message || "Invalid OTP");
    }
  };

  return (
    <div className="min-h-screen flex justify-center items-center bg-cover"
    style={{backgroundImage:"url('/images/v7.jpg')"}}>

      <div className="w-full max-w-sm bg-black shadow-blue-800 p-8  text-white shadow rounded-xl">
        <h2 className="text-3xl font-bold text-center mb-4">Verify Email</h2>
        <p className="text-center  text-gray-400 mb-6">
          Enter OTP sent to <span className="font-semibold">{state.email}</span>
        </p>

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            maxLength="6"
            type="text"
            placeholder="Enter OTP"
            className="w-full outline-none p-3 rounded text-center bg-[#1c1d12] text-xl tracking-widest"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            required
          />

          <button className="w-full bg-[#211ae0] cursor-pointer hover:bg-[#13db92] text-white py-3 rounded hover:bg-">
            Verify OTP
          </button>
        </form>
      </div>
    </div>
  );
}
