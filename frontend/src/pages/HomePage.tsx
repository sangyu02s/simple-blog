import type { DemoPost } from '../types';

const demoPosts: DemoPost[] = [
  {
    id: 1,
    title: '欢迎来到 Simple Blog',
    summary: '这里会展示博客列表、热门标签和社区动态。',
    tags: ['公告', '社区'],
  },
  {
    id: 2,
    title: '第一版功能规划',
    summary: '首版将优先实现登录、发帖、评论、点赞与后台管理。',
    tags: ['开发', '规划'],
  },
];

export function HomePage() {
  return (
    <section>
      <h2>最新文章</h2>
      <div className="card-list">
        {demoPosts.map((post) => (
          <article key={post.id} className="card">
            <h3>{post.title}</h3>
            <p>{post.summary}</p>
            <div className="tag-list">
              {post.tags.map((tag) => (
                <span key={tag} className="tag">
                  {tag}
                </span>
              ))}
            </div>
          </article>
        ))}
      </div>
    </section>
  );
}
