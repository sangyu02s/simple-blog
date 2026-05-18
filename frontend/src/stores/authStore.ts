import { create } from 'zustand';
import { fetchCurrentUser, login, register } from '../api/auth';
import { getErrorMessage } from '../api/error';
import type { CurrentUser, LoginFormValues, RegisterFormValues } from '../types';

const TOKEN_KEY = 'simple-blog-token';

interface AuthStore {
  token: string | null;
  currentUser: CurrentUser | null;
  isInitializing: boolean;
  isSubmitting: boolean;
  errorMessage: string | null;
  initialize: () => Promise<void>;
  login: (values: LoginFormValues) => Promise<void>;
  register: (values: RegisterFormValues) => Promise<void>;
  logout: () => void;
}

export const useAuthStore = create<AuthStore>((set) => ({
  token: localStorage.getItem(TOKEN_KEY),
  currentUser: null,
  isInitializing: true,
  isSubmitting: false,
  errorMessage: null,
  initialize: async () => {
    const token = localStorage.getItem(TOKEN_KEY);

    if (!token) {
      set({ token: null, currentUser: null, isInitializing: false });
      return;
    }

    try {
      const currentUser = await fetchCurrentUser();
      set({ token, currentUser, isInitializing: false, errorMessage: null });
    } catch {
      localStorage.removeItem(TOKEN_KEY);
      set({ token: null, currentUser: null, isInitializing: false, errorMessage: null });
    }
  },
  login: async (values) => {
    set({ isSubmitting: true, errorMessage: null });

    try {
      const response = await login(values);
      localStorage.setItem(TOKEN_KEY, response.token);
      set({
        token: response.token,
        currentUser: response.user,
        isSubmitting: false,
        errorMessage: null,
      });
    } catch (error) {
      set({
        isSubmitting: false,
        errorMessage: getErrorMessage(error, '登录失败，请检查用户名和密码。'),
      });
      throw error;
    }
  },
  register: async (values) => {
    set({ isSubmitting: true, errorMessage: null });

    try {
      const response = await register(values);
      localStorage.setItem(TOKEN_KEY, response.token);
      set({
        token: response.token,
        currentUser: response.user,
        isSubmitting: false,
        errorMessage: null,
      });
    } catch (error) {
      set({
        isSubmitting: false,
        errorMessage: getErrorMessage(error, '注册失败，请检查输入信息。'),
      });
      throw error;
    }
  },
  logout: () => {
    localStorage.removeItem(TOKEN_KEY);
    set({ token: null, currentUser: null, errorMessage: null });
  },
}));
