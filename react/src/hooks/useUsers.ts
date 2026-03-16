import { useQuery } from '@tanstack/react-query';
import { UserService } from '../services/api';
import type { User } from '../types/user';

export function useUsers() {
  return useQuery<User[]>({
    queryKey: ['users'], // Der eindeutige Cache-Key für die User-Liste
    queryFn: UserService.getUsers,
    staleTime: 1000 * 60 * 10, // User-Daten ändern sich seltener, 10 Min cachen
  });
}