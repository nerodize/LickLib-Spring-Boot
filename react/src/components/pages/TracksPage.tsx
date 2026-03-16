import { useState } from 'react';
import { useTracks } from '../../hooks/useTracks';
import { useUsers } from '../../hooks/useUsers';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { trackService } from '../../services/api';
import { useForm } from 'react-hook-form';
import type { TrackCreate } from '../../types/track';
import { Plus, Clock, User, Music, X } from 'lucide-react';

export function TracksPage() {
  const [showModal, setShowModal] = useState(false);
  const queryClient = useQueryClient();
  const { data: tracks, isLoading } = useTracks();
  const { data: users } = useUsers();

  const { register, handleSubmit, reset } = useForm<TrackCreate>();

  const mutation = useMutation({
    mutationFn: (newTrack: TrackCreate) => trackService.createTrack(newTrack),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tracks'] });
      setShowModal(false);
      reset();
    },
    onError: (error) => alert("Fehler: " + error)
  });

  const onSubmit = (data: TrackCreate) => {
    mutation.mutate({ ...data, duration: Number(data.duration) });
  };

  if (isLoading) return <div className="p-8 text-slate-400 animate-pulse text-center">Licks werden geladen...</div>;

  return (
    <div className="space-y-8">
      <div className="flex justify-between items-end">
        <div>
          <h1 className="text-3xl font-bold text-slate-100 italic tracking-tight">GUITAR LICKS</h1>
          <p className="text-slate-500 text-sm mt-1">Deine persönliche Riff-Bibliothek</p>
        </div>
        <button 
          onClick={() => setShowModal(true)}
          className="
            relative group overflow-hidden
            flex items-center space-x-2 
            bg-gradient-to-br from-violet-600 to-fuchsia-600 
            hover:from-violet-500 hover:to-fuchsia-500
            px-8 py-3.5 rounded-2xl font-bold 
            transition-all duration-300
            shadow-[0_0_20px_rgba(139,92,246,0.3)] 
            hover:shadow-[0_0_30px_rgba(139,92,246,0.5)]
            active:scale-95 text-white
          "
        >
          {/* Subtiler Glanz-Effekt beim Hover */}
          <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/10 to-transparent -translate-x-full group-hover:animate-[shimmer_1.5s_infinite] pointer-events-none" />
          
          <div className="bg-white/20 p-1 rounded-lg">
            <Plus size={18} strokeWidth={3} />
          </div>
          <span className="tracking-tight text-lg">Lick hinzufügen</span>
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {tracks?.map(track => (
          <div key={track.id} className="bg-slate-900 p-6 rounded-2xl border border-slate-800 hover:border-violet-500/40 transition-all group">
            <div className="w-10 h-10 bg-slate-950 rounded-lg flex items-center justify-center text-violet-500 mb-4 group-hover:bg-violet-500 group-hover:text-white transition-all">
              <Music size={20} />
            </div>
            <h3 className="font-bold text-xl text-slate-100 mb-1">{track.title}</h3>
            <p className="text-slate-400 text-sm mb-6">{track.artist}</p>
            
            <div className="flex items-center justify-between pt-4 border-t border-slate-800/50">
              <div className="flex items-center space-x-2 text-slate-500">
                <User size={14} />
                <span className="text-xs font-medium">{track.username}</span>
              </div>
              <div className="flex items-center space-x-2 text-slate-500">
                <Clock size={14} />
                <span className="text-xs font-mono">{track.duration}s</span>
              </div>
            </div>
          </div>
        ))}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-slate-950/80 backdrop-blur-md flex items-center justify-center z-50 p-4">
          <div className="bg-slate-900 border border-slate-800 p-8 rounded-3xl w-full max-w-md shadow-2xl relative">
            <button onClick={() => setShowModal(false)} className="absolute top-6 right-6 text-slate-500 hover:text-white transition-colors">
              <X size={24} />
            </button>
            
            <h2 className="text-2xl font-bold mb-2 text-slate-100 font-mono italic">NEW_LICK.EXE</h2>
            <p className="text-slate-500 text-sm mb-8 italic">Mappe ein neues Riff in deine Datenbank.</p>
            
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
              <div className="space-y-1.5">
                <label className="text-[10px] font-bold uppercase text-slate-500 tracking-widest ml-1">Track Title</label>
                <input {...register("title", { required: true })} className="w-full bg-slate-950 border border-slate-800 p-3 rounded-xl focus:border-violet-500 focus:ring-1 focus:ring-violet-500 outline-none transition-all placeholder:text-slate-700" placeholder="E.g. Neon Nights" />
              </div>

              <div className="space-y-1.5">
                <label className="text-[10px] font-bold uppercase text-slate-500 tracking-widest ml-1">Artist</label>
                <input {...register("artist", { required: true })} className="w-full bg-slate-950 border border-slate-800 p-3 rounded-xl focus:border-violet-500 focus:ring-1 focus:ring-violet-500 outline-none transition-all placeholder:text-slate-700" placeholder="John Mayer" />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold uppercase text-slate-500 tracking-widest ml-1">Seconds</label>
                  <input type="number" {...register("duration")} className="w-full bg-slate-950 border border-slate-800 p-3 rounded-xl focus:border-violet-500 outline-none" placeholder="15" />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-bold uppercase text-slate-500 tracking-widest ml-1">Creator</label>
                  <select {...register("creatorId", { required: true })} className="w-full bg-slate-950 border border-slate-800 p-3 rounded-xl focus:border-violet-500 outline-none text-slate-300">
                    <option value="">Select...</option>
                    {users?.map(u => <option key={u.id} value={u.id}>{u.username}</option>)}
                  </select>
                </div>
              </div>

              <div className="flex space-x-3 pt-6">
                <button type="button" onClick={() => setShowModal(false)} className="flex-1 bg-slate-800 text-slate-300 py-3.5 rounded-xl font-bold hover:bg-slate-700 transition-all border border-slate-700">Cancel</button>
                <button type="submit" className="flex-1 bg-violet-600 text-white py-3.5 rounded-xl font-bold hover:bg-violet-500 transition-all shadow-lg shadow-violet-600/20">Save Lick</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}