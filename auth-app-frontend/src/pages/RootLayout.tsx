import React from "react";
import { Outlet } from "react-router";
import Navbar from "./Navbar";
import { Toaster } from "react-hot-toast";

function RootLayout() {
  return (
    <div>
      <Toaster />
      <Navbar />
      <Outlet />
    </div>
  );
}

export default RootLayout;
