import Navbar from "../components/Navbar";
import { useAuth } from "../context/AuthContext";


export default function Dashboard() {
  const { user } = useAuth();

  return (
    <div className="min-h-screen  bg-cover bg-center font-['Neue_Montreal']"
    style={{backgroundImage: "url('/images/v6.jpg')"}}>
      <Navbar />

      <div className="flex  items-center justify-center gap-10 pt-60">
        <h1 className="text-9xl text-gray-200 font-bold">Welcome</h1>
        <h1 className="text-9xl text-gray-300 font-light" >{user?.name}</h1>

      </div>
    </div>
  );
}
