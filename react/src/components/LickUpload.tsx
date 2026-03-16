/*
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { trackService } from '../services/api';
import type { TrackCreate } from '../types/track';

export function AddTrack() {
  const queryClient = useQueryClient();

  // Die Mutation ist das Gegenstück zum HttpClient.post
  const mutation = useMutation({
    mutationFn: (newTrack: TrackCreate) => trackService.createTrack(newTrack),
    onSuccess: () => {
      // Das hier ist der "Magic Trick": Aktualisiert deine Track-Liste automatisch
      queryClient.invalidateQueries({ queryKey: ['tracks'] });
    },
  });

 // In deiner AddTrack Komponente
const handleCreate = () => {
  const payload: TrackCreate = {
    title: "My New Track",
    description: "whatever",
    size: 69,
    artist: "Alex",
    duration: 180,
    creatorId: "d290f1ee-6c54-4b01-90e6-d701748f0851" // Die echte ID des aktuellen Users
  };
  
  mutation.mutate(payload);
};

  return (
    <div className="p-4 border border-slate-700 rounded-lg">
      <button 
        onClick={handleCreate}
        disabled={mutation.isPending}
        className="bg-emerald-600 px-4 py-2 rounded font-bold disabled:opacity-50"
      >
        {mutation.isPending ? 'Speichere...' : 'Dummy Track hinzufügen'}
      </button>
      {mutation.isError && <p className="text-red-500 mt-2">Fehler!</p>}
    </div>
  );
}
*/