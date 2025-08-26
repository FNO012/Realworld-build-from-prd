import { create } from 'zustand';
import { api } from '@/lib/api';
import type { User, LoginRequest, RegisterRequest } from '@/types';

interface AuthState {
  user: User | null;
  isLoggedIn: boolean;
  isLoading: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  register: (credentials: RegisterRequest) => Promise<void>;
  logout: () => void;
  initAuth: () => void;
}

export const useAuthStore = create<AuthState>((set, get) => ({
  user: null,
  isLoggedIn: false,
  isLoading: false,

  login: async (credentials: LoginRequest) => {
    set({ isLoading: true });
    try {
      const formData = new FormData();
      formData.append('email', credentials.email);
      formData.append('password', credentials.password);

      const response = await api.post('/login', formData);

      const token = response.data.token;
      if (token) {
        localStorage.setItem('token', token);
        
        // 임시 사용자 객체 생성 (실제 사용자 정보는 향후 /api/user API 구현 후 사용)
        const tempUser: User = {
          id: 1,
          username: credentials.email.split('@')[0],
          email: credentials.email,
          bio: '',
          image: '',
        };
        
        set({ 
          user: tempUser, 
          isLoggedIn: true, 
          isLoading: false 
        });
      }
    } catch (error) {
      set({ isLoading: false });
      throw error;
    }
  },

  register: async (credentials: RegisterRequest) => {
    set({ isLoading: true });
    try {
      await api.post('/api/users', credentials);
      
      await get().login({
        email: credentials.email,
        password: credentials.password,
      });
    } catch (error) {
      set({ isLoading: false });
      throw error;
    }
  },

  logout: () => {
    localStorage.removeItem('token');
    set({ 
      user: null, 
      isLoggedIn: false 
    });
  },

  initAuth: () => {
    const token = localStorage.getItem('token');
    if (token) {
      // 토큰이 있으면 로그인 상태로 설정 (향후 JWT 검증 로직 추가 예정)
      const tempUser: User = {
        id: 1,
        username: 'user',
        email: 'user@example.com',
        bio: '',
        image: '',
      };
      
      set({ 
        user: tempUser, 
        isLoggedIn: true 
      });
    }
  },
}));