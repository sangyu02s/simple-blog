import { Route, Routes } from 'react-router-dom';
import { HomePage } from '../pages/HomePage';
import { LoginPage } from '../pages/LoginPage';
import { RegisterPage } from '../pages/RegisterPage';
import { WritePostPage } from '../pages/WritePostPage';
import { AdminPage } from '../pages/AdminPage';
import { PostDetailPage } from '../pages/PostDetailPage';

export function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/write" element={<WritePostPage />} />
      <Route path="/posts/:postId" element={<PostDetailPage />} />
      <Route path="/admin" element={<AdminPage />} />
    </Routes>
  );
}
