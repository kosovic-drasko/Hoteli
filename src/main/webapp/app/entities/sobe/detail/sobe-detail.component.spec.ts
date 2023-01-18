import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SobeDetailComponent } from './sobe-detail.component';

describe('Sobe Management Detail Component', () => {
  let comp: SobeDetailComponent;
  let fixture: ComponentFixture<SobeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SobeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sobe: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SobeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SobeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sobe on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sobe).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
