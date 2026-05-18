import { http } from './http';
import type { CommentItem, CreateCommentValues } from '../types';

export async function fetchComments(postId: string) {
  const response = await http.get<CommentItem[]>(`/api/posts/${postId}/comments`);
  return response.data;
}

export async function createComment(postId: string, values: CreateCommentValues) {
  const response = await http.post<CommentItem>(`/api/posts/${postId}/comments`, values);
  return response.data;
}
