'use client';

import { useState } from 'react';
import { useArticleStore } from '@/store/articleStore';
import { useAuthStore } from '@/store/authStore';

interface CommentFormProps {
  articleSlug: string;
}

export default function CommentForm({ articleSlug }: CommentFormProps) {
  const { createComment } = useArticleStore();
  const { user, isLoggedIn } = useAuthStore();
  const [body, setBody] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!body.trim()) return;

    setIsSubmitting(true);
    setError(null);

    try {
      await createComment(articleSlug, { body });
      setBody('');
    } catch (err: unknown) {
      const error = err as { response?: { data?: { error?: { message?: string } } } };
      setError(error.response?.data?.error?.message || 'Failed to post comment');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!isLoggedIn) {
    return (
      <div className="text-center py-4">
        <p className="text-gray-500">
          <a href="/login" className="text-green-600 hover:text-green-700">
            Sign in
          </a>{' '}
          or{' '}
          <a href="/register" className="text-green-600 hover:text-green-700">
            sign up
          </a>{' '}
          to add comments on this article.
        </p>
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className="border border-gray-200 rounded-lg">
      {error && (
        <div className="bg-red-100 border-b border-red-200 text-red-700 px-4 py-3">
          {error}
        </div>
      )}
      
      <textarea
        value={body}
        onChange={(e) => setBody(e.target.value)}
        placeholder="Write a comment..."
        rows={3}
        className="w-full p-4 border-none resize-none focus:outline-none"
        required
      />
      
      <div className="bg-gray-50 px-4 py-3 flex items-center justify-between rounded-b-lg">
        <div className="flex items-center">
          <img
            src={user?.image || '/images/smiley-cyrus.jpg'}
            alt={user?.username}
            className="w-6 h-6 rounded-full mr-2"
          />
          <span className="text-sm text-gray-600">{user?.username}</span>
        </div>
        
        <button
          type="submit"
          disabled={isSubmitting || !body.trim()}
          className="bg-green-600 text-white px-4 py-2 rounded text-sm hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {isSubmitting ? 'Posting...' : 'Post Comment'}
        </button>
      </div>
    </form>
  );
}