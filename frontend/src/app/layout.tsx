import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import './globals.css';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'KBase - Knowledge Base Platform',
  description: 'Manage and share your knowledge base with your team.',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" className="dark" suppressHydrationWarning>
      <body className={`${inter.className} min-h-screen bg-[var(--background)] text-[var(--foreground)] selection:bg-indigo-500/30`}>
        {children}
      </body>
    </html>
  );
}
