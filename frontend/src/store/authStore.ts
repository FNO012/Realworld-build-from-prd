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
  setUser: (user: User) => void;
}

export const useAuthStore = create<AuthState>((set, get) => ({
  user: null,
  isLoggedIn: false,
  isLoading: false,

  login: async (credentials: LoginRequest) => {
    set({ isLoading: true });
    try {
      const formData = new URLSearchParams();
      formData.append('email', credentials.email);
      formData.append('password', credentials.password);

      const response = await api.post('/login', formData, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      });

      const token = response.data.token;
      if (token) {
        localStorage.setItem('token', token);
        
        // Fetch user information after successful login
        try {
          const userResponse = await api.get('/api/users/me');
          const user = userResponse.data.data;
          set({ 
            user: user, 
            isLoggedIn: true, 
            isLoading: false 
          });
        } catch {
          // If fetching user fails, set user to null but still mark as logged in
          set({ 
            user: null, 
            isLoggedIn: true, 
            isLoading: false 
          });
        }
        
        // 로그인 성공 후 홈페이지로 이동
        if (typeof window !== 'undefined') {
          setTimeout(() => {
            window.location.href = '/';
          }, 100);
        }
        
        // Promise resolve를 명시적으로 반환
        return Promise.resolve();
      } else {
        set({ isLoading: false });
        throw new Error('토큰을 받지 못했습니다');
      }
    } catch (error) {
      set({ isLoading: false });
      throw error;
    }
  },

  register: async (credentials: RegisterRequest) => {
    set({ isLoading: true });
    try {
      // First register the user
      await api.post('/api/users', credentials);
      
      // Then login the user
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
      // 토큰이 있으면 로그인 상태로 설정
      // Try to fetch user information
      api.get('/api/users/me')
        .then((response) => {
          const user = response.data.data;
          set({ 
            user: user, 
            isLoggedIn: true 
          });
        })
        .catch(() => {
          // If fetching user fails, set user to null but still mark as logged in
          set({ 
            user: null, 
            isLoggedIn: true 
          });
        });
    }
  },
  
  setUser: (user: User) => {
    set({ user });
  },
}));