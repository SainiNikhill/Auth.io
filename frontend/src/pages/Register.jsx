import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
import { successToast, errorToast } from "../utils/toast";

export default function Register() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  const navigate = useNavigate();

  const handleGoogle = () => {
    window.location.href =
      import.meta.env.VITE_BACKEND_URL.replace("/auth", "")+
      "/oauth2/authorization/google";
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (form.password !== form.confirmPassword) {
      return errorToast("Passwords do not match!");
    }

    try {
      const res = await api.post("/register", form);

      // check succcess -
      if(res.data.success === false) {
        return errorToast(res.data.message || "Registration failed")
      }
      
      successToast(res.data.message);
      navigate("/verify-otp", { state: { email: form.email } });
    } catch (err) {
      errorToast(err.response?.data?.message || "Registration failed");
    }
  };

  return (
    <div
      className="min-h-screen flex justify-center items-center bg-cover text-white"
      style={{ backgroundImage: "url('/images/v1.jpeg')" }}
    >
      <div className="w-full max-w-sm bg-[#0c0b0e]  p-8 shadow rounded-xl">
        <h2 className="text-3xl font-bold text-center mb-6">Create Account</h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <label className="text-gray-400 mb-1">Fullname</label>
          <input
            type="text"
            placeholder="Full Name"
            className="w-full bg-[#1c1d21] p-3 rounded"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            required
          />
          <label className="text-gray-400">Email</label>
          <input
            type="email"
            placeholder="Email Address"
            className="w-full bg-[#1c1d21] p-3 rounded"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
            required
          />
          <label className="text-gray-400">Password</label>
          <input
            type="password"
            placeholder="Password"
            className="w-full bg-[#1c1d21] p-3 rounded"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
            required
          />
          <label className="text-gray-400">Confirm Password</label>
          <input
            type="password"
            placeholder="Confirm Password"
            className="w-full bg-[#1c1d21] p-3 rounded"
            value={form.confirmPassword}
            onChange={(e) =>
              setForm({ ...form, confirmPassword: e.target.value })
            }
            required
          />

          <button className="w-full bg-[#5a35ea] text-white py-3 rounded hover:bg-green-500 cursor-pointer">
            Register
          </button>
        </form>

        <button
          onClick={handleGoogle}
          className="w-full mt-4 outline-none py-3 rounded flex justify-center cursor-pointer items-center gap-3 text-black bg-gray-200 hover:bg-gray-800 hover:text-white"
        >
          <img
            src="https://www.svgrepo.com/show/475656/google-color.svg"
            className="w-5"
          />
          Continue with Google
        </button>

        <p className="text-center mt-5 text-md">
          Already have an account?
          <Link to="/login" className="text-blue-400  ml-1">
            Login
          </Link>
        </p>
      </div>
    </div>
  );
}
