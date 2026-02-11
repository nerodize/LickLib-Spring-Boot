import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { DataService } from '../../services/data.service';
import { Track } from '../../models/types';

@Component({
  selector: 'app-track-detail',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './track-detail.html'
})
export class TrackDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private dataService = inject(DataService);
  
  track = signal<Track | null>(null);

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.dataService.getTrackById(id).subscribe({
        next: (data) => this.track.set(data),
        error: (err) => console.error('Track nicht gefunden', err)
      });
    }
  }
}