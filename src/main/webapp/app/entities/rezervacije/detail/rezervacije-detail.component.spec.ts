import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RezervacijeDetailComponent } from './rezervacije-detail.component';

describe('Rezervacije Management Detail Component', () => {
  let comp: RezervacijeDetailComponent;
  let fixture: ComponentFixture<RezervacijeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RezervacijeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rezervacije: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RezervacijeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RezervacijeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rezervacije on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rezervacije).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
