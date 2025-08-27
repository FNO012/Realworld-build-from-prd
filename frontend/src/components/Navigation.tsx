'use client';

import Link from 'next/link';
import { useAuthStore } from '@/store/authStore';
import { useEffect } from 'react';

export default function Navigation() {
  const { user, isLoggedIn, logout, initAuth } = useAuthStore();

  useEffect(() => {
    initAuth();
  }, [initAuth]);

  return (
    <nav className="border-b border-gray-200">
      <div className="max-w-6xl mx-auto px-4">
        <div className="flex justify-between items-center h-16">
          <Link href="/" className="text-green-600 text-xl font-bold">
            conduit
          </Link>

          <div className="flex items-center space-x-4">
            <Link href="/" className="text-gray-600 hover:text-gray-900">
              Home
            </Link>

            {isLoggedIn ? (
              <>
                <Link href="/editor" className="text-gray-600 hover:text-gray-900">
                  <i className="ion-compose mr-1"></i>
                  New Article
                </Link>
                <Link href="/settings" className="text-gray-600 hover:text-gray-900">
                  <i className="ion-gear-a mr-1"></i>
                  Settings
                </Link>
                <Link
                  href={`/profile/${user?.username}`}
                  className="text-gray-600 hover:text-gray-900"
                >
                  {user?.username}
                </Link>
                <button
                  onClick={logout}
                  className="text-gray-600 hover:text-gray-900"
                >
                  Logout
                </button>
              </>
            ) : (
              <>
                <Link href="/login" className="text-gray-600 hover:text-gray-900">
                  Sign in
                </Link>
                <Link href="/register" className="text-gray-600 hover:text-gray-900">
                  Sign up
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}