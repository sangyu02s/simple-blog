import { http } from './http';
import type { PagedResult, PostSortOption, PostSummary, TagItem } from '../types';

export async function fetchTags() {
  const response = await http.get<TagItem[]>('/api/tags');
  return response.data;
}

export async function fetchPostsByTag(slug: string, page = 0, size = 5, sort: PostSortOption = 'latest') {
  const response = await http.get<PagedResult<PostSummary>>(`/api/tags/${slug}/posts`, {
    params: { page, size, sort },
  });
  return response.data;
}
