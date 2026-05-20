import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getErrorMessage } from '../api/error';
import { fetchMyProfile, type MyProfile } from '../api/me';

export function ProfilePage() {
  const [profile, setProfile] = useState<MyProfile | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadProfile = async () => {
      try {
        const data = await fetchMyProfile();
        setProfile(data);
        setErrorMessage(null);
      } catch (error) {
        setErrorMessage(getErrorMessage(error, '个人中心加载失败。'));
      } finally {
        setIsLoading(false);
      }
    };

    void loadProfile();
  }, []);

  return (
    <section className="detail-layout">
      <article className="card detail-card">
        <h2>个人中心</h2>
        {isLoading ? <p>正在加载个人信息...</p> : null}
        {errorMessage ? <p className="error-text">{errorMessage}</p> : null}
        {profile ? (
          <>
            <p className="meta-text">昵称：{profile.nickname}</p>
            <p className="meta-text">用户名：{profile.username}</p>
            <p className="meta-text">角色：{profile.role}</p>
            <p className="meta-text">状态：{profile.status}</p>
            <p className="form-link-row">
              <Link to="/me/posts">查看我的文章</Link>
            </p>
          </>
        ) : null}
      </article>
    </section>
  );
}
