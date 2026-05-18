import { useEffect, useState, type FormEvent } from 'react';
import { useParams } from 'react-router-dom';
import { createComment, fetchComments } from '../api/comments';
import { fetchPostDetail } from '../api/posts';
import { useAuthStore } from '../stores/authStore';
import type { CommentItem, PostDetail } from '../types';

export function PostDetailPage() {
  const { postId } = useParams();
  const { token } = useAuthStore();
  const [post, setPost] = useState<PostDetail | null>(null);
  const [comments, setComments] = useState<CommentItem[]>([]);
  const [commentContent, setCommentContent] = useState('');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [commentErrorMessage, setCommentErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isCommentSubmitting, setIsCommentSubmitting] = useState(false);

  useEffect(() => {
    if (!postId) {
      setErrorMessage('文章 ID 不存在。');
      setIsLoading(false);
      return;
    }

    const loadPageData = async () => {
      try {
        const [postData, commentData] = await Promise.all([
          fetchPostDetail(postId),
          fetchComments(postId),
        ]);
        setPost(postData);
        setComments(commentData);
        setErrorMessage(null);
      } catch {
        setErrorMessage('文章详情加载失败，请稍后重试。');
      } finally {
        setIsLoading(false);
      }
    };

    void loadPageData();
  }, [postId]);

  const handleCommentSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (!postId) {
      return;
    }

    if (!token) {
      setCommentErrorMessage('请先登录后再发表评论。');
      return;
    }

    setIsCommentSubmitting(true);
    setCommentErrorMessage(null);

    try {
      await createComment(postId, { content: commentContent });

      // 评论成功后同时刷新评论列表和文章详情，保证评论数与内容保持一致。
      const [postData, commentData] = await Promise.all([
        fetchPostDetail(postId),
        fetchComments(postId),
      ]);
      setPost(postData);
      setComments(commentData);
      setCommentContent('');
    } catch {
      setCommentErrorMessage('发表评论失败，请检查内容后重试。');
    } finally {
      setIsCommentSubmitting(false);
    }
  };

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
    <section className="detail-layout">
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

      <section className="card detail-card">
        <h3>评论（{comments.length}）</h3>
        <form className="form-grid" onSubmit={handleCommentSubmit}>
          <label>
            发表评论
            <textarea
              rows={4}
              placeholder="请输入评论内容"
              value={commentContent}
              onChange={(event) => setCommentContent(event.target.value)}
            />
          </label>
          {commentErrorMessage ? <p className="error-text">{commentErrorMessage}</p> : null}
          <button type="submit" disabled={isCommentSubmitting}>
            {isCommentSubmitting ? '提交中...' : '提交评论'}
          </button>
        </form>

        {comments.length === 0 ? <p>当前还没有评论，欢迎发表第一条评论。</p> : null}
        <div className="comment-list">
          {comments.map((comment) => (
            <article key={comment.id} className="comment-card">
              <p className="meta-text">{comment.author.nickname}</p>
              <div className="content-block">{comment.content}</div>
            </article>
          ))}
        </div>
      </section>
    </section>
  );
}
