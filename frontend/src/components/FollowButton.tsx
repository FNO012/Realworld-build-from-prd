'use client';

import { useState } from 'react';
import { useAuthStore } from '@/store/authStore';
import { useUserStore } from '@/store/userStore';
import type { User } from '@/types';

interface FollowButtonProps {
  user: User;
  className?: string;
}

export default function FollowButton({ user, className = '' }: FollowButtonProps) {
  const { isLoggedIn, user: currentUser } = useAuthStore();
  const { followUser, unfollowUser, isLoading } = useUserStore();
  const [optimisticFollowing, setOptimisticFollowing] = useState<boolean | null>(null);

  // 로그인하지 않았거나 자기 자신인 경우 버튼을 보여주지 않음
  if (!isLoggedIn || currentUser?.username === user.username) {
    return null;
  }

  const isCurrentlyFollowing = optimisticFollowing ?? user.following;

  const handleClick = async () => {
    if (isLoading) return;

    try {
      if (isCurrentlyFollowing) {
        setOptimisticFollowing(false);
        await unfollowUser(user.username);
      } else {
        setOptimisticFollowing(true);
        await followUser(user.username);
      }
      
      // Reset optimistic state after successful operation
      setOptimisticFollowing(null);
    } catch (error) {
      // Revert optimistic update on error
      setOptimisticFollowing(null);
      console.error('Follow/Unfollow error:', error);
    }
  };

  return (
    <button
      onClick={handleClick}
      disabled={isLoading}
      className={`
        px-4 py-2 rounded border text-sm font-medium transition-colors
        ${isCurrentlyFollowing 
          ? 'border-gray-400 text-gray-700 hover:bg-gray-50' 
          : 'border-green-500 text-green-500 hover:bg-green-500 hover:text-white'
        }
        ${isLoading ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'}
        ${className}
      `}
    >
      {isLoading ? (
        <span className="flex items-center">
          <svg className="animate-spin -ml-1 mr-2 h-4 w-4" fill="none" viewBox="0 0 24 24">
            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
            <path className="opacity-75" fill="currentColor" d="m4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          {isCurrentlyFollowing ? 'Unfollowing...' : 'Following...'}
        </span>
      ) : (
        <>
          {isCurrentlyFollowing ? (
            <>
              <i className="ion-minus-round mr-1"></i>
              Unfollow {user.username}
            </>
          ) : (
            <>
              <i className="ion-plus-round mr-1"></i>
              Follow {user.username}
            </>
          )}
        </>
      )}
    </button>
  );
}