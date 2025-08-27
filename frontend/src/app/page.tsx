'use client';

import { useEffect, useState } from 'react';
import { useArticleStore } from '@/store/articleStore';
import { useAuthStore } from '@/store/authStore';
import ArticleCard from '@/components/ArticleCard';

export default function Home() {
  const { articles, isLoading, fetchArticles } = useArticleStore();
  const { isLoggedIn, user } = useAuthStore();
  const [activeFeed, setActiveFeed] = useState<'your' | 'global'>('global');
  const [selectedTag, setSelectedTag] = useState<string | null>(null);

  useEffect(() => {
    const loadArticles = async () => {
      let author: string | undefined;
      if (activeFeed === 'your' && isLoggedIn && user) {
        author = user.username;
      }
      
      await fetchArticles(author, selectedTag || undefined);
    };
    
    loadArticles();
  }, [activeFeed, isLoggedIn, user, selectedTag]);

  const handleFeedChange = (feed: 'your' | 'global') => {
    setActiveFeed(feed);
  };

  const handleTagClick = (tag: string) => {
    setSelectedTag(prevTag => prevTag === tag ? null : tag);
  };

  const popularTags = ['programming', 'javascript', 'web', 'coding'];

  return (
    <div className="bg-gray-50">
      {/* Hero Banner */}
      <div className="bg-green-600 text-white text-center py-16">
        <h1 className="text-4xl font-bold mb-2">conduit</h1>
        <p className="text-lg">A place to share your knowledge.</p>
      </div>

      <div className="max-w-6xl mx-auto px-4 py-8">
        <div className="flex gap-8">
          {/* Main Content */}
          <div className="flex-1">
            <div className="bg-white rounded-lg shadow">
              <div className="border-b border-gray-200">
                <div className="flex">
                  {isLoggedIn && (
                    <button 
                      className={`px-6 py-3 ${activeFeed === 'your' ? 'text-green-600 border-b-2 border-green-600' : 'text-gray-500 hover:text-gray-700'}`}
                      onClick={() => handleFeedChange('your')}
                    >
                      Your Feed
                    </button>
                  )}
                  <button 
                    className={`px-6 py-3 ${activeFeed === 'global' || !isLoggedIn ? 'text-green-600 border-b-2 border-green-600' : 'text-gray-500 hover:text-gray-700'}`}
                    onClick={() => handleFeedChange('global')}
                  >
                    Global Feed
                  </button>
                </div>
              </div>

              {isLoading ? (
                <div className="p-6 text-center">
                  <p className="text-gray-500">Loading articles...</p>
                </div>
              ) : articles.length > 0 ? (
                <div>
                  {articles.map((article) => (
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

          {/* Sidebar */}
          <div className="w-64">
            <div className="bg-gray-100 rounded-lg p-4">
              <h3 className="text-sm font-semibold text-gray-700 mb-3">Popular Tags</h3>
              <div className="flex flex-wrap gap-1">
                {popularTags.map((tag) => (
                  <span 
                    key={tag}
                    className={`px-2 py-1 text-white text-xs rounded-full cursor-pointer hover:bg-gray-600 ${
                      selectedTag === tag ? 'bg-green-500' : 'bg-gray-500'
                    }`}
                    onClick={() => handleTagClick(tag)}
                  >
                    #{tag}
                  </span>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
