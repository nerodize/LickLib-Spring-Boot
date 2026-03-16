import axios from 'axios';
import type { Track, TrackCreate } from '../types/track';
import type { User } from '../types/user';


const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

export const trackService = {
  // GET: Mapped das JSON-Array vom Backend auf dein Track-Interface
  getTracks: async (): Promise<Track[]> => {
    const { data } = await api.get('/tracks/');
    return data;
  },

  // POST: Sendet ein TrackCreate-Objekt als JSON
  createTrack: async (newTrack: TrackCreate): Promise<Track> => {
    const { data } = await api.post('/tracks/', newTrack);
    return data;
  }
};

export const UserService = {
    // 1. Funktion: Alle User holen
    getUsers: async (): Promise<User[]> => {
        const { data } = await api.get('/users/findAll');
        return data;
    },

    // 2. Funktion: Backend-Check (Komma oben nicht vergessen!)
    ping: async (): Promise<boolean> => {
        try {
            // Wir nutzen /users/findAll, da wir wissen, dass dieser Endpunkt existiert
            await api.get('/users/findAll'); 
            return true;
        } catch (error) {
            return false;
        }
    }
};