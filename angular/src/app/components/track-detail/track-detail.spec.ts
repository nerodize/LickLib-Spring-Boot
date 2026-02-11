import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackDetailComponent } from './track-detail.component';

describe('TrackDetailComponent', () => {
  let component: TrackDetailComponent;
  let fixture: ComponentFixture<TrackDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrackDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrackDetailComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
