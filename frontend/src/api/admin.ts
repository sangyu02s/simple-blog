import { http } from './http';

export interface AdminPostItem {
  id: number;
  title: string;
  authorNickname: string;
  status: 'PUBLISHED' | 'HIDDEN';
  createdAt: string;
  likeCount: number;
  commentCount: number;
}

export interface AdminCommentItem {
  id: number;
  postTitle: string;
  authorNickname: string;
  content: string;
  status: 'VISIBLE' | 'HIDDEN';
  createdAt: string;
}

export interface AdminUserItem {
  id: number;
  username: string;
  nickname: string;
  role: 'USER' | 'ADMIN';
  status: 'ACTIVE' | 'DISABLED';
  createdAt: string;
}

export async function fetchAdminPosts() {
  const response = await http.get<AdminPostItem[]>('/api/admin/posts');
  return response.data;
}

export async function updateAdminPostStatus(postId: number, status: 'PUBLISHED' | 'HIDDEN') {
  const response = await http.patch<AdminPostItem>(`/api/admin/posts/${postId}/status`, { status });
  return response.data;
}

export async function fetchAdminComments() {
  const response = await http.get<AdminCommentItem[]>('/api/admin/comments');
  return response.data;
}

export async function updateAdminCommentStatus(commentId: number, status: 'VISIBLE' | 'HIDDEN') {
  const response = await http.patch<AdminCommentItem>(`/api/admin/comments/${commentId}/status`, { status });
  return response.data;
}

export async function fetchAdminUsers() {
  const response = await http.get<AdminUserItem[]>('/api/admin/users');
  return response.data;
}

export async function updateAdminUserStatus(userId: number, status: 'ACTIVE' | 'DISABLED') {
  const response = await http.patch<AdminUserItem>(`/api/admin/users/${userId}/status`, { status });
  return response.data;
}
