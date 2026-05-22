# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 常用命令

### 后端
- 启动后端开发服务：
  - `mvn -f backend/pom.xml spring-boot:run`
- 运行后端测试：
  - `mvn -f backend/pom.xml test`
- 打包后端：
  - `mvn -f backend/pom.xml package`

### 前端
- 安装前端依赖：
  - `npm install --prefix frontend`
- 启动前端开发服务：
  - `npm run dev --prefix frontend -- --host 127.0.0.1`
- 构建前端：
  - `npm run build --prefix frontend`

### 本地联调
- 后端默认端口：`8080`
- 前端由 Vite 提供服务，通常使用 `5173` 或后续递增可用端口
- 后端当前使用 H2 内存数据库；重启后端会清空运行期数据，并重新从 `backend/src/main/resources/demo-data/` 载入演示种子数据

## 高层架构

本仓库是一个前后端分离的博客 / 社区应用：
- 后端：Spring Boot
- 前端：React + TypeScript + Vite

### 后端架构

后端主代码位于：
- `backend/src/main/java/com/simpleblog/backend/`

后端按领域模块拆分，主要包括：
- `auth/`：注册、登录、当前用户信息、JWT、认证过滤器、Spring Security 集成
- `post/`：文章发布、文章详情、首页列表、标签聚合、分页与排序
- `comment/`：文章评论与评论状态管理
- `like/`：文章点赞、取消点赞、点赞状态查询
- `admin/`：管理员文章 / 评论 / 用户管理、筛选搜索、概览统计
- `me/`：当前登录用户的自管理能力，例如个人资料、我的文章、编辑自己的文章、隐藏自己的文章
- `tag/`：标签列表与标签文章聚合接口
- `user/`：用户实体、角色状态枚举、用户仓储
- `common/`：统一异常处理、分页响应对象、演示种子数据初始化
- `config/`：Spring Security 与安全相关配置

后端的重要设计约束：
- 内容治理依赖状态字段，而不是物理删除：
  - 文章：`PUBLISHED` / `HIDDEN`
  - 评论：`VISIBLE` / `HIDDEN`
  - 用户：`ACTIVE` / `DISABLED`
- 前台公开接口只返回可见内容；后台接口可以查看全部内容
- 管理员接口统一放在 `/api/admin/**` 下，并要求 `ADMIN` 角色
- 用户自管理接口统一放在 `/api/me/**` 下，并在服务端校验文章归属权
- 演示数据由 `common/DemoDataInitializer` 在启动时自动导入，数据源来自 `backend/src/main/resources/demo-data/` 下的 JSON / Markdown 资源
- 数据库结构由 Flyway 维护，本地默认运行在 H2 内存数据库之上

### 前端架构

前端代码位于：
- `frontend/src/`

前端主要结构：
- `api/`：按领域组织的接口调用封装，例如 `auth`、`posts`、`comments`、`likes`、`admin`、`me`、`tags`
- `pages/`：路由级页面，例如首页、登录注册、文章详情、个人中心、我的文章、后台页、标签页
- `router/`：统一路由定义，入口在 `AppRouter.tsx`
- `stores/`：Zustand 状态管理，目前主要用于认证状态
- `types.ts`：前端共享的数据结构定义

前端的重要实现模式：
- 登录状态由 Zustand 管理，并在应用启动时从本地存储恢复
- 后端校验错误通过 `frontend/src/api/error.ts` 统一提取与展示
- 后台页是一个集中式管理页，包含概览卡片与文章 / 评论 / 用户管理区块
- 首页与标签页都基于分页响应结构和排序参数工作，而不是直接加载全量文章列表

## 演示数据与内容资源

本仓库内置了本地开发用的演示用户和文章：
- 用户数据：`backend/src/main/resources/demo-data/users.json`
- 文章元信息：`backend/src/main/resources/demo-data/posts.json`
- 文章正文：`backend/src/main/resources/demo-data/articles/*.md`

这些资源文件是本地演示内容的事实来源。若需要调整演示文章或用户，优先修改这些资源，而不是重新把长文本硬编码回 Java 代码中。

## 文档结构

- 设计文档：`docs/design/`
  - 功能演进文档：`docs/design/features/`
  - 优化 / 交互 / 演示数据等文档：`docs/design/optimizations/`
  - 总索引：`docs/design/INDEX.md`
- 手工测试文档：`docs/test/`
  - 测试文档按模块拆分，而不是按阶段拆分

## 当前产品状态

当前项目已经具备：
- 认证系统
- 发帖与文章详情
- 评论
- 点赞
- 标签列表与标签聚合
- 用户自管理：个人中心 / 我的文章 / 编辑 / 隐藏文章
- 后台文章、评论、用户管理
- 后台概览与后台筛选 / 搜索

后续扩展时，优先沿用现有的领域模块划分方式，不要把跨领域逻辑随意塞入无关模块。