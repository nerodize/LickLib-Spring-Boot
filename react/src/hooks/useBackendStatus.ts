import { useQuery } from "@tanstack/react-query";
import { UserService } from "../services/api";

export function useBackendStatus() {
  return useQuery({
    queryKey: ['health'],
    queryFn: UserService.ping,
    refetchInterval: 5000, // Prüfe alle 5 Sekunden automatisch
  });
}