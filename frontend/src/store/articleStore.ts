import { create } from 'zustand';
import { api } from '@/lib/api';
import type { Article, ArticleRequest, Comment, CommentRequest } from '@/types';

interface ArticleState {
  articles: Article[];
  currentArticle: Article | null;
  comments: Comment[];
  isLoading: boolean;
  fetchArticles: () => Promise<void>;
  fetchArticle: (slug: string) => Promise<void>;
  createArticle: (article: ArticleRequest) => Promise<Article>;
  fetchComments: (slug: string) => Promise<void>;
  createComment: (slug: string, comment: CommentRequest) => Promise<void>;
}

export const useArticleStore = create<ArticleState>((set, get) => ({
  articles: [],
  currentArticle: null,
  comments: [],
  isLoading: false,

  fetchArticles: async () => {
    set({ isLoading: true });
    try {
      const response = await api.get('/api/articles');
      const articles = response.data.data || [];
      set({ articles, isLoading: false });
    } catch (error) {
      set({ isLoading: false });
      throw error;
    }
  },

  fetchArticle: async (slug: string) => {
    set({ isLoading: true });
    try {
      const response = await api.get(`/api/articles/${slug}`);
      const article = response.data.data;
      set({ currentArticle: article, isLoading: false });
    } catch (error) {
      set({ isLoading: false });
      throw error;
    }
  },

  createArticle: async (articleData: ArticleRequest) => {
    set({ isLoading: true });
    try {
      const response = await api.post('/api/articles', articleData);
      const article = response.data.data;
      set({ isLoading: false });
      return article;
    } catch (error) {
      set({ isLoading: false });
      throw error;
    }
  },

  fetchComments: async (slug: string) => {
    try {
      const response = await api.get(`/api/articles/${slug}/comments`);
      const comments = response.data.data || [];
      set({ comments });
    } catch (error) {
      throw error;
    }
  },

  createComment: async (slug: string, commentData: CommentRequest) => {
    try {
      await api.post(`/api/articles/${slug}/comments`, commentData);
      await get().fetchComments(slug);
    } catch (error) {
      throw error;
    }
  },
}));