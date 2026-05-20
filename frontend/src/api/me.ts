import { http } from './http';

export interface MyProfile {
  id: number;
  username: string;
  nickname: string;
  role: 'USER' | 'ADMIN';
  status: 'ACTIVE' | 'DISABLED';
}

export interface MyPostItem {
  id: number;
  title: string;
  summary: string;
  status: 'PUBLISHED' | 'HIDDEN';
  createdAt: string;
  likeCount: number;
  commentCount: number;
}

export interface UpdateMyPostValues {
  title: string;
  summary: string;
  content: string;
  tags: string[];
}

export async function fetchMyProfile() {
  const response = await http.get<MyProfile>('/api/me');
  return response.data;
}

export async function fetchMyPosts() {
  const response = await http.get<MyPostItem[]>('/api/me/posts');
  return response.data;
}

export async function fetchMyPostForEdit(postId: string) {
  const response = await http.get<UpdateMyPostValues>(`/api/me/posts/${postId}`);
  return response.data;
}

export async function updateMyPost(postId: string, values: UpdateMyPostValues) {
  const response = await http.put<MyPostItem>(`/api/me/posts/${postId}`, values);
  return response.data;
}

export async function hideMyPost(postId: string) {
  const response = await http.delete<MyPostItem>(`/api/me/posts/${postId}`);
  return response.data;
}
