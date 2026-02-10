import type RegisterData from "@/models/RegisterData";
import apiClient from "@/config/ApiClient";
import type LoginData from "@/models/LoginData";
import type LoginResponseData from "../models/LoginRequestData";
import type User from "@/models/User";

export const registerUser = async (signupData: RegisterData) => {
  // api call to server to save data
  const response = await apiClient.post("/auth/register", signupData);
  return response.data;
};

export const loginUser = async (loginData: LoginData) => {
  // api call to server to fetch data
  const response = await apiClient.post<LoginResponseData>(
    "/auth/login",
    loginData
  );
  return response.data;
};

export const logoutUser = async () => {
  const response = await apiClient.post("/auth/logout");
  return response.data;
};

// get current login user
export const getCurrentUser = async (emailId: string | undefined) => {
  const response = await apiClient.get<User>(`/users/email/${emailId}`);
  return response.data;
};

// refresh token
export const refreshToken = async () => {
  const response = await apiClient.post<LoginResponseData>(`/auth/refresh`);
  return response.data;
};
