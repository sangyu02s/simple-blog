import { http } from './http';
import type { LikeState } from '../types';

export async function fetchLikeState(postId: string) {
  const response = await http.get<LikeState>(`/api/posts/${postId}/like`);
  return response.data;
}

export async function likePost(postId: string) {
  const response = await http.post<LikeState>(`/api/posts/${postId}/like`);
  return response.data;
}

export async function unlikePost(postId: string) {
  const response = await http.delete<LikeState>(`/api/posts/${postId}/like`);
  return response.data;
}
