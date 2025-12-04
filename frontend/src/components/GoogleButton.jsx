export default function GoogleButton(){
    const handleGoogleLogin = () =>{
        window.location.href = "http://localhost:8080/api/v1/oauth2/authorization/google";
    };

    return (
        <button
        type="button"
        onClick={handleGoogleLogin}
        className="w-full flex items-center justify-center gap-2 border rounded-md py-2 text-smfont-medium hover:bg-gray-50"
        >
            <span>Continue with google</span>
        </button>
    )
};
