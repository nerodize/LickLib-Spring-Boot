import { NavLink, Link } from 'react-router-dom';
import { useUsers } from '../../hooks/useUsers';
import { useBackendStatus } from '../../hooks/useBackendStatus';
import { Music, Users, Activity } from 'lucide-react';

export function Sidebar() {
  const { data: isOnline, isLoading } = useBackendStatus();
  const { data: users } = useUsers();

  return (
    <aside className="w-64 bg-slate-900 border-r border-slate-800 flex flex-col">
      {/* Das neue interaktive Logo */}
      <div className="p-8">
        <Link to="/" className="group flex items-center space-x-3 outline-none">
          <div className="w-10 h-10 bg-gradient-to-br from-violet-600 to-fuchsia-600 rounded-xl flex items-center justify-center text-white shadow-lg shadow-violet-500/20 group-hover:shadow-violet-500/40 group-hover:rotate-6 group-hover:scale-110 transition-all duration-300">
            <Activity size={22} strokeWidth={2.5} />
          </div>
          <div className="flex flex-col">
            <span className="text-xl font-black tracking-tighter text-slate-100 group-hover:text-white transition-colors">
              LICK<span className="text-violet-500">LIB</span>
            </span>
            <span className="text-[10px] font-bold text-slate-500 tracking-[0.2em] -mt-1 group-hover:text-slate-400 transition-colors">
              GUITAR ARCHIVE
            </span>
          </div>
        </Link>
      </div>

      <nav className="flex-1 px-4 space-y-2">
        <NavLink
          to="/tracks"
          className={({ isActive }) => `
            flex items-center space-x-3 p-3 rounded-xl transition-all
            ${isActive 
              ? 'bg-violet-600/10 text-violet-400 border border-violet-500/20 shadow-lg shadow-violet-500/5' 
              : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/50'}
          `}
        >
          <Music size={18} />
          <span className="font-medium">Alle Licks</span>
        </NavLink>
        
        <NavLink
          to="/users"
          className={({ isActive }) => `
            flex items-center space-x-3 p-3 rounded-xl transition-all
            ${isActive 
              ? 'bg-violet-600/10 text-violet-400 border border-violet-500/20 shadow-lg shadow-violet-500/5' 
              : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/50'}
          `}
        >
          <Users size={18} />
          <span className="font-medium flex-1">Community</span>
          <span className="text-xs opacity-50 bg-slate-800 px-2 py-0.5 rounded-full font-mono">
            {users?.length || 0}
          </span>
        </NavLink>
      </nav>

      {/* Status Section */}
      <div className="p-4 m-4 bg-slate-800/50 rounded-xl border border-slate-700">
        <p className="text-xs text-slate-500 uppercase font-bold tracking-widest">Status</p>
        <div className="flex items-center space-x-2 mt-1">
          <div className={`w-2 h-2 rounded-full ${isOnline ? 'bg-emerald-400 shadow-[0_0_8px_#34d399]' : 'bg-red-400 animate-pulse'}`} />
          <p className={`text-sm font-mono ${isOnline ? 'text-emerald-400' : 'text-red-400'}`}>
            {isLoading ? 'Checking...' : isOnline ? 'Backend Online' : 'Backend Offline'}
          </p>
        </div>
      </div>
    </aside>
  );
}