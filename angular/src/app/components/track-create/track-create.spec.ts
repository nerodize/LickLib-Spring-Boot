import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackCreateComponent } from './track-create.component';

describe('TrackCreateComponent', () => {
  let component: TrackCreateComponent;
  let fixture: ComponentFixture<TrackCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrackCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrackCreateComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
