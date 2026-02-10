export default interface User {
  id: string;
  name: string; // ✅ add this
  email: string;

  // keep others optional if sometimes present
  enabled?: boolean;
  image?: string;
  updatedAt?: string;
  createdAt?: string;
  provider?: string;

  // ❌ remove name if you don’t have it
  // name?: string;
}
