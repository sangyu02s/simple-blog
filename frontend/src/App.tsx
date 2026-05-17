import { Link } from 'react-router-dom';
import { AppRouter } from './router/AppRouter';

function App() {
  return (
    <div className="app-shell">
      <header className="app-header">
        <div>
          <h1>Simple Blog</h1>
          <p>面向社区交流的简单博客项目</p>
        </div>
        <nav className="app-nav">
          <Link to="/">首页</Link>
          <Link to="/login">登录</Link>
          <Link to="/register">注册</Link>
          <Link to="/write">发帖</Link>
          <Link to="/admin">后台</Link>
        </nav>
      </header>
      <main className="app-main">
        <AppRouter />
      </main>
    </div>
  );
}

export default App;
