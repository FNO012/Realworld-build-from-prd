import Link from 'next/link';
import RegisterForm from '@/components/forms/RegisterForm';

export default function RegisterPage() {
  return (
    <div className="min-h-screen bg-gray-50 py-16">
      <div className="max-w-md mx-auto px-4">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Sign up</h1>
          <Link href="/login" className="text-green-600 hover:text-green-700">
            Have an account?
          </Link>
        </div>
        
        <div className="bg-white rounded-lg shadow p-8">
          <RegisterForm />
        </div>
      </div>
    </div>
  );
}