import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive], // Wichtig!
  templateUrl: './sidebar.html', // oder dein Pfad
  styleUrl: './sidebar.css'
})
export class SidebarComponent {}