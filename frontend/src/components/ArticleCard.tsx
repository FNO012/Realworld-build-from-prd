import { useState } from 'react';
import Link from 'next/link';
import { api } from '@/lib/api';
import type { Article } from '@/types';

interface ArticleCardProps {
  article: Article;
}

export default function ArticleCard({ article }: ArticleCardProps) {
  const [favoriteCount, setFavoriteCount] = useState(article.favoritesCount || 0);
  const [isFavorited, setIsFavorited] = useState(article.favorited || false);
  const tags = article.tagList || [];

  const handleFavorite = async (e: React.MouseEvent) => {
    e.preventDefault();
    
    try {
      if (isFavorited) {
        // Unfavorite the article
        await api.delete(`/api/articles/${article.slug}/favorite`);
        setFavoriteCount(prev => prev - 1);
        setIsFavorited(false);
      } else {
        // Favorite the article
        await api.post(`/api/articles/${article.slug}/favorite`);
        setFavoriteCount(prev => prev + 1);
        setIsFavorited(true);
      }
    } catch (error) {
      console.error('Failed to toggle favorite:', error);
      // TODO: Show error message to user
    }
  };

  return (
    <div className="border-t border-gray-200 p-6">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center">
          <img
            src={article.author.image || '/images/smiley-cyrus.jpg'}
            alt={article.author.username}
            className="w-8 h-8 rounded-full mr-3"
          />
          <div className="text-sm">
            <Link 
              href={`/profile/${article.author.username}`}
              className="text-green-600 font-medium hover:text-green-800"
            >
              {article.author.username}
            </Link>
            <p className="text-gray-500">
              {new Date(article.createdAt).toLocaleDateString('en-US', {
                month: 'long',
                day: 'numeric',
                year: 'numeric'
              })}
            </p>
          </div>
        </div>
        <button 
          className={`px-2 py-1 rounded text-sm ${
            isFavorited 
              ? 'bg-green-600 text-white border border-green-600' 
              : 'border border-green-600 text-green-600 hover:bg-green-600 hover:text-white'
          }`}
          onClick={handleFavorite}
        >
          <i className="ion-heart mr-1"></i>
          {favoriteCount}
        </button>
      </div>
      
      <div className="mb-3">
        <Link 
          href={`/article/${article.slug}`}
          className="text-xl font-semibold text-gray-900 hover:text-gray-700"
        >
          {article.title}
        </Link>
        {article.description && (
          <p className="text-gray-600 mt-1">{article.description}</p>
        )}
      </div>
      
      <div className="flex justify-between items-center">
        <Link 
          href={`/article/${article.slug}`}
          className="text-xs text-gray-500 hover:text-gray-700"
        >
          Read more...
        </Link>
        <div className="flex space-x-1">
          {tags.length > 0 ? (
            tags.map((tag, index) => (
              <span 
                key={index}
                className="px-2 py-1 bg-gray-200 text-gray-700 text-xs rounded-full"
              >
                #{tag}
              </span>
            ))
          ) : (
            // If no tags, we can hide the tags section or show a placeholder
            <span className="px-2 py-1 bg-gray-200 text-gray-700 text-xs rounded-full opacity-0">
              &nbsp;
            </span>
          )}
        </div>
      </div>
    </div>
  );
}