import axios from 'axios';

export const http = axios.create({
  baseURL: 'http://127.0.0.1:8080',
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('simple-blog-token');

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});
