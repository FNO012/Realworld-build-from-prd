'use client';

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { useUserStore } from '@/store/userStore';
import { useAuthStore } from '@/store/authStore';
import FollowButton from '@/components/FollowButton';
import { api } from '@/lib/api';
import type { Article } from '@/types';
import ArticleCard from '@/components/ArticleCard';

export default function ProfilePage() {
  const params = useParams();
  const username = params.username as string;
  
  const { fetchUserProfile, profiles } = useUserStore();
  const { user: currentUser, isLoggedIn } = useAuthStore();
  
  const [userArticles, setUserArticles] = useState<Article[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const profileUser = profiles[username];
  const isOwnProfile = isLoggedIn && currentUser?.username === username;

  useEffect(() => {
    const fetchProfileData = async () => {
      setIsLoading(true);
      setError(null);
      
      try {
        // Fetch user profile using store
        await fetchUserProfile(username);
        
        // Fetch user's articles
        const articlesResponse = await api.get(`/api/articles?author=${username}`);
        const articles = articlesResponse.data.data?.articles || articlesResponse.data.data || [];
        setUserArticles(articles);
      } catch (err) {
        setError('Failed to load profile data');
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };

    if (username) {
      fetchProfileData();
    }
  }, [username, fetchUserProfile]);

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 py-16">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <p className="text-gray-500">Loading profile...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gray-50 py-16">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <p className="text-red-500">{error}</p>
        </div>
      </div>
    );
  }

  if (!profileUser) {
    return (
      <div className="min-h-screen bg-gray-50 py-16">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <p className="text-gray-500">User not found.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Profile Header */}
      <div className="bg-gray-100 py-16">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <div className="mb-4">
            {profileUser.image ? (
              <img 
                src={profileUser.image} 
                alt={profileUser.username}
                className="w-24 h-24 rounded-full mx-auto object-cover"
              />
            ) : (
              <div className="w-24 h-24 rounded-full bg-gray-300 mx-auto flex items-center justify-center">
                <span className="text-2xl text-gray-600 font-bold">
                  {profileUser.username.charAt(0).toUpperCase()}
                </span>
              </div>
            )}
          </div>
          
          <h1 className="text-3xl font-bold text-gray-900 mb-2">
            {profileUser.username}
          </h1>
          
          {profileUser.bio && (
            <p className="text-gray-600 mb-4 max-w-2xl mx-auto">
              {profileUser.bio}
            </p>
          )}
          
          <div className="flex justify-center items-center gap-6 mb-4">
            <div className="text-center">
              <div className="font-semibold text-lg">{profileUser.followersCount || 0}</div>
              <div className="text-sm text-gray-600">Followers</div>
            </div>
            <div className="text-center">
              <div className="font-semibold text-lg">{profileUser.followingCount || 0}</div>
              <div className="text-sm text-gray-600">Following</div>
            </div>
          </div>
          
          <div className="flex justify-center">
            {isOwnProfile ? (
              <button className="px-4 py-2 border border-gray-400 text-gray-700 rounded hover:bg-gray-50 transition-colors">
                <i className="ion-gear-a mr-2"></i>
                Edit Profile Settings
              </button>
            ) : (
              <FollowButton user={profileUser} />
            )}
          </div>
        </div>
      </div>

      {/* Articles Section */}
      <div className="max-w-4xl mx-auto px-4 py-8">
        <div className="bg-white rounded-lg shadow">
          <div className="border-b border-gray-200">
            <div className="flex">
              <button className="px-6 py-3 text-green-600 border-b-2 border-green-600">
                My Articles
              </button>
              <button className="px-6 py-3 text-gray-500 hover:text-gray-700">
                Favorited Articles
              </button>
            </div>
          </div>

          {userArticles.length > 0 ? (
            <div>
              {userArticles.map((article) => (
                <ArticleCard key={article.slug} article={article} />
              ))}
            </div>
          ) : (
            <div className="p-6 text-center">
              <p className="text-gray-500">No articles are here... yet.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}