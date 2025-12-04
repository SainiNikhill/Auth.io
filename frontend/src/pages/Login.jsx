import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";
import { successToast, errorToast } from "../utils/toast";

export default function Login() {
  const [form, setForm] = useState({ email: "", password: "" });
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await api.post("/login", form);

      login({
        token: res.data.token,
        refreshToken: res.data.refreshToken,
        user: res.data.user,
      });

      successToast(res.data.message);
      navigate("/dashboard");
    } catch (err) {
      errorToast(err.response?.data?.message || "Invalid credentials");
    }
  };

  const handleGoogle = () => {
    window.location.href =
      import.meta.env.VITE_BACKEND_URL.replace("/auth", "") +
      "/oauth2/authorization/google";
  };

  return (
    <div
      className="min-h-screen flex justify-center items-center bg-cover text-white"
      style={{ backgroundImage: "url('/images/v5.jpg')" }}
    >
      <div className="w-full max-w-sm p-8 shadow bg-[#0c0b0e] rounded-xl">
        <h2 className="text-4xl font-bold text-center mb-6 text-white ">
          Welcome Back!
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4 text-white">
          <label className="text-gray-400">Email</label>
          <input
            type="email"
            placeholder="Email"
            className="w-full  p-3 rounded bg-[#1c1d21] outline-none"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
            required
          />
          <label className="text-gray-400">Password</label>
          <input
            type="password"
            placeholder="Password"
            className="w-full  p-3 rounded  bg-[#1c1d21] outline-none"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
            required
          />
          <Link to="/forgot-password" className="text-blue-400 cursor-pointer">
            Forgot Password?
          </Link>

          <button className="w-full mt-3 bg-[#ac2eea] text-white py-3 rounded hover:bg-green-500 cursor-pointer">
            Login
          </button>
        </form>

        <button
          onClick={handleGoogle}
          className="w-full mt-4 bg-gray-200 py-3 rounded flex justify-center items-center gap-3  text-black hover:bg-gray-700 hover:text-white cursor-pointer"
        >
          <img
            src="https://www.svgrepo.com/show/475656/google-color.svg"
            className="w-5"
          />
          Continue with Google
        </button>

        <div className="flex justify-center mt-5 text-sm gap-2">
          <p>Don't have account? </p>
          <Link to="/register" className="text-blue-400 cursor-pointer">
            Create Account
          </Link>
        </div>
      </div>
    </div>
  );
}
