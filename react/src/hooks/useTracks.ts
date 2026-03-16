// src/hooks/useTracks.ts
import { useQuery } from '@tanstack/react-query';
import { trackService } from '../services/api';

export function useTracks() {
  return useQuery({
    queryKey: ['tracks'], 
    queryFn: trackService.getTracks // Hier wird nur noch die Referenz übergeben
  });
}