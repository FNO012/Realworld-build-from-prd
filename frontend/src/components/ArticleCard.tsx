import Link from 'next/link';
import type { Article } from '@/types';

interface ArticleCardProps {
  article: Article;
}

export default function ArticleCard({ article }: ArticleCardProps) {
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
        <button className="border border-green-600 text-green-600 px-2 py-1 rounded text-sm hover:bg-green-600 hover:text-white">
          <i className="ion-heart mr-1"></i>
          {/* TODO: Add favorite count */}
          0
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
          {/* TODO: Add tags functionality */}
          <span className="px-2 py-1 bg-gray-200 text-gray-700 text-xs rounded-full">
            #placeholder
          </span>
        </div>
      </div>
    </div>
  );
}