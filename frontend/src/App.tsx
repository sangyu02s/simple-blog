import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { AppRouter } from './router/AppRouter';
import { useAuthStore } from './stores/authStore';

function App() {
  const { currentUser, isInitializing, initialize, logout } = useAuthStore();

  useEffect(() => {
    void initialize();
  }, [initialize]);

  return (
    <div className="app-shell">
      <header className="app-header">
        <div>
          <h1>Simple Blog</h1>
          <p>面向社区交流的简单博客项目</p>
        </div>
        <nav className="app-nav">
          <Link to="/">首页</Link>
          {!currentUser ? <Link to="/login">登录</Link> : null}
          {currentUser ? <Link to="/write">发帖</Link> : null}
          {currentUser ? <Link to="/me">个人中心</Link> : null}
          <Link to="/admin">后台</Link>
          {currentUser ? (
            <button type="button" className="link-button" onClick={logout}>
              退出（{currentUser.nickname}）
            </button>
          ) : null}
        </nav>
      </header>
      {isInitializing ? <p>正在恢复登录状态...</p> : null}
      <main className="app-main">
        <AppRouter />
      </main>
    </div>
  );
}

export default App;
