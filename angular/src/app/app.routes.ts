import { Routes } from '@angular/router';
import { TrackListComponent } from './components/track-list/track-list.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { HomeComponent } from './components/home/home.component';
import { TrackCreateComponent } from './components/track-create/track-create.component';
import { TrackDetailComponent } from './components/track-detail/track-detail.component';

export const routes: Routes = [


  { path: 'tracks', component: TrackListComponent },
  { path : 'users', component: UserListComponent },
  { path: 'home', component: HomeComponent },
  { path: 'tracks/create', component: TrackCreateComponent},
  { path: 'tracks/:id', component: TrackDetailComponent}

   // Leerer Pfad = Landing Page
];