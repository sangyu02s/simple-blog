import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchPosts } from '../api/posts';
import type { PostSummary } from '../types';

export function HomePage() {
  const [posts, setPosts] = useState<PostSummary[]>([]);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadPosts = async () => {
      try {
        const data = await fetchPosts();
        setPosts(data);
        setErrorMessage(null);
      } catch {
        setErrorMessage('文章列表加载失败，请稍后重试。');
      } finally {
        setIsLoading(false);
      }
    };

    void loadPosts();
  }, []);

  return (
    <section>
      <h2>最新文章</h2>
      {isLoading ? <p>正在加载文章...</p> : null}
      {errorMessage ? <p className="error-text">{errorMessage}</p> : null}
      {!isLoading && !errorMessage && posts.length === 0 ? <p>当前还没有已发布文章。</p> : null}
      <div className="card-list">
        {posts.map((post) => (
          <article key={post.id} className="card">
            <h3>
              <Link to={`/posts/${post.id}`}>{post.title}</Link>
            </h3>
            <p>{post.summary}</p>
            <p className="meta-text">
              作者：{post.author.nickname} · 点赞 {post.likeCount} · 评论 {post.commentCount}
            </p>
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
