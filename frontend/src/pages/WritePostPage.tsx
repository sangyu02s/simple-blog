import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { getErrorMessage } from '../api/error';
import { createPost } from '../api/posts';
import { useAuthStore } from '../stores/authStore';

export function WritePostPage() {
  const navigate = useNavigate();
  const { token } = useAuthStore();
  const [title, setTitle] = useState('');
  const [summary, setSummary] = useState('');
  const [tags, setTags] = useState('');
  const [content, setContent] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (!token) {
      setErrorMessage('请先登录后再发布文章。');
      return;
    }

    setIsSubmitting(true);
    setErrorMessage(null);

    try {
      const post = await createPost({
        title,
        summary,
        content,
        tags: tags.split(',').map((tag) => tag.trim()).filter(Boolean),
      });
      navigate(`/posts/${post.id}`);
    } catch (error) {
      setErrorMessage(getErrorMessage(error, '发布失败，请检查输入内容后重试。'));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <section className="card">
      <h2>发布文章</h2>
      <form className="form-grid" onSubmit={handleSubmit}>
        <label>
          标题
          <input type="text" placeholder="请输入标题" value={title} onChange={(event) => setTitle(event.target.value)} />
        </label>
        <label>
          摘要
          <input
            type="text"
            placeholder="请输入摘要"
            value={summary}
            onChange={(event) => setSummary(event.target.value)}
          />
        </label>
        <label>
          标签
          <input
            type="text"
            placeholder="多个标签用逗号分隔"
            value={tags}
            onChange={(event) => setTags(event.target.value)}
          />
        </label>
        <label>
          正文
          <textarea
            rows={10}
            placeholder="请输入正文内容"
            value={content}
            onChange={(event) => setContent(event.target.value)}
          />
        </label>
        {errorMessage ? <p className="error-text">{errorMessage}</p> : null}
        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? '提交中...' : '提交'}
        </button>
      </form>
    </section>
  );
}
