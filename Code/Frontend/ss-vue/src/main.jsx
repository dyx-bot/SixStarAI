// src/main.jsx
import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

import App from "./App.jsx";
import RequireAuth from "./auth/RequireAuth.jsx";

import Login from "./pages/Login.jsx";
import Chat from "./pages/Chat.jsx";
import Discussion from "./pages/Discussion.jsx";
import Profile from "./pages/Profile.jsx";
import Setting from "./pages/Setting.jsx";
import Post from "./pages/Post.jsx";
import PostDetail from "./pages/PostDerails.jsx";
import MyPosts from "./pages/MyPosts.jsx";
import MyLike from "./pages/MyLike.jsx";
import MyFavorite from "./pages/MyFavorite.jsx";

const router = createBrowserRouter([
  { path: "/", 
    element: <Login /> },
  {
    path: "/app",
    element: (
      <App/>
    ),
    children: [
      { index: true, element: <Chat /> },           // 默认 Tab：问答/AI
      { path: "discussion", element: <Discussion /> },
      { path: "mine", element: <Profile /> },       // 你的“我的”页
    ],
  },
  { path: "/setting", element: <Setting /> },
  { path: "/post", element: <Post /> },
  { path: "/post/:id", element: <PostDetail /> },
  { path: "/my/posts", element: <MyPosts /> },
  { path: "/my/likes", element: <MyLike /> },
  { path: "/my/favorites", element: <MyFavorite /> },
  { path: "*", element: <Login /> },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
