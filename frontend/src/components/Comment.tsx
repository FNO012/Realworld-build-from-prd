import type { Comment } from '@/types';

interface CommentProps {
  comment: Comment;
}

export default function Comment({ comment }: CommentProps) {
  return (
    <div className="border border-gray-200 rounded-lg p-4 mb-4">
      <p className="text-gray-800 mb-4">{comment.body}</p>
      
      <div className="flex items-center justify-between">
        <div className="flex items-center">
          <img
            src={comment.author.image || '/images/smiley-cyrus.jpg'}
            alt={comment.author.username}
            className="w-6 h-6 rounded-full mr-2"
          />
          <span className="text-green-600 text-sm font-medium mr-2">
            {comment.author.username}
          </span>
          <span className="text-gray-500 text-xs">
            {new Date(comment.createdAt).toLocaleDateString('en-US', {
              month: 'long',
              day: 'numeric',
              year: 'numeric'
            })}
          </span>
        </div>
        
        {/* TODO: Add delete comment functionality for own comments */}
        <button className="text-red-500 text-sm hover:text-red-700">
          <i className="ion-trash-a mr-1"></i>
          Delete
        </button>
      </div>
    </div>
  );
}