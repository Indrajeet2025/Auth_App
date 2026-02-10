import useAuth from "@/auth/store";
import toast from "react-hot-toast";

import { Navigate, Outlet } from "react-router";

const Userlayout = () => {
  const checkLogin = useAuth((state) => state.checkLogin);

  if (checkLogin()) {
    return (
      <>
        <Outlet />
      </>
    );
  } else {
    toast.error("Please login first !");
    return <Navigate to={"/login"} />;
  }
};

export default Userlayout;
