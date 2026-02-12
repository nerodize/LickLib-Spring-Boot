import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { DataService } from '../../services/data.service';
import { forkJoin } from 'rxjs';
import { User, Track } from '../../models/types';

@Component({
  selector: 'app-user-list',
  standalone: true,
  templateUrl: './user-list.html',
  styleUrl: './user-list.css'
})
export class UserListComponent implements OnInit {
  private dataService = inject(DataService);

  users = signal<User[]>([]);
  allTracks = signal<Track[]>([]);
  isLoading = signal(true);

  distinctUsers = computed(() => {
    const tracks = this.allTracks();
    
    const userMap = new Map<string, { username: string; count: number }>();

    tracks.forEach(track => {
      const current = userMap.get(track.username) || { username: track.username, count: 0 };
      userMap.set(track.username, { 
        username: track.username, 
        count: current.count + 1 
      });
    });

    return Array.from(userMap.values());
  });

  ngOnInit() {
    // forkJoin wartet, bis BEIDE Requests fertig sind
    forkJoin({
      users: this.dataService.getUsers(),
      tracks: this.dataService.getTracks()
    }).subscribe({
      next: (result) => {
        this.users.set(result.users);
        this.allTracks.set(result.tracks);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Fehler beim Laden der Daten', err);
        this.isLoading.set(false);
      }
    });
  }

  getTrackCount(username: string): number {
    return this.allTracks().filter(t => t.username === username).length;
  }
}