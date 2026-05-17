import { http } from './http';
import type { AuthResponse, CurrentUser, LoginFormValues, RegisterFormValues } from '../types';

export async function login(values: LoginFormValues) {
  const response = await http.post<AuthResponse>('/api/auth/login', values);
  return response.data;
}

export async function register(values: RegisterFormValues) {
  const response = await http.post<AuthResponse>('/api/auth/register', values);
  return response.data;
}

export async function fetchCurrentUser() {
  const response = await http.get<CurrentUser>('/api/auth/me');
  return response.data;
}
