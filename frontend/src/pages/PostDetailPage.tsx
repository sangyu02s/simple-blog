import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchPostDetail } from '../api/posts';
import type { PostDetail } from '../types';

export function PostDetailPage() {
  const { postId } = useParams();
  const [post, setPost] = useState<PostDetail | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (!postId) {
      setErrorMessage('文章 ID 不存在。');
      setIsLoading(false);
      return;
    }

    const loadPost = async () => {
      try {
        const data = await fetchPostDetail(postId);
        setPost(data);
        setErrorMessage(null);
      } catch {
        setErrorMessage('文章详情加载失败，请稍后重试。');
      } finally {
        setIsLoading(false);
      }
    };

    void loadPost();
  }, [postId]);

  if (isLoading) {
    return <p>正在加载文章详情...</p>;
  }

  if (errorMessage) {
    return <p className="error-text">{errorMessage}</p>;
  }

  if (!post) {
    return <p>未找到文章内容。</p>;
  }

  return (
    <article className="card detail-card">
      <h2>{post.title}</h2>
      <p className="meta-text">
        作者：{post.author.nickname} · 点赞 {post.likeCount} · 评论 {post.commentCount} · 浏览 {post.viewCount}
      </p>
      <p>{post.summary}</p>
      <div className="tag-list">
        {post.tags.map((tag) => (
          <span key={tag} className="tag">
            {tag}
          </span>
        ))}
      </div>
      <div className="content-block">{post.content}</div>
    </article>
  );
}
