import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataService } from '../../services/data.service'; 
import { Track } from '../../models/types';
import { RouterLink } from '@angular/router'; 

@Component({
  selector: 'app-track-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './track-list.html',
  styleUrl: './track-list.css'
})
export class TrackListComponent implements OnInit {
  private dataService = inject(DataService);

  // Daten-Container (Signals)
  tracks = signal<Track[]>([]);
  searchQuery = signal('');
  isLoading = signal<boolean>(true);
  errorMessage = signal<string | null>(null);

  formatDuration(seconds: number): string {
  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  return `${mins}:${secs.toString().padStart(2, '0')}`;

  }

  // Liste wird im HTML für den @for-Loop genutzt
  filteredTracks = computed(() => {
    const query = this.searchQuery().toLowerCase();
    if (!query) return this.tracks();

    return this.tracks().filter(track => 
      track.title.toLowerCase().includes(query) || 
      track.artist.toLowerCase().includes(query) ||
      track.username.toLocaleLowerCase().includes(query)
    );
  });

  onSearch(event: Event) {
  const input = event.target as HTMLInputElement;
  this.searchQuery.set(input.value);
  }

  ngOnInit() {
    this.dataService.getTracks().subscribe({
      next: (data) => {
        this.tracks.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Backend nicht erreichbar.' + err);
        this.isLoading.set(false);
      }
    });
  }
}