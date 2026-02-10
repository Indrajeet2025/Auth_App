import useAuth from "@/auth/store";
import { Button } from "@/components/ui/button";

import { NavLink, useNavigate } from "react-router";

function Navbar() {
  const navigate = useNavigate();
  const checkLogin = useAuth((state) => state.checkLogin);
  const user = useAuth((state) => state.user);
  const logout = useAuth((state) => state.logout);
  return (
    <nav className="bg-gray-50 dark:bg-gray-900 py-5 md:py-0  border-b border-gray-300 flex md:flex-row flex-col gap-4 md:gap-0 md:h-14 justify-around items-center left-0 z-50 w-full sticky top-0 ">
      {/* {brand} */}
      <div className="font-semibold flex justify-around items-center gap-2 ">
        <span className="inline-block text-center h-6 w-6 rounded-md bg-gradient-to-r from-primary to-primary/40">
          {"A"}
        </span>
        <span className="text-base tracking-tight">Auth app</span>
      </div>
      <div className="flex gap-4 justify-around items-center">
        {checkLogin() ? (
          <>
            {console.log(user?.email)}
            <NavLink to={"/dashboard/profile"}>{user?.name}</NavLink>

            <Button
              onClick={() => {
                logout();
                navigate("/");
              }}
              size={"sm"}
              className="cursor-pointer"
              variant={"outline"}
            >
              Logout
            </Button>

            <NavLink to={"/signup"}>
              <Button
                size={"sm"}
                className="cursor-pointer"
                variant={"outline"}
              >
                Signup
              </Button>
            </NavLink>
          </>
        ) : (
          <>
            <NavLink to={"/"}>Home</NavLink>
            <NavLink to={"/login"}>
              <Button
                size={"sm"}
                className="cursor-pointer"
                variant={"outline"}
              >
                Login
              </Button>
            </NavLink>
            <NavLink to={"/signup"}>
              <Button
                size={"sm"}
                className="cursor-pointer"
                variant={"outline"}
              >
                Signup
              </Button>
            </NavLink>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
