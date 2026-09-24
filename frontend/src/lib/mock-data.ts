export type Role = 'Admin' | 'Owner' | 'User';

export interface User {
  id: string;
  email: string;
  name: string;
  role: Role;
  avatar?: string;
}

export interface Project {
  id: string;
  name: string;
  description: string;
  createdAt: string;
  ownerId: string;
  memberCount: number;
}

export interface Document {
  id: string;
  name: string;
  type: string; // 'pdf', 'docx', 'xlsx', 'image', 'video', etc.
  size: string;
  uploadedBy: string;
  uploadedAt: string;
  url: string;
}

export const CURRENT_USER: User = {
  id: 'u1',
  email: 'admin@kbase.com',
  name: 'Admin User',
  role: 'Admin',
  avatar: 'https://i.pravatar.cc/150?u=u1',
};

export const MOCK_PROJECTS: Project[] = [
  { id: 'p1', name: 'KBase Platform Redesign', description: 'Modernizing the UI/UX for the Knowledge Base platform.', createdAt: '2024-03-01', ownerId: 'u1', memberCount: 12 },
  { id: 'p2', name: 'Marketing Assets 2024', description: 'Banners, videos and copywriting for Q3 campaigns.', createdAt: '2024-02-15', ownerId: 'u2', memberCount: 5 },
  { id: 'p3', name: 'Backend API V2', description: 'Migration to Spring Boot 3.2 and Java 21.', createdAt: '2024-03-10', ownerId: 'u1', memberCount: 8 },
];

export const MOCK_DOCUMENTS: Record<string, Document[]> = {
  'p1': [
    { id: 'd1', name: 'UI_Guidelines_v2.pdf', type: 'pdf', size: '2.4 MB', uploadedBy: 'Alice', uploadedAt: '2 hours ago', url: '#' },
    { id: 'd2', name: 'Logo_Assets.zip', type: 'zip', size: '15 MB', uploadedBy: 'Bob', uploadedAt: '1 day ago', url: '#' },
    { id: 'd3', name: 'Mockup_Home.png', type: 'image', size: '4.1 MB', uploadedBy: 'Charlie', uploadedAt: '3 days ago', url: '#' },
    { id: 'd4', name: 'Requirements.docx', type: 'docx', size: '1.2 MB', uploadedBy: 'Admin', uploadedAt: '1 week ago', url: '#' },
    { id: 'd5', name: 'Promo_Video.mp4', type: 'video', size: '45 MB', uploadedBy: 'Alice', uploadedAt: '2 weeks ago', url: '#' },
  ]
};
