import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WhatToDoComponent } from './what-to-do-component';

describe('WhatToDoComponent', () => {
  let component: WhatToDoComponent;
  let fixture: ComponentFixture<WhatToDoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WhatToDoComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(WhatToDoComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
