import { useTracks } from '../../hooks/useTracks';
import { Music } from 'lucide-react';

export function Navbar() {
  const { data: tracks } = useTracks();

  return (
    <header className="h-16 border-b border-slate-800 flex items-center justify-between px-8 bg-slate-950/50 backdrop-blur-md sticky top-0 z-40">
      <div className="flex items-center space-x-2">
        <span className="text-sm font-medium text-slate-500 uppercase tracking-widest">Dashboard</span>
      </div>
      
      <div className="flex items-center space-x-4">
        {/* Lila Badge für die Lick-Anzahl */}
        <div className="bg-violet-500/10 text-violet-400 px-3 py-1 rounded-lg text-xs font-bold border border-violet-500/20 flex items-center space-x-2">
          <Music size={14} />
          <span>{tracks?.length || 0} LICKS</span>
        </div>

        {/* Profil Avatar mit Lila-Gradient */}
        <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-violet-500 to-fuchsia-600 p-[2px] cursor-pointer hover:scale-105 transition-transform">
          <div className="w-full h-full rounded-[10px] bg-slate-900 flex items-center justify-center text-xs font-bold text-white">
            JD
          </div>
        </div>
      </div>
    </header>
  );
}