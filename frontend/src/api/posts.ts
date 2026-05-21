import { http } from './http';
import type { CreatePostValues, PagedResult, PostDetail, PostSortOption, PostSummary } from '../types';

export async function fetchPosts(page = 0, size = 5, sort: PostSortOption = 'latest') {
  const response = await http.get<PagedResult<PostSummary>>('/api/posts', {
    params: { page, size, sort },
  });
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
