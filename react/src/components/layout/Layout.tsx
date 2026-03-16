import { Outlet } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { Navbar } from './Navbar';

export function Layout() {
  return (
    // Das ist die "Shell" deiner App
    <div className="flex h-screen bg-slate-950 text-slate-200 overflow-hidden font-sans">
      
      {/* Sidebar bleibt immer links stehen */}
      <Sidebar />

      <div className="flex flex-col flex-1 relative">
        {/* Navbar bleibt immer oben stehen */}
        <Navbar />
        
        {/* Hier wechselt der Inhalt je nach URL */}
        <main className="flex-1 overflow-y-auto p-8 custom-scrollbar">
          <Outlet /> 
        </main>
      </div>
    </div>
  );
}