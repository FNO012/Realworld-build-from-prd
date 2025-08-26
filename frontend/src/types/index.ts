export interface User {
  id: number;
  username: string;
  email: string;
  bio?: string;
  image?: string;
}

export interface Article {
  id: number;
  slug: string;
  title: string;
  description?: string;
  body: string;
  createdAt: string;
  updatedAt: string;
  author: User;
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