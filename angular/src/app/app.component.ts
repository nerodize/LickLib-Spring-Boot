import { Component, inject, OnInit, signal } from '@angular/core';
import { DataService } from './services/data.service';
import { Track } from './models/types';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { RouterOutlet } from '@angular/router';
import { HomeComponent } from "./components/home/home.component";


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    NavbarComponent,
    SidebarComponent,
], // Wichtig für die Anzeige
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private dataService = inject(DataService);

  // intelligenter Container, scheinbar Binding-Ersatz
  tracks = signal<Track[]>([]);
  isLoading = signal<boolean>(true);
  errorMessage = signal<string | null>(null);

  ngOnInit() {
    this.dataService.getTracks().subscribe({
      next: (data) => {
        this.tracks.set(data); 
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Fehler beim Laden:', err);
        this.errorMessage.set('Backend nicht erreichbar. Hast du CORS aktiviert?');
        this.isLoading.set(false);
      }
    });
  }
}