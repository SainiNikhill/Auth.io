import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import logo from "/images/Authio.png"
import ProfileDropdown from "./ProfileDropdown";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <nav className="relative  w-full  items-center">

      <div className="absolute left-1/2 transform -translate-x-1/2  bg-white/30 mt-2 rounded-lg  py-2 px-2  w-[30%] flex items-center gap-20">
      <div className=" w-[100px] rounded-xl overflow-hidden">
        <img src={logo} alt=""  className="object-cover"/>
      </div>
      <div className="flex justify-end gap-8 text-lg text-white font-medium ">
        <a href="#" className="hover:underline">Documentation</a>
        <a href="#" className="hover:underline">GitHub</a>
        <a href="" className="hover:underline ">Linkedin</a>
      </div>
      
      </div>
      <div className="flex items-center justify-end p-2 ">
        <ProfileDropdown />
      </div>
    
    </nav>
  );
}
