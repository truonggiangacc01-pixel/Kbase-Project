'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { BookOpen, ChevronRight, Mail, Lock } from 'lucide-react';

export default function AuthPage() {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const router = useRouter();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Determine role based on email input
    let assignedRole = 'User';
    if (email.includes('admin')) assignedRole = 'Admin';
    else if (email.includes('owner')) assignedRole = 'Owner';
    
    // Save role to local storage
    localStorage.setItem('kbase_role', assignedRole);
    router.push('/dashboard');
  };

  return (
    <div className="min-h-screen flex items-center justify-center relative overflow-hidden">
      {/* Background decoration */}
      <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] rounded-full bg-indigo-600/20 blur-[120px] pointer-events-none" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[40%] h-[40%] rounded-full bg-purple-600/20 blur-[120px] pointer-events-none" />

      <div className="w-full max-w-md z-10 p-8">
        <div className="glass-panel rounded-2xl p-8 animate-in fade-in slide-in-from-bottom-8 duration-700">
          <div className="flex flex-col items-center mb-8">
            <div className="w-12 h-12 bg-indigo-500/20 rounded-xl flex items-center justify-center mb-4 border border-indigo-500/30">
              <BookOpen className="text-indigo-400 w-6 h-6" />
            </div>
            <h1 className="text-2xl font-bold tracking-tight text-white">
              {isLogin ? 'Welcome back to KBase' : 'Create an account'}
            </h1>
            <p className="text-sm text-gray-400 mt-2 text-center">
              {isLogin ? 'Enter your details to access your workspaces.' : 'Sign up to start managing your knowledge base.'}
            </p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <label className="text-xs font-medium text-gray-300">Email Address</label>
              <div className="relative">
                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500" />
                <input 
                  type="email" 
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="name@company.com" 
                  className="w-full bg-[var(--input)] border border-[var(--border)] rounded-lg py-2.5 pl-10 pr-4 text-sm text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50 transition-all placeholder:text-gray-600"
                />
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-xs font-medium text-gray-300">Password</label>
              <div className="relative">
                <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500" />
                <input 
                  type="password" 
                  required
                  placeholder="••••••••" 
                  className="w-full bg-[var(--input)] border border-[var(--border)] rounded-lg py-2.5 pl-10 pr-4 text-sm text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50 transition-all placeholder:text-gray-600"
                />
              </div>
            </div>

            <button 
              type="submit" 
              className="w-full bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg py-2.5 text-sm font-medium transition-colors flex items-center justify-center gap-2 mt-6 group"
            >
              {isLogin ? 'Sign In' : 'Sign Up'}
              <ChevronRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
            </button>
          </form>

          <div className="mt-6 text-center text-sm text-gray-400">
            {isLogin ? "Don't have an account? " : "Already have an account? "}
            <button 
              onClick={() => setIsLogin(!isLogin)} 
              className="text-indigo-400 hover:text-indigo-300 font-medium transition-colors"
            >
              {isLogin ? 'Sign up' : 'Sign in'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
