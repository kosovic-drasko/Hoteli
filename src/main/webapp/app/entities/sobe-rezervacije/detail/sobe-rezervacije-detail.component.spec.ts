import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SobeRezervacijeDetailComponent } from './sobe-rezervacije-detail.component';

describe('SobeRezervacije Management Detail Component', () => {
  let comp: SobeRezervacijeDetailComponent;
  let fixture: ComponentFixture<SobeRezervacijeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SobeRezervacijeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sobeRezervacije: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SobeRezervacijeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SobeRezervacijeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sobeRezervacije on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sobeRezervacije).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
