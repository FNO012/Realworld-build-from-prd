export interface User {
  id: number;
  username: string;
  email: string;
  bio?: string;
  image?: string;
  following?: boolean;
  followersCount?: number;
  followingCount?: number;
}

export interface Article {
  slug: string;
  title: string;
  description?: string;
  body: string;
  createdAt: string;
  updatedAt: string;
  author: User;
  favoritesCount?: number;
  favorited?: boolean;
  tagList?: string[];
}

export interface Comment {
  id: number;
  body: string;
  createdAt: string;
  author: User;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface ArticleRequest {
  title: string;
  description?: string;
  body: string;
}

export interface CommentRequest {
  body: string;
}

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: {
    code: string;
    message: string;
    details?: Record<string, string[]>;
  };
}