import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';

export function RegisterPage() {
  const navigate = useNavigate();
  const { register, isSubmitting, errorMessage } = useAuthStore();
  const [username, setUsername] = useState('');
  const [nickname, setNickname] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    await register({ username, nickname, password });
    navigate('/');
  };

  return (
    <section className="card">
      <h2>注册</h2>
      <form className="form-grid" onSubmit={handleSubmit}>
        <label>
          用户名
          <input
            type="text"
            placeholder="请输入用户名"
            value={username}
            onChange={(event) => setUsername(event.target.value)}
          />
        </label>
        <label>
          昵称
          <input
            type="text"
            placeholder="请输入昵称"
            value={nickname}
            onChange={(event) => setNickname(event.target.value)}
          />
        </label>
        <label>
          密码
          <input
            type="password"
            placeholder="请输入密码"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />
        </label>
        {errorMessage ? <p className="error-text">{errorMessage}</p> : null}
        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? '注册中...' : '注册'}
        </button>
      </form>
    </section>
  );
}
