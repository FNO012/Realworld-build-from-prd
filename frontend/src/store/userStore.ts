import { create } from 'zustand';
import { api } from '@/lib/api';
import type { User } from '@/types';

interface UserState {
  profiles: Record<string, User>;
  isLoading: boolean;
  error: string | null;
  
  // Actions
  fetchUserProfile: (username: string) => Promise<User>;
  followUser: (username: string) => Promise<void>;
  unfollowUser: (username: string) => Promise<void>;
  clearError: () => void;
}

export const useUserStore = create<UserState>((set) => ({
  profiles: {},
  isLoading: false,
  error: null,

  fetchUserProfile: async (username: string) => {
    set({ isLoading: true, error: null });
    
    try {
      const response = await api.get(`/api/users/${username}`);
      const user = response.data.data;
      
      set(state => ({
        profiles: { ...state.profiles, [username]: user },
        isLoading: false
      }));
      
      return user;
    } catch (error: unknown) {
      const err = error as { response?: { data?: { error?: { message?: string } } } };
      const errorMessage = err.response?.data?.error?.message || 'Failed to fetch user profile';
      set({ isLoading: false, error: errorMessage });
      throw error;
    }
  },

  followUser: async (username: string) => {
    set({ isLoading: true, error: null });
    
    try {
      const response = await api.post(`/api/users/${username}/follow`);
      const updatedUser = response.data.data.profile;
      
      set(state => ({
        profiles: { ...state.profiles, [username]: updatedUser },
        isLoading: false
      }));
    } catch (error: unknown) {
      const err = error as { response?: { data?: { error?: { message?: string } } } };
      const errorMessage = err.response?.data?.error?.message || 'Failed to follow user';
      set({ isLoading: false, error: errorMessage });
      throw error;
    }
  },

  unfollowUser: async (username: string) => {
    set({ isLoading: true, error: null });
    
    try {
      const response = await api.delete(`/api/users/${username}/follow`);
      const updatedUser = response.data.data.profile;
      
      set(state => ({
        profiles: { ...state.profiles, [username]: updatedUser },
        isLoading: false
      }));
    } catch (error: unknown) {
      const err = error as { response?: { data?: { error?: { message?: string } } } };
      const errorMessage = err.response?.data?.error?.message || 'Failed to unfollow user';
      set({ isLoading: false, error: errorMessage });
      throw error;
    }
  },

  clearError: () => set({ error: null })
}));