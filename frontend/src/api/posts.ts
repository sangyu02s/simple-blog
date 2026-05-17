import { http } from './http';
import type { CreatePostValues, PostDetail, PostSummary } from '../types';

export async function fetchPosts() {
  const response = await http.get<PostSummary[]>('/api/posts');
  return response.data;
}

export async function fetchPostDetail(postId: string) {
  const response = await http.get<PostDetail>(`/api/posts/${postId}`);
  return response.data;
}

export async function createPost(values: CreatePostValues) {
  const response = await http.post<PostDetail>('/api/posts', values);
  return response.data;
}
