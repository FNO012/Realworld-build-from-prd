'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useArticleStore } from '@/store/articleStore';
import type { ArticleRequest } from '@/types';

export default function ArticleForm() {
  const router = useRouter();
  const { createArticle, isLoading } = useArticleStore();
  
  const [formData, setFormData] = useState<ArticleRequest>({
    title: '',
    description: '',
    body: '',
  });
  
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    try {
      const article = await createArticle(formData);
      router.push(`/article/${article.slug}`);
    } catch (err: unknown) {
      const error = err as { response?: { data?: { error?: { message?: string } } } };
      setError(error.response?.data?.error?.message || 'Failed to create article');
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          {error}
        </div>
      )}
      
      <div>
        <input
          type="text"
          name="title"
          placeholder="Article Title"
          value={formData.title}
          onChange={handleChange}
          required
          className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent text-lg"
        />
      </div>
      
      <div>
        <input
          type="text"
          name="description"
          placeholder="What's this article about?"
          value={formData.description || ''}
          onChange={handleChange}
          className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
        />
      </div>
      
      <div>
        <textarea
          name="body"
          placeholder="Write your article (in markdown)"
          value={formData.body}
          onChange={handleChange}
          required
          rows={12}
          className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent resize-none"
        />
      </div>
      
      <div>
        <input
          type="text"
          placeholder="Enter tags (separate with spaces)"
          className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
        />
      </div>
      
      <div className="flex justify-end">
        <button
          type="submit"
          disabled={isLoading}
          className="bg-green-600 text-white py-2 px-6 rounded-lg hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {isLoading ? 'Publishing...' : 'Publish Article'}
        </button>
      </div>
    </form>
  );
}