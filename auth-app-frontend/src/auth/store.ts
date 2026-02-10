import type LoginData from "@/models/LoginData";
import type LoginRequestData from "@/models/LoginRequestData";
import type User from "@/models/User";
import { loginUser, logoutUser } from "@/services/AuthService";
import { create } from "zustand";
import { persist } from "zustand/middleware";

const LOCAL_KEY = "app_state";

type AuthStatus = "idle" | "authenticating" | "authenticated" | "anonymous";

// global authstate:

type AuthState = {
  accessToken: string | null;
  user: User | null;
  authStatus: boolean;
  authLoading: boolean;
  login: (loginData: LoginData) => Promise<LoginRequestData>;
  logout: (silent?: boolean) => void;
  checkLogin: () => boolean;

  changeLocalLoginData: (
    accessToken: string,
    user: User,
    authStatus: boolean
  ) => void;
};

// main logic for global state
const useAuth = create<AuthState>()(
  persist(
    (set, get) => ({
      accessToken: null,
      user: null,
      authStatus: false,
      authLoading: false,
      login: async (loginData) => {
        console.log("started login..");
        set({
          authLoading: true,
        });
        try {
          const loginResponseData = await loginUser(loginData);
          console.log(loginResponseData);
          set({
            accessToken: loginResponseData.accessToken,
            user: loginResponseData.userDto,
            authStatus: true,
          });
          return loginResponseData;
        } catch (error) {
          console.log("Galat error");
          console.log(error);
          throw error;
        } finally {
          set({
            authLoading: false,
          });
        }
      },
      logout: async (silent = false) => {
        try {
          // ask chatgpt for what was this for what it means by silent logout
          // if (!silent) {
          //   await logoutUser();
          // }

          set({
            authLoading: true,
          });
          await logoutUser();
        } catch (error) {
          console.error(error);
        } finally {
          set({
            authLoading: false,
          });
        }
        // this will make user logout from frontend as well as backend
        // await logoutUser();

        set({
          accessToken: null,
          user: null,
          authLoading: false,
          authStatus: false,
        });
      },
      checkLogin: () => {
        if (get().accessToken && get().authStatus) {
          return true;
        } else {
          return false;
        }
      },
      changeLocalLoginData: (accessToken, user, authStatus) => {
        set({
          accessToken,
          user,
          authStatus,
        });
      },
    }),
    { name: LOCAL_KEY }
  )
);

export default useAuth;
