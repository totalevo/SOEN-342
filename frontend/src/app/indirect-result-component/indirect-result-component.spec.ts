import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndirectResultComponent } from './indirect-result-component';

describe('IndirectResultComponent', () => {
  let component: IndirectResultComponent;
  let fixture: ComponentFixture<IndirectResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndirectResultComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IndirectResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
