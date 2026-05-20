import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getErrorMessage } from '../api/error';
import { fetchMyPosts, hideMyPost, type MyPostItem } from '../api/me';

export function MyPostsPage() {
  const [posts, setPosts] = useState<MyPostItem[]>([]);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [updatingPostId, setUpdatingPostId] = useState<number | null>(null);

  useEffect(() => {
    const loadMyPosts = async () => {
      try {
        const data = await fetchMyPosts();
        setPosts(data);
        setErrorMessage(null);
      } catch (error) {
        setErrorMessage(getErrorMessage(error, '我的文章加载失败。'));
      } finally {
        setIsLoading(false);
      }
    };

    void loadMyPosts();
  }, []);

  const handleHidePost = async (post: MyPostItem) => {
    setUpdatingPostId(post.id);

    try {
      const updatedPost = await hideMyPost(String(post.id));
      setPosts((currentPosts) => currentPosts.map((item) => item.id === updatedPost.id ? updatedPost : item));
      setErrorMessage(null);
    } catch (error) {
      setErrorMessage(getErrorMessage(error, '隐藏文章失败。'));
    } finally {
      setUpdatingPostId(null);
    }
  };

  return (
    <section className="detail-layout">
      <article className="card detail-card">
        <h2>我的文章</h2>
        {isLoading ? <p>正在加载文章列表...</p> : null}
        {errorMessage ? <p className="error-text">{errorMessage}</p> : null}
        {!isLoading && posts.length === 0 ? <p>你还没有发布文章。</p> : null}
        <div className="admin-post-list">
          {posts.map((post) => (
            <article key={post.id} className="admin-post-card">
              <div className="admin-post-header align-start">
                <div>
                  <h3>{post.title}</h3>
                  <p className="meta-text">状态：{post.status}</p>
                  <p className="meta-text">点赞 {post.likeCount} · 评论 {post.commentCount}</p>
                  <p>{post.summary}</p>
                </div>
                <div className="action-column">
                  <Link className="primary-link" to={`/posts/${post.id}`}>
                    查看详情
                  </Link>
                  <Link className="primary-link" to={`/me/posts/${post.id}/edit`}>
                    编辑文章
                  </Link>
                  {post.status === 'PUBLISHED' ? (
                    <button
                      type="button"
                      className="primary-button"
                      onClick={() => handleHidePost(post)}
                      disabled={updatingPostId === post.id}
                    >
                      {updatingPostId === post.id ? '处理中...' : '隐藏文章'}
                    </button>
                  ) : null}
                </div>
              </div>
            </article>
          ))}
        </div>
      </article>
    </section>
  );
}
