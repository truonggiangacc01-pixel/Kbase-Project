'use client';

import { useState, useEffect, useRef } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { ArrowLeft, Search, UploadCloud, FileText, Download, Eye, Image as ImageIcon, Video, File, X, FileArchive, FileType } from 'lucide-react';
import { MOCK_PROJECTS, MOCK_DOCUMENTS, Role, Document } from '@/lib/mock-data';

export default function ProjectDetailsPage() {
  const params = useParams();
  const router = useRouter();
  const projectId = params.id as string;
  
  const [role, setRole] = useState<Role>('User');
  const [isUploadModalOpen, setIsUploadModalOpen] = useState(false);
  const [isDragging, setIsDragging] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  
  const project = MOCK_PROJECTS.find(p => p.id === projectId);
  const documents = MOCK_DOCUMENTS[projectId] || [];

  const filteredDocs = documents.filter(doc => 
    doc.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  useEffect(() => {
    const savedRole = localStorage.getItem('kbase_role') as Role;
    if (savedRole) setRole(savedRole);
  }, []);

  if (!project) {
    return <div className="min-h-screen flex items-center justify-center text-white">Project not found</div>;
  }

  const getFileIcon = (type: string) => {
    switch (type.toLowerCase()) {
      case 'pdf': case 'docx': case 'xlsx': case 'txt': case 'md': return <FileText className="text-blue-400" />;
      case 'image': case 'jpg': case 'png': return <ImageIcon className="text-emerald-400" />;
      case 'video': case 'mp4': return <Video className="text-purple-400" />;
      case 'zip': case 'rar': return <FileArchive className="text-amber-400" />;
      default: return <File className="text-gray-400" />;
    }
  };

  // Drag and Drop handlers
  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(true);
  };
  const handleDragLeave = () => setIsDragging(false);
  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
    // Handle file drop here
    setIsUploadModalOpen(false);
  };

  return (
    <div className="min-h-screen bg-[var(--background)]">
      {/* Navbar */}
      <header className="glass sticky top-0 z-40 border-b border-[var(--border)] px-6 py-4 flex items-center gap-4">
        <button onClick={() => router.push('/dashboard')} className="p-2 hover:bg-[var(--input)] rounded-lg text-gray-400 hover:text-white transition-colors">
          <ArrowLeft className="w-5 h-5" />
        </button>
        <div>
          <h1 className="font-semibold text-lg tracking-tight text-white">{project.name}</h1>
          <p className="text-xs text-gray-400">Project Documents</p>
        </div>
      </header>

      <main className="max-w-6xl mx-auto px-6 py-8">
        <div className="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500" />
            <input 
              type="text" 
              placeholder="Search documents..." 
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full bg-[var(--input)] border border-[var(--border)] rounded-lg py-2 pl-9 pr-4 text-sm text-white focus:outline-none focus:ring-2 focus:ring-indigo-500/50"
            />
          </div>
          
          <button 
            onClick={() => setIsUploadModalOpen(true)}
            className="bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg py-2 px-4 text-sm font-medium transition-colors flex items-center gap-2 shadow-lg shadow-indigo-500/20 whitespace-nowrap"
          >
            <UploadCloud className="w-4 h-4" />
            Upload File
          </button>
        </div>

        {/* Documents List */}
        <div className="glass-panel rounded-xl overflow-hidden border border-[var(--border)]">
          <div className="grid grid-cols-12 gap-4 p-4 border-b border-[var(--border)] bg-gray-900/50 text-xs font-medium text-gray-400 uppercase tracking-wider">
            <div className="col-span-6 md:col-span-5">Name</div>
            <div className="col-span-3 md:col-span-2 hidden md:block">Size</div>
            <div className="col-span-3 md:col-span-2 hidden sm:block">Uploaded By</div>
            <div className="col-span-6 md:col-span-2">Date</div>
            <div className="col-span-12 md:col-span-1 text-right">Actions</div>
          </div>
          
          {filteredDocs.length === 0 ? (
            <div className="p-12 text-center text-gray-400">
              <FileText className="w-12 h-12 mx-auto mb-3 opacity-20" />
              <p>No documents found.</p>
            </div>
          ) : (
            <div className="divide-y divide-[var(--border)]">
              {filteredDocs.map((doc) => (
                <div key={doc.id} className="grid grid-cols-12 gap-4 p-4 items-center hover:bg-gray-800/30 transition-colors group">
                  <div className="col-span-6 md:col-span-5 flex items-center gap-3">
                    <div className="w-8 h-8 rounded-lg bg-gray-800 flex items-center justify-center shrink-0">
                      {getFileIcon(doc.type)}
                    </div>
                    <span className="font-medium text-sm text-gray-200 truncate group-hover:text-indigo-300 transition-colors">{doc.name}</span>
                  </div>
                  <div className="col-span-3 md:col-span-2 hidden md:block text-sm text-gray-400">{doc.size}</div>
                  <div className="col-span-3 md:col-span-2 hidden sm:block text-sm text-gray-400">{doc.uploadedBy}</div>
                  <div className="col-span-6 md:col-span-2 text-sm text-gray-400">{doc.uploadedAt}</div>
                  <div className="col-span-12 md:col-span-1 flex items-center justify-end gap-2">
                    <button className="p-1.5 text-gray-400 hover:text-white hover:bg-gray-700 rounded-md transition-all" title="View">
                      <Eye className="w-4 h-4" />
                    </button>
                    <button className="p-1.5 text-gray-400 hover:text-indigo-400 hover:bg-indigo-500/10 rounded-md transition-all" title="Download">
                      <Download className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </main>

      {/* File Upload Modal */}
      {isUploadModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm animate-in fade-in duration-200 p-4">
          <div className="glass-panel w-full max-w-xl rounded-2xl shadow-2xl animate-in zoom-in-95 duration-200 overflow-hidden flex flex-col">
            <div className="flex items-center justify-between p-4 border-b border-[var(--border)]">
              <h2 className="text-lg font-semibold text-white">Upload Documents</h2>
              <button onClick={() => setIsUploadModalOpen(false)} className="text-gray-400 hover:text-white transition-colors">
                <X className="w-5 h-5" />
              </button>
            </div>
            
            <div className="p-6">
              <div 
                onDragOver={handleDragOver}
                onDragLeave={handleDragLeave}
                onDrop={handleDrop}
                className={`border-2 border-dashed rounded-xl p-10 flex flex-col items-center justify-center text-center transition-all cursor-pointer ${isDragging ? 'border-indigo-500 bg-indigo-500/10' : 'border-gray-600 hover:border-gray-500 hover:bg-gray-800/30'}`}
              >
                <div className={`w-16 h-16 rounded-full flex items-center justify-center mb-4 transition-colors ${isDragging ? 'bg-indigo-500/20 text-indigo-400' : 'bg-gray-800 text-gray-400'}`}>
                  <UploadCloud className="w-8 h-8" />
                </div>
                <h3 className="text-base font-medium text-white mb-1">Click to upload or drag and drop</h3>
                <p className="text-sm text-gray-400 mb-6 max-w-sm">
                  Upload project files, resources, and media here.
                </p>
                
                <div className="flex flex-wrap justify-center gap-3 text-xs text-gray-500">
                  <div className="flex items-center gap-1.5"><FileText className="w-3.5 h-3.5" /> PDF, DOC, XLS, PPT, MD, TXT</div>
                  <div className="flex items-center gap-1.5"><ImageIcon className="w-3.5 h-3.5" /> JPG, PNG, GIF, SVG</div>
                  <div className="flex items-center gap-1.5"><Video className="w-3.5 h-3.5" /> MP4, MOV, AVI</div>
                </div>
              </div>
            </div>
            
            <div className="p-4 border-t border-[var(--border)] bg-gray-900/50 flex justify-end gap-3">
              <button onClick={() => setIsUploadModalOpen(false)} className="px-4 py-2 text-sm font-medium text-gray-300 hover:text-white transition-colors">Cancel</button>
              {/* Dummy browse button */}
              <label className="bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg px-4 py-2 text-sm font-medium transition-colors shadow-lg shadow-indigo-500/20 cursor-pointer">
                Browse Files
                <input type="file" className="hidden" multiple />
              </label>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
