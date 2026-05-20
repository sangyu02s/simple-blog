import { useEffect, useState, type FormEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getErrorMessage } from '../api/error';
import { fetchMyPostForEdit, updateMyPost } from '../api/me';

export function EditMyPostPage() {
  const { postId } = useParams();
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [summary, setSummary] = useState('');
  const [tags, setTags] = useState('');
  const [content, setContent] = useState('');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (!postId) {
      setErrorMessage('文章 ID 不存在。');
      setIsLoading(false);
      return;
    }

    const loadPost = async () => {
      try {
        const post = await fetchMyPostForEdit(postId);
        setTitle(post.title);
        setSummary(post.summary);
        setContent(post.content);
        setTags(post.tags.join(', '));
        setErrorMessage(null);
      } catch (error) {
        setErrorMessage(getErrorMessage(error, '文章编辑数据加载失败。'));
      } finally {
        setIsLoading(false);
      }
    };

    void loadPost();
  }, [postId]);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (!postId) {
      return;
    }

    setIsSubmitting(true);
    setErrorMessage(null);

    try {
      await updateMyPost(postId, {
        title,
        summary,
        content,
        tags: tags.split(',').map((tag) => tag.trim()).filter(Boolean),
      });
      navigate('/me/posts');
    } catch (error) {
      setErrorMessage(getErrorMessage(error, '文章更新失败。'));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <section className="detail-layout">
      <article className="card detail-card">
        <h2>编辑文章</h2>
        {isLoading ? <p>正在加载文章信息...</p> : null}
        {errorMessage ? <p className="error-text">{errorMessage}</p> : null}
        {!isLoading ? (
          <form className="form-grid" onSubmit={handleSubmit}>
            <label>
              标题
              <input type="text" value={title} onChange={(event) => setTitle(event.target.value)} />
            </label>
            <label>
              摘要
              <input type="text" value={summary} onChange={(event) => setSummary(event.target.value)} />
            </label>
            <label>
              标签
              <input type="text" value={tags} onChange={(event) => setTags(event.target.value)} />
            </label>
            <label>
              正文
              <textarea rows={10} value={content} onChange={(event) => setContent(event.target.value)} />
            </label>
            <button type="submit" disabled={isSubmitting}>
              {isSubmitting ? '保存中...' : '保存修改'}
            </button>
          </form>
        ) : null}
      </article>
    </section>
  );
}
