import type { FormEvent } from 'react';

export function WritePostPage() {
  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    // 这里先阻止默认提交，保留给后续接入真实发帖接口。
    event.preventDefault();
  };

  return (
    <section className="card">
      <h2>发布文章</h2>
      <form className="form-grid" onSubmit={handleSubmit}>
        <label>
          标题
          <input type="text" placeholder="请输入标题" />
        </label>
        <label>
          摘要
          <input type="text" placeholder="请输入摘要" />
        </label>
        <label>
          标签
          <input type="text" placeholder="多个标签用逗号分隔" />
        </label>
        <label>
          正文
          <textarea rows={10} placeholder="请输入正文内容" />
        </label>
        <button type="submit">提交</button>
      </form>
    </section>
  );
}
