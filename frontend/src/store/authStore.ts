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
      formData.append('username', credentials.email);
      formData.append('password', credentials.password);

      const response = await api.post('/login', formData, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      });

      const token = response.data.token;
      if (token) {
        localStorage.setItem('token', token);
        
        const userResponse = await api.get('/api/user');
        const user = userResponse.data.data;
        
        set({ 
          user, 
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
      api.get('/api/user')
        .then((response) => {
          const user = response.data.data;
          set({ 
            user, 
            isLoggedIn: true 
          });
        })
        .catch(() => {
          localStorage.removeItem('token');
          set({ 
            user: null, 
            isLoggedIn: false 
          });
        });
    }
  },
}));