import { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function OAuth2Redirect() {
  const location = useLocation();
  const navigate = useNavigate();
  const { login } = useAuth();

  useEffect(() => {
    const params = new URLSearchParams(location.search);

    const token = params.get("token");
    const refreshToken = params.get("refreshToken");
    const name = params.get("name");
    const email = params.get("email");
    const role = params.get("role");

    if (token) {
      login({
        token,
        refreshToken,
        user: { name, email, role },
      });
      navigate("/dashboard");
    } else {
      navigate("/login");
    }
  }, []);

  return (
    <div className="h-screen flex justify-center items-center text-xl">
      Logging in with Google...
    </div>
  );
}
