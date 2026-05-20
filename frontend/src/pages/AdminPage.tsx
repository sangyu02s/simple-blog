import { useEffect, useState } from 'react';
import {
  fetchAdminComments,
  fetchAdminPosts,
  fetchAdminUsers,
  updateAdminCommentStatus,
  updateAdminPostStatus,
  updateAdminUserStatus,
  type AdminCommentItem,
  type AdminPostItem,
  type AdminUserItem,
} from '../api/admin';
import { getErrorMessage } from '../api/error';

export function AdminPage() {
  const [posts, setPosts] = useState<AdminPostItem[]>([]);
  const [comments, setComments] = useState<AdminCommentItem[]>([]);
  const [users, setUsers] = useState<AdminUserItem[]>([]);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [updatingPostId, setUpdatingPostId] = useState<number | null>(null);
  const [updatingCommentId, setUpdatingCommentId] = useState<number | null>(null);
  const [updatingUserId, setUpdatingUserId] = useState<number | null>(null);

  useEffect(() => {
    const loadAdminData = async () => {
      try {
        const [postData, commentData, userData] = await Promise.all([
          fetchAdminPosts(),
          fetchAdminComments(),
          fetchAdminUsers(),
        ]);
        setPosts(postData);
        setComments(commentData);
        setUsers(userData);
        setErrorMessage(null);
      } catch (error) {
        setErrorMessage(getErrorMessage(error, '后台管理数据加载失败。'));
      } finally {
        setIsLoading(false);
      }
    };

    void loadAdminData();
  }, []);

  const handleTogglePostStatus = async (post: AdminPostItem) => {
    const nextStatus = post.status === 'PUBLISHED' ? 'HIDDEN' : 'PUBLISHED';
    setUpdatingPostId(post.id);

    try {
      const updatedPost = await updateAdminPostStatus(post.id, nextStatus);
      setPosts((currentPosts) => currentPosts.map((item) => item.id === updatedPost.id ? updatedPost : item));
      setErrorMessage(null);
    } catch (error) {
      setErrorMessage(getErrorMessage(error, '文章状态更新失败。'));
    } finally {
      setUpdatingPostId(null);
    }
  };

  const handleToggleCommentStatus = async (comment: AdminCommentItem) => {
    const nextStatus = comment.status === 'VISIBLE' ? 'HIDDEN' : 'VISIBLE';
    setUpdatingCommentId(comment.id);

    try {
      const updatedComment = await updateAdminCommentStatus(comment.id, nextStatus);
      setComments((currentComments) => currentComments.map((item) => item.id === updatedComment.id ? updatedComment : item));
      setErrorMessage(null);
    } catch (error) {
      setErrorMessage(getErrorMessage(error, '评论状态更新失败。'));
    } finally {
      setUpdatingCommentId(null);
    }
  };

  const handleToggleUserStatus = async (user: AdminUserItem) => {
    const nextStatus = user.status === 'ACTIVE' ? 'DISABLED' : 'ACTIVE';
    setUpdatingUserId(user.id);

    try {
      const updatedUser = await updateAdminUserStatus(user.id, nextStatus);
      setUsers((currentUsers) => currentUsers.map((item) => item.id === updatedUser.id ? updatedUser : item));
      setErrorMessage(null);
    } catch (error) {
      setErrorMessage(getErrorMessage(error, '用户状态更新失败。'));
    } finally {
      setUpdatingUserId(null);
    }
  };

  return (
    <section className="detail-layout">
      <article className="card detail-card">
        <h2>后台文章管理</h2>
        <p className="meta-text">可在这里查看文章状态，并执行隐藏或恢复操作。</p>
        {isLoading ? <p>正在加载后台数据...</p> : null}
        {errorMessage ? <p className="error-text">{errorMessage}</p> : null}
        {!isLoading && posts.length === 0 ? <p>当前还没有文章。</p> : null}
        <div className="admin-post-list">
          {posts.map((post) => (
            <article key={post.id} className="admin-post-card">
              <div className="admin-post-header">
                <div>
                  <h3>{post.title}</h3>
                  <p className="meta-text">
                    作者：{post.authorNickname} · 点赞 {post.likeCount} · 评论 {post.commentCount}
                  </p>
                  <p className="meta-text">当前状态：{post.status}</p>
                </div>
                <button
                  type="button"
                  className="primary-button"
                  onClick={() => handleTogglePostStatus(post)}
                  disabled={updatingPostId === post.id}
                >
                  {updatingPostId === post.id ? '处理中...' : post.status === 'PUBLISHED' ? '隐藏文章' : '恢复文章'}
                </button>
              </div>
            </article>
          ))}
        </div>
      </article>

      <article className="card detail-card">
        <h2>后台评论管理</h2>
        <p className="meta-text">可在这里查看评论状态，并执行隐藏或恢复操作。</p>
        {!isLoading && comments.length === 0 ? <p>当前还没有评论。</p> : null}
        <div className="admin-post-list">
          {comments.map((comment) => (
            <article key={comment.id} className="admin-post-card">
              <div className="admin-post-header">
                <div>
                  <h3>{comment.postTitle}</h3>
                  <p className="meta-text">评论作者：{comment.authorNickname}</p>
                  <p className="meta-text">当前状态：{comment.status}</p>
                  <div className="content-block">{comment.content}</div>
                </div>
                <button
                  type="button"
                  className="primary-button"
                  onClick={() => handleToggleCommentStatus(comment)}
                  disabled={updatingCommentId === comment.id}
                >
                  {updatingCommentId === comment.id ? '处理中...' : comment.status === 'VISIBLE' ? '隐藏评论' : '恢复评论'}
                </button>
              </div>
            </article>
          ))}
        </div>
      </article>

      <article className="card detail-card">
        <h2>后台用户管理</h2>
        <p className="meta-text">可在这里查看用户状态，并执行禁用或恢复操作。</p>
        {!isLoading && users.length === 0 ? <p>当前还没有用户。</p> : null}
        <div className="admin-post-list">
          {users.map((user) => (
            <article key={user.id} className="admin-post-card">
              <div className="admin-post-header">
                <div>
                  <h3>{user.nickname}</h3>
                  <p className="meta-text">用户名：{user.username} · 角色：{user.role}</p>
                  <p className="meta-text">当前状态：{user.status}</p>
                </div>
                <button
                  type="button"
                  className="primary-button"
                  onClick={() => handleToggleUserStatus(user)}
                  disabled={updatingUserId === user.id}
                >
                  {updatingUserId === user.id ? '处理中...' : user.status === 'ACTIVE' ? '禁用用户' : '恢复用户'}
                </button>
              </div>
            </article>
          ))}
        </div>
      </article>
    </section>
  );
}
