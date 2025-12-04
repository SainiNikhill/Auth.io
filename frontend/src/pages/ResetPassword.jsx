import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../services/api";
import { successToast, errorToast } from "../utils/toast";

export default function ResetPassword() {
  const { state } = useLocation();
  const navigate = useNavigate();
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await api.post("/reset-password", {
        email: state.email,
        otp,
        newPassword,
      });

      successToast(res.data.message);
      navigate("/login");
    } catch (err) {
      errorToast("Failed to reset password");
    }
  };

  return (
    <div className="min-h-screen flex justify-center items-center bg-gray-50 bg-cover"
    style={{backgroundImage:"url('/images/v9.jpg')"}}
    >
      <div className="w-full max-w-md bg-black p-8  text-white shadow rounded-xl">
        <h2 className="text-2xl font-bold text-center mb-4">Reset Password</h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            placeholder="Enter OTP"
            className="w-full bg-[#1c1d12] outline-none p-3 rounded"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="New Password"
            className="w-full bg-[#1c1d12]  outline-none p-3 rounded"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />

          <button className="w-full bg-[#6f2dc9] text-white py-3 rounded hover:bg-green-500">
            Reset Password
          </button>
        </form>
      </div>
    </div>
  );
}
