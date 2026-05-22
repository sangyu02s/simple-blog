import { useEffect, useState } from 'react';
import {
  fetchAdminComments,
  fetchAdminOverview,
  fetchAdminPosts,
  fetchAdminUsers,
  updateAdminCommentStatus,
  updateAdminPostStatus,
  updateAdminUserStatus,
  type AdminCommentItem,
  type AdminOverview,
  type AdminPostItem,
  type AdminUserItem,
} from '../api/admin';
import { getErrorMessage } from '../api/error';

export function AdminPage() {
  const [overview, setOverview] = useState<AdminOverview | null>(null);
  const [posts, setPosts] = useState<AdminPostItem[]>([]);
  const [comments, setComments] = useState<AdminCommentItem[]>([]);
  const [users, setUsers] = useState<AdminUserItem[]>([]);
  const [postStatus, setPostStatus] = useState('');
  const [postKeyword, setPostKeyword] = useState('');
  const [commentStatus, setCommentStatus] = useState('');
  const [commentKeyword, setCommentKeyword] = useState('');
  const [userStatus, setUserStatus] = useState('');
  const [userKeyword, setUserKeyword] = useState('');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [updatingPostId, setUpdatingPostId] = useState<number | null>(null);
  const [updatingCommentId, setUpdatingCommentId] = useState<number | null>(null);
  const [updatingUserId, setUpdatingUserId] = useState<number | null>(null);

  const loadAdminData = async () => {
    try {
      const [overviewData, postData, commentData, userData] = await Promise.all([
        fetchAdminOverview(),
        fetchAdminPosts(postStatus, postKeyword),
        fetchAdminComments(commentStatus, commentKeyword),
        fetchAdminUsers(userStatus, userKeyword),
      ]);
      setOverview(overviewData);
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

  useEffect(() => {
    void loadAdminData();
  }, [postStatus, postKeyword, commentStatus, commentKeyword, userStatus, userKeyword]);

  const handleTogglePostStatus = async (post: AdminPostItem) => {
    const nextStatus = post.status === 'PUBLISHED' ? 'HIDDEN' : 'PUBLISHED';
    setUpdatingPostId(post.id);

    try {
      await updateAdminPostStatus(post.id, nextStatus);
      await loadAdminData();
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
      await updateAdminCommentStatus(comment.id, nextStatus);
      await loadAdminData();
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
      await updateAdminUserStatus(user.id, nextStatus);
      await loadAdminData();
    } catch (error) {
      setErrorMessage(getErrorMessage(error, '用户状态更新失败。'));
    } finally {
      setUpdatingUserId(null);
    }
  };

  return (
    <section className="detail-layout">
      <article className="card detail-card">
        <h2>后台概览</h2>
        {overview ? (
          <div className="overview-grid">
            <div className="overview-card"><strong>{overview.totalUsers}</strong><span>用户总数</span></div>
            <div className="overview-card"><strong>{overview.totalPosts}</strong><span>文章总数</span></div>
            <div className="overview-card"><strong>{overview.totalComments}</strong><span>评论总数</span></div>
            <div className="overview-card"><strong>{overview.hiddenPosts}</strong><span>隐藏文章</span></div>
            <div className="overview-card"><strong>{overview.hiddenComments}</strong><span>隐藏评论</span></div>
            <div className="overview-card"><strong>{overview.disabledUsers}</strong><span>禁用用户</span></div>
          </div>
        ) : null}
      </article>

      {isLoading ? <p>正在加载后台数据...</p> : null}
      {errorMessage ? <p className="error-text">{errorMessage}</p> : null}

      <article className="card detail-card">
        <h2>后台文章管理</h2>
        <div className="filter-row">
          <select value={postStatus} onChange={(event) => setPostStatus(event.target.value)}>
            <option value="">全部状态</option>
            <option value="PUBLISHED">PUBLISHED</option>
            <option value="HIDDEN">HIDDEN</option>
          </select>
          <input value={postKeyword} onChange={(event) => setPostKeyword(event.target.value)} placeholder="按标题搜索" />
        </div>
        {!isLoading && posts.length === 0 ? <p>当前没有匹配文章。</p> : null}
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
                <button type="button" className="primary-button" onClick={() => handleTogglePostStatus(post)} disabled={updatingPostId === post.id}>
                  {updatingPostId === post.id ? '处理中...' : post.status === 'PUBLISHED' ? '隐藏文章' : '恢复文章'}
                </button>
              </div>
            </article>
          ))}
        </div>
      </article>

      <article className="card detail-card">
        <h2>后台评论管理</h2>
        <div className="filter-row">
          <select value={commentStatus} onChange={(event) => setCommentStatus(event.target.value)}>
            <option value="">全部状态</option>
            <option value="VISIBLE">VISIBLE</option>
            <option value="HIDDEN">HIDDEN</option>
          </select>
          <input value={commentKeyword} onChange={(event) => setCommentKeyword(event.target.value)} placeholder="按评论内容搜索" />
        </div>
        {!isLoading && comments.length === 0 ? <p>当前没有匹配评论。</p> : null}
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
                <button type="button" className="primary-button" onClick={() => handleToggleCommentStatus(comment)} disabled={updatingCommentId === comment.id}>
                  {updatingCommentId === comment.id ? '处理中...' : comment.status === 'VISIBLE' ? '隐藏评论' : '恢复评论'}
                </button>
              </div>
            </article>
          ))}
        </div>
      </article>

      <article className="card detail-card">
        <h2>后台用户管理</h2>
        <div className="filter-row">
          <select value={userStatus} onChange={(event) => setUserStatus(event.target.value)}>
            <option value="">全部状态</option>
            <option value="ACTIVE">ACTIVE</option>
            <option value="DISABLED">DISABLED</option>
          </select>
          <input value={userKeyword} onChange={(event) => setUserKeyword(event.target.value)} placeholder="按用户名或昵称搜索" />
        </div>
        {!isLoading && users.length === 0 ? <p>当前没有匹配用户。</p> : null}
        <div className="admin-post-list">
          {users.map((user) => (
            <article key={user.id} className="admin-post-card">
              <div className="admin-post-header">
                <div>
                  <h3>{user.nickname}</h3>
                  <p className="meta-text">用户名：{user.username} · 角色：{user.role}</p>
                  <p className="meta-text">当前状态：{user.status}</p>
                </div>
                <button type="button" className="primary-button" onClick={() => handleToggleUserStatus(user)} disabled={updatingUserId === user.id}>
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
