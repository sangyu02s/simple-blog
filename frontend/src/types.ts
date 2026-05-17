export interface DemoPost {
  id: number;
  title: string;
  summary: string;
  tags: string[];
}

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
