'use client';

import { useEffect } from 'react';
import { useParams } from 'next/navigation';
import { useArticleStore } from '@/store/articleStore';
import Comment from '@/components/Comment';
import CommentForm from '@/components/CommentForm';

export default function ArticlePage() {
  const params = useParams();
  const slug = params.slug as string;
  
  const { currentArticle, comments, isLoading, fetchArticle, fetchComments } = useArticleStore();

  useEffect(() => {
    if (slug) {
      fetchArticle(slug);
      fetchComments(slug);
    }
  }, [slug, fetchArticle, fetchComments]);

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 py-16">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <p className="text-gray-500">Loading article...</p>
        </div>
      </div>
    );
  }

  if (!currentArticle) {
    return (
      <div className="min-h-screen bg-gray-50 py-16">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <p className="text-gray-500">Article not found.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Article Header */}
      <div className="bg-gray-900 text-white py-16">
        <div className="max-w-4xl mx-auto px-4">
          <h1 className="text-4xl font-bold mb-4">{currentArticle.title}</h1>
          
          <div className="flex items-center">
            <img
              src={currentArticle.author.image || '/images/smiley-cyrus.jpg'}
              alt={currentArticle.author.username}
              className="w-8 h-8 rounded-full mr-3"
            />
            <div className="mr-6">
              <div className="text-green-400 font-medium">
                {currentArticle.author.username}
              </div>
              <div className="text-gray-400 text-sm">
                {new Date(currentArticle.createdAt).toLocaleDateString('en-US', {
                  month: 'long',
                  day: 'numeric',
                  year: 'numeric'
                })}
              </div>
            </div>
            
            <div className="flex items-center space-x-2">
              <button className="border border-gray-400 text-gray-300 px-3 py-1 rounded text-sm hover:bg-gray-700">
                <i className="ion-plus-round mr-1"></i>
                Follow {currentArticle.author.username}
              </button>
              
              <button className="border border-green-400 text-green-400 px-3 py-1 rounded text-sm hover:bg-green-600 hover:text-white">
                <i className="ion-heart mr-1"></i>
                Favorite Article
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Article Content */}
      <div className="max-w-4xl mx-auto px-4 py-8">
        <div className="bg-white rounded-lg shadow p-8 mb-8">
          <div className="prose max-w-none">
            <p className="text-lg text-gray-600 mb-6">{currentArticle.description}</p>
            <div className="whitespace-pre-wrap text-gray-800">
              {currentArticle.body}
            </div>
          </div>
          
          {/* Tags */}
          <div className="mt-8 pt-8 border-t border-gray-200">
            <div className="flex space-x-1">
              <span className="px-2 py-1 bg-gray-200 text-gray-700 text-xs rounded-full">
                #placeholder
              </span>
            </div>
          </div>
        </div>

        {/* Author Actions */}
        <div className="bg-white rounded-lg shadow p-6 mb-8">
          <div className="flex items-center justify-center">
            <img
              src={currentArticle.author.image || '/images/smiley-cyrus.jpg'}
              alt={currentArticle.author.username}
              className="w-12 h-12 rounded-full mr-4"
            />
            <div className="flex-1">
              <div className="text-lg font-medium text-gray-900">
                {currentArticle.author.username}
              </div>
              <div className="text-gray-600">
                {currentArticle.author.bio || `Follow ${currentArticle.author.username} to get more articles like this.`}
              </div>
            </div>
            <div className="flex items-center space-x-2">
              <button className="border border-gray-400 text-gray-600 px-3 py-2 rounded text-sm hover:bg-gray-100">
                <i className="ion-plus-round mr-1"></i>
                Follow {currentArticle.author.username}
              </button>
            </div>
          </div>
        </div>

        {/* Comments Section */}
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-semibold mb-6">Comments ({comments.length})</h3>
          
          <div className="mb-6">
            <CommentForm articleSlug={slug} />
          </div>
          
          <div>
            {comments.length > 0 ? (
              comments.map((comment) => (
                <Comment key={comment.id} comment={comment} />
              ))
            ) : (
              <p className="text-gray-500 text-center py-8">No comments yet.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}