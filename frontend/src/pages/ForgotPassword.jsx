import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import { successToast, errorToast } from "../utils/toast";

export default function ForgotPassword() {
  const [email, setEmail] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await api.post("/forgot-password", { email });
      successToast(res.data.message);
      navigate("/reset-password", { state: { email } });
    } catch (err) {
      errorToast("Failed to send OTP");
    }
  };

  return (
    <div className="min-h-screen flex justify-center items-center bg-cover"
    style={{backgroundImage:"url('/images/v8.jpg')"}}>
      <div className="w-full max-w-sm bg-[#0c0b0e] p-8 shadow rounded-xl text-white">
        <h2 className="text-2xl font-bold text-center mb-4">
          Forgot Password?
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="email"
            placeholder="Enter your email"
            className="w-full outline-none bg-[#1c1d12] p-3 rounded"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <button className="w-full bg-[#8a4fff] text-white py-3 rounded cursor-pointer hover:bg-green-400">
            Send OTP
          </button>
        </form>
      </div>
    </div>
  );
}
