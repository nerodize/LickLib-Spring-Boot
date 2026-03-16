import { useUsers } from '../../hooks/useUsers';
import { Info, User as UserIcon } from 'lucide-react';

export function UserList() {
  const { data: users, isLoading, error } = useUsers();

  if (isLoading) return <div className="p-8 text-slate-400 animate-pulse">Lade Community...</div>;
  if (error) return <div className="p-8 text-red-400">Fehler beim Laden der User!</div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-slate-100">LickLib Community</h1>
        <div className="text-sm font-medium text-slate-500 bg-slate-900 px-4 py-1.5 rounded-full border border-slate-800">
          <span className="text-violet-400">{users?.length}</span> Gitarristen online
        </div>
      </div>

      <div className="bg-slate-900 rounded-2xl border border-slate-800 overflow-hidden shadow-xl">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-slate-950/50 border-b border-slate-800">
              <th className="p-4 font-bold text-xs uppercase tracking-widest text-slate-500">Username</th>
              <th className="p-4 font-bold text-xs uppercase tracking-widest text-slate-500">Email</th>
              <th className="p-4 font-bold text-xs uppercase tracking-widest text-slate-500">User ID</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800/50">
            {users?.map((user) => (
              <tr key={user.id} className="hover:bg-violet-500/5 transition-colors group">
                <td className="p-4 flex items-center space-x-3">
                  <div className="w-9 h-9 rounded-xl bg-violet-500/10 flex items-center justify-center text-violet-400 border border-violet-500/20 group-hover:scale-110 transition-transform">
                    <UserIcon size={16} />
                  </div>
                  <span className="font-semibold text-slate-200">{user.username}</span>
                </td>
                <td className="p-4 text-slate-400 text-sm">{user.email}</td>
                <td className="p-4 text-[10px] font-mono text-slate-600">{user.id}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="p-4 bg-violet-500/5 border border-violet-500/10 rounded-2xl flex items-start space-x-3">
        <Info size={18} className="text-violet-500 mt-0.5" />
        <p className="text-xs text-slate-400 leading-relaxed italic">
          Diese Daten kommen direkt über den <span className="text-violet-400">UserService</span> aus deinem Spring Boot Backend. 
          React Query übernimmt das Caching für eine flüssige UI.
        </p>
      </div>
    </div>
  );
}