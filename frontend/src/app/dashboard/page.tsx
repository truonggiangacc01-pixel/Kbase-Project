'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Plus, Folder, Users, Search, MoreVertical, LogOut } from 'lucide-react';
import { MOCK_PROJECTS, Role, Project } from '@/lib/mock-data';

export default function DashboardPage() {
  const router = useRouter();
  const [role, setRole] = useState<Role>('User');
  const [projects, setProjects] = useState<Project[]>(MOCK_PROJECTS);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isInviteModalOpen, setIsInviteModalOpen] = useState(false);
  const [selectedProjectId, setSelectedProjectId] = useState<string | null>(null);

  useEffect(() => {
    const savedRole = localStorage.getItem('kbase_role') as Role;
    if (savedRole) setRole(savedRole);
  }, []);

  const handleCreateProject = (e: React.FormEvent) => {
    e.preventDefault();
    setIsCreateModalOpen(false);
    // In a real app, we would add the project to the state
  };

  const handleInvite = (e: React.FormEvent) => {
    e.preventDefault();
    setIsInviteModalOpen(false);
  };

  const handleLogout = () => {
    localStorage.removeItem('kbase_role');
    router.push('/');
  };

  return (
    <div className="min-h-screen bg-[var(--background)]">
      {/* Navbar */}
      <header className="glass sticky top-0 z-40 border-b border-[var(--border)] px-6 py-4 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 bg-indigo-500 rounded-lg flex items-center justify-center shadow-lg shadow-indigo-500/20">
            <Folder className="text-white w-4 h-4" />
          </div>
          <span className="font-semibold text-lg tracking-tight">KBase</span>
        </div>
        <div className="flex items-center gap-4">
          <div className="text-sm">
            <span className="text-gray-400">Role: </span>
            <span className="text-indigo-400 font-medium">{role}</span>
          </div>
          <button onClick={handleLogout} className="text-gray-400 hover:text-white transition-colors">
            <LogOut className="w-5 h-5" />
          </button>
        </div>
      </header>

      <main className="max-w-6xl mx-auto px-6 py-12">
        <div className="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
          <div>
            <h1 className="text-3xl font-bold tracking-tight text-white mb-2">Projects</h1>
            <p className="text-gray-400 text-sm">Manage and collaborate on your knowledge bases.</p>
          </div>
          
          <div className="flex items-center gap-3">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500" />
              <input 
                type="text" 
                placeholder="Search projects..." 
                className="w-full md:w-64 bg-[var(--input)] border border-[var(--border)] rounded-lg py-2 pl-9 pr-4 text-sm text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
              />
            </div>
            
            {(role === 'Admin' || role === 'Owner') && (
              <button 
                onClick={() => setIsCreateModalOpen(true)}
                className="bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg py-2 px-4 text-sm font-medium transition-colors flex items-center gap-2 shadow-lg shadow-indigo-500/20 whitespace-nowrap"
              >
                <Plus className="w-4 h-4" />
                New Project
              </button>
            )}
          </div>
        </div>

        {/* Project Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {projects.map((project) => (
            <div 
              key={project.id} 
              className="glass-panel rounded-xl p-5 hover:border-indigo-500/30 transition-all cursor-pointer group"
              onClick={() => router.push(`/project/${project.id}`)}
            >
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-gray-800 rounded-lg flex items-center justify-center border border-gray-700">
                  <Folder className="text-indigo-400 w-5 h-5 group-hover:scale-110 transition-transform" />
                </div>
                <button className="text-gray-500 hover:text-white p-1 rounded-md hover:bg-[var(--input)]" onClick={(e) => e.stopPropagation()}>
                  <MoreVertical className="w-4 h-4" />
                </button>
              </div>
              
              <h3 className="text-lg font-medium text-white mb-1 group-hover:text-indigo-300 transition-colors">{project.name}</h3>
              <p className="text-sm text-gray-400 mb-6 line-clamp-2">{project.description}</p>
              
              <div className="flex items-center justify-between mt-auto pt-4 border-t border-[var(--border)]">
                <div className="flex items-center gap-1.5 text-xs text-gray-400">
                  <Users className="w-3.5 h-3.5" />
                  <span>{project.memberCount} members</span>
                </div>
                
                {(role === 'Admin' || role === 'Owner') && (
                  <button 
                    onClick={(e) => {
                      e.stopPropagation();
                      setSelectedProjectId(project.id);
                      setIsInviteModalOpen(true);
                    }}
                    className="text-xs font-medium text-indigo-400 hover:text-indigo-300 px-2 py-1 rounded hover:bg-indigo-500/10 transition-colors"
                  >
                    Invite
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      </main>

      {/* Create Project Modal */}
      {isCreateModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm animate-in fade-in duration-200">
          <div className="glass-panel w-full max-w-md rounded-2xl p-6 shadow-2xl animate-in zoom-in-95 duration-200">
            <h2 className="text-xl font-semibold text-white mb-1">Create New Project</h2>
            <p className="text-sm text-gray-400 mb-6">Set up a new workspace for your team.</p>
            
            <form onSubmit={handleCreateProject} className="space-y-4">
              <div className="space-y-2">
                <label className="text-xs font-medium text-gray-300">Project Name</label>
                <input type="text" required placeholder="E.g., Engineering Docs" className="w-full bg-[var(--input)] border border-[var(--border)] rounded-lg py-2 px-3 text-sm text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50" />
              </div>
              <div className="space-y-2">
                <label className="text-xs font-medium text-gray-300">Description</label>
                <textarea rows={3} placeholder="Brief description of the project..." className="w-full bg-[var(--input)] border border-[var(--border)] rounded-lg py-2 px-3 text-sm text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50 resize-none"></textarea>
              </div>
              
              <div className="flex justify-end gap-3 pt-4">
                <button type="button" onClick={() => setIsCreateModalOpen(false)} className="px-4 py-2 text-sm font-medium text-gray-300 hover:text-white transition-colors">Cancel</button>
                <button type="submit" className="bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg px-4 py-2 text-sm font-medium transition-colors shadow-lg shadow-indigo-500/20">Create Project</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Invite Member Modal */}
      {isInviteModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm animate-in fade-in duration-200">
          <div className="glass-panel w-full max-w-md rounded-2xl p-6 shadow-2xl animate-in zoom-in-95 duration-200">
            <h2 className="text-xl font-semibold text-white mb-1">Invite Members</h2>
            <p className="text-sm text-gray-400 mb-6">Add collaborators to your project.</p>
            
            <form onSubmit={handleInvite} className="space-y-4">
              <div className="space-y-2">
                <label className="text-xs font-medium text-gray-300">Email Address</label>
                <input type="email" required placeholder="colleague@company.com" className="w-full bg-[var(--input)] border border-[var(--border)] rounded-lg py-2 px-3 text-sm text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50" />
              </div>
              <div className="space-y-2">
                <label className="text-xs font-medium text-gray-300">Role</label>
                <select className="w-full bg-[var(--input)] border border-[var(--border)] rounded-lg py-2 px-3 text-sm text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50 appearance-none">
                  <option value="user">User (View & Upload)</option>
                  <option value="owner">Owner (Manage)</option>
                </select>
              </div>
              
              <div className="flex justify-end gap-3 pt-4">
                <button type="button" onClick={() => setIsInviteModalOpen(false)} className="px-4 py-2 text-sm font-medium text-gray-300 hover:text-white transition-colors">Cancel</button>
                <button type="submit" className="bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg px-4 py-2 text-sm font-medium transition-colors shadow-lg shadow-indigo-500/20">Send Invite</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
