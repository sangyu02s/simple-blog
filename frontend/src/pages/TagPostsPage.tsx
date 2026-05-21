import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getErrorMessage } from '../api/error';
import { fetchPostsByTag, fetchTags } from '../api/tags';
import type { PagedResult, PostSortOption, PostSummary, TagItem } from '../types';

const PAGE_SIZE = 5;

export function TagPostsPage() {
  const { slug } = useParams();
  const [result, setResult] = useState<PagedResult<PostSummary> | null>(null);
  const [tags, setTags] = useState<TagItem[]>([]);
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState<PostSortOption>('latest');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (!slug) {
      setErrorMessage('标签不存在。');
      setIsLoading(false);
      return;
    }

    const loadTagPosts = async () => {
      try {
        const [postData, tagData] = await Promise.all([
          fetchPostsByTag(slug, page, PAGE_SIZE, sort),
          fetchTags(),
        ]);
        setResult(postData);
        setTags(tagData);
        setErrorMessage(null);
      } catch (error) {
        setErrorMessage(getErrorMessage(error, '标签文章加载失败。'));
      } finally {
        setIsLoading(false);
      }
    };

    setIsLoading(true);
    void loadTagPosts();
  }, [slug, page, sort]);

  const currentTag = tags.find((item) => item.slug === slug);
  const posts = result?.content ?? [];

  return (
    <section className="detail-layout">
      <div className="toolbar-row">
        <h2>标签：{currentTag?.name ?? slug}</h2>
        <select value={sort} onChange={(event) => setSort(event.target.value as PostSortOption)}>
          <option value="latest">最新发布</option>
          <option value="mostLiked">点赞最多</option>
          <option value="mostCommented">评论最多</option>
        </select>
      </div>
      {isLoading ? <p>正在加载标签文章...</p> : null}
      {errorMessage ? <p className="error-text">{errorMessage}</p> : null}
      {!isLoading && !errorMessage && posts.length === 0 ? <p>该标签下还没有文章。</p> : null}
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
                <Link key={tag} className="tag tag-link" to={`/tags/${encodeURIComponent(tag.toLowerCase())}`}>
                  {tag}
                </Link>
              ))}
            </div>
          </article>
        ))}
      </div>
      {result ? (
        <div className="pagination-row">
          <button type="button" className="primary-button" disabled={!result.hasPrevious} onClick={() => setPage((current) => current - 1)}>
            上一页
          </button>
          <span className="meta-text">
            第 {result.page + 1} 页 / 共 {Math.max(result.totalPages, 1)} 页
          </span>
          <button type="button" className="primary-button" disabled={!result.hasNext} onClick={() => setPage((current) => current + 1)}>
            下一页
          </button>
        </div>
      ) : null}
    </section>
  );
}
