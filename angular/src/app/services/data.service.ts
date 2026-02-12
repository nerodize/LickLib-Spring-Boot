import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Track, TrackCreate, User } from '../models/types';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class DataService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api'; 

  getTracks() {
    return this.http.get<Track[]>(`${this.apiUrl}/tracks/`);
  }

  getUsers() {
    return this.http.get<User[]>(`${this.apiUrl}/tracks/`);
  }

  createTrack(track: TrackCreate): Observable<any> {
  return this.http.post(`${this.apiUrl}/tracks/`, track);
  }

  getTrackById(id: string): Observable<Track> {
  return this.http.get<Track>(`${this.apiUrl}/tracks/${id}`);
}
}