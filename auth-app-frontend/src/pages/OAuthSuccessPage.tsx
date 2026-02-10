import useAuth from "@/auth/store";
import { Spinner } from "@/components/ui/spinner";
import { refreshToken } from "@/services/AuthService";
import React, { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router";

const OAuthSuccessPage = () => {
  const [isRefreshing, setIsRefreshing] = useState<boolean>(false);
  const changeLocalLoginData = useAuth((state) => state.changeLocalLoginData);
  const navigate = useNavigate();
  useEffect(() => {
    async function getAccessToken() {
      if (!isRefreshing) {
        // call api of refreshToken
        setIsRefreshing(true);
        const responseLoginDate = await refreshToken();
        try {
          //login:
          changeLocalLoginData(
            responseLoginDate.accessToken,
            responseLoginDate.userDto,
            true
          );

          toast.success("Login successfull..");
          navigate("/dashboard");
        } catch (error) {
          toast.error("While login..");
          console.error(error);
        } finally {
          setIsRefreshing(false);
        }
      }
    }
    getAccessToken();
  }, []);

  return (
    <div className="p-10 flex flex-col gap-3 justify-center items-center">
      <Spinner />
      <h1 className="text-2xl font-semibold">Please wait..</h1>
    </div>
  );
};

export default OAuthSuccessPage;
