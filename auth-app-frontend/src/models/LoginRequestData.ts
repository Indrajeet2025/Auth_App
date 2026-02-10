import type User from "./User";

export default interface LoginRequestData {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  tokenType?: string; // optional if backend sends it
  userDto: User; // ✅ change this
}
