import { useState, useRef , useEffect } from "react";

import { useAuth } from "../context/AuthContext";
import { successToast } from "../utils/toast";

export default function ProfileDropdown() {
    const {user,logout} = useAuth();
    const [open,setOpen]= useState(false);
    const ref = useRef(null);


    useEffect(()=>{
        function handleClickOutside(e) {
            if(ref.current && !ref.current.contains(e.target)) setOpen(false);

        }
        document.addEventListener("mousedown", handleClickOutside);
        return()=> document.removeEventListener("mousedown",handleClickOutside);
    },[]);

    if(!user) return null;

    const intials = user.name ? user.name.split(" ")
    .map((n) =>n[0])
    .join("")
    .toUpperCase()
    :"U";
    const handleLogout = () => {
        successToast("logged out successfully")
        logout();
    }

    return (
      <div className="relative " ref={ref} >
        <button
          onClick={() => setOpen((prev) => !prev)}
          className="h-15 w-15 mr-2 rounded-full bg-gray-200 flex items-center justify-center text-sm font-semibold cursor-pointer"
        >
          {intials}
        </button>
        {open && (
          <div className="absolute right-0 mt-2 w-56 bg-white  rounded-md shadow-md p-3">
            <p className="text-sm font-semibold">{user.name}</p>
            <p className="text-xs text-gray-500 mb-3">{user.email}</p>
            <button
              onClick={handleLogout}
              className="w-full text-left text-sm text-red-600 hover:bg-red-50 rounded px-2 py-1 cursor-pointer "
            >
              Logout
            </button>
          </div>
        )}
      </div>
    );
}