import type { FormEvent } from 'react';

export function LoginPage() {
  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    // 这里先阻止默认提交，保留给后续接入真实登录接口。
    event.preventDefault();
  };

  return (
    <section className="card">
      <h2>登录</h2>
      <form className="form-grid" onSubmit={handleSubmit}>
        <label>
          用户名
          <input type="text" placeholder="请输入用户名" />
        </label>
        <label>
          密码
          <input type="password" placeholder="请输入密码" />
        </label>
        <button type="submit">登录</button>
      </form>
    </section>
  );
}
