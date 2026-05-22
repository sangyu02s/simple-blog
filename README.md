# Simple Blog

> 本项目由 **Claude Code** 与 **GPT-5.4** 协同开发实现。

Simple Blog 是一个前后端分离的博客 / 社区应用，支持用户注册登录、发帖、评论、点赞、标签浏览、个人内容管理以及后台内容治理能力。

## 项目特性

当前项目已经具备以下能力：

- 认证系统
  - 用户注册
  - 用户登录
  - 登录态恢复
  - 禁用用户登录限制

- 文章能力
  - 发布文章
  - 文章列表
  - 文章详情
  - 标签聚合页
  - 分页与排序

- 互动能力
  - 评论
  - 点赞

- 用户自管理
  - 个人中心
  - 我的文章
  - 编辑自己的文章
  - 隐藏自己的文章

- 后台管理
  - 后台概览
  - 后台文章管理
  - 后台评论管理
  - 后台用户管理
  - 后台筛选与搜索

- 本地演示数据
  - 预置普通用户与管理员账号
  - 预置中文散文、英文故事、英文诗歌相关示例文章

## 技术栈

### 后端
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Flyway
- H2（本地开发）
- Maven

### 前端
- React
- TypeScript
- Vite
- React Router
- Zustand
- Axios

## 项目结构

```text
simple-blog/
├── backend/   # Spring Boot 后端
├── frontend/  # React + TypeScript 前端
├── docs/      # 设计文档、测试文档、运行说明等
└── CLAUDE.md  # 仓库级 Claude Code 使用说明
```

## 启动方式

### 启动后端
```bash
mvn -f backend/pom.xml spring-boot:run
```

后端默认地址：
```text
http://127.0.0.1:8080
```

### 启动前端
```bash
npm install --prefix frontend
npm run dev --prefix frontend -- --host 127.0.0.1
```

前端通常运行在：
```text
http://127.0.0.1:5173
```

如果端口被占用，Vite 会自动切换到其他可用端口。

## 构建与测试

### 后端测试
```bash
mvn -f backend/pom.xml test
```

### 后端打包
```bash
mvn -f backend/pom.xml package
```

### 前端构建
```bash
npm run build --prefix frontend
```

## 演示账号

本地开发环境会自动加载演示种子数据。

### 管理员账号
- 用户名：`admin`
- 密码：`admin123456`

### 普通用户账号
- `linxi / password123`
- `muyu / password123`
- `claire / password123`
- `ethan / password123`

## 文档说明

- 仓库级开发说明：`CLAUDE.md`
- 设计文档：`docs/design/`
- 手工测试文档：`docs/test/`
- 项目运行说明：`docs/项目运行说明.md`
- 技术栈说明：`docs/项目技术栈说明.md`

## 当前定位

这是一个已经具备完整基础能力的博客社区项目，适合：
- 本地开发与联调
- 项目演示
- 功能扩展与持续迭代
