import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DataService } from '../../services/data.service';
import { Router } from '@angular/router';
import { TrackCreate } from '../../models/types';

@Component({
  selector: 'app-track-create',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './track-create.html'
})
export class TrackCreateComponent {
  private fb = inject(FormBuilder);
  private dataService = inject(DataService);
  private router = inject(Router);

  trackForm = this.fb.group({
    title: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100)]],
    description: ['', [Validators.minLength(10), Validators.maxLength(200)]],
    artist: ['', [Validators.required]],
    duration: [0, [Validators.required, Validators.min(1)]],

    // hardcoded => id parsing fehlt
    creatorId: ['f47ac10b-58cc-4372-a567-0e02b2c3d479', [Validators.required]] 
  });

  onSubmit() {
    if (this.trackForm.valid) {
      const newTrack = {
        ...this.trackForm.value,
        size: 1024 // Dummy-Wert, @missing value parser
      } as TrackCreate;

      this.dataService.createTrack(newTrack).subscribe({
        next: () => {
          this.router.navigate(['/tracks']);
        },
        error: (err) => console.error('Upload fehlgeschlagen', err)
      });
    }
  }
}