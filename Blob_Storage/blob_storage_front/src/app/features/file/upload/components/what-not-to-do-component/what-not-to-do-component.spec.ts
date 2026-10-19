import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WhatNotToDoComponent } from './what-not-to-do-component';

describe('WhatNotToDoComponent', () => {
  let component: WhatNotToDoComponent;
  let fixture: ComponentFixture<WhatNotToDoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WhatNotToDoComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(WhatNotToDoComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
