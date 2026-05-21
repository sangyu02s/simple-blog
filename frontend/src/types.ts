export interface CurrentUser {
  id: number;
  username: string;
  nickname: string;
  role: 'USER' | 'ADMIN';
  status: 'ACTIVE' | 'DISABLED';
}

export interface AuthResponse {
  token: string;
  user: CurrentUser;
}

export interface LoginFormValues {
  username: string;
  password: string;
}

export interface RegisterFormValues {
  username: string;
  nickname: string;
  password: string;
}

export interface PostAuthor {
  id: number;
  username: string;
  nickname: string;
}

export interface PagedResult<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export type PostSortOption = 'latest' | 'mostLiked' | 'mostCommented';

export interface PostSummary {
  id: number;
  title: string;
  summary: string;
  author: PostAuthor;
  tags: string[];
  createdAt: string;
  likeCount: number;
  commentCount: number;
}

export interface PostDetail {
  id: number;
  title: string;
  summary: string;
  content: string;
  author: PostAuthor;
  tags: string[];
  createdAt: string;
  likeCount: number;
  commentCount: number;
  viewCount: number;
}

export interface CreatePostValues {
  title: string;
  summary: string;
  content: string;
  tags: string[];
}

export interface CommentAuthor {
  id: number;
  username: string;
  nickname: string;
}

export interface CommentItem {
  id: number;
  content: string;
  author: CommentAuthor;
  createdAt: string;
}

export interface CreateCommentValues {
  content: string;
}

export interface LikeState {
  liked: boolean;
  likeCount: number;
}

export interface TagItem {
  name: string;
  slug: string;
}
