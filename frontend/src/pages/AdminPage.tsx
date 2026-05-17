export function AdminPage() {
  return (
    <section>
      <h2>后台管理</h2>
      <div className="card-list">
        <article className="card">
          <h3>文章管理</h3>
          <p>处理文章隐藏、恢复与状态维护。</p>
        </article>
        <article className="card">
          <h3>评论管理</h3>
          <p>处理评论审核、隐藏与恢复。</p>
        </article>
        <article className="card">
          <h3>用户管理</h3>
          <p>处理用户状态、角色和基础封禁能力。</p>
        </article>
      </div>
    </section>
  );
}
