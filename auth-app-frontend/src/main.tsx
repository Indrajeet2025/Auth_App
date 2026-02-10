import { createRoot } from "react-dom/client";
import "./index.css";

import { BrowserRouter, Routes, Route } from "react-router";

import RootLayout from "./pages/RootLayout";
import App from "./App";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import About from "./pages/About";
import Services from "./pages/Services";
import Userhome from "./pages/user/Userhome";
import Userlayout from "./pages/user/Userlayout";
import Userprofile from "./pages/user/Userprofile";
import OAuthSuccessPage from "./pages/OAuthSuccessPage";

createRoot(document.getElementById("root")!).render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<RootLayout />}>
        <Route index element={<App />} />
        <Route path="login" element={<Login />} />
        <Route path="signup" element={<Signup />} />
        <Route path="about" element={<About />} />
        <Route path="services" element={<Services />} />
        <Route path="dashboard" element={<Userlayout />}>
          <Route index element={<Userhome />} />
          <Route path="profile" element={<Userprofile />} />
        </Route>
        <Route path={"/auth/success"} element={<OAuthSuccessPage />} />
      </Route>
    </Routes>
  </BrowserRouter>
);
