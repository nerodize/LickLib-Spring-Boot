export interface Track {
  id: string;          // UUID wird im JSON zum String
  title: string;
  artist: string;
  username: string;    // Der Name des Users, der es hochgeladen hat
  description?: string; // Optional (da @Size(max=500), aber evtl. null im Backend)
  duration: number;
}

export interface TrackCreate {
  title: string;
  description: string;
  artist: string;
  size: number;
  duration: number;
  creatorId: string; // hmm
}