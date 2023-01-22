import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TabelaRaspolozivostiSobaDetailComponent } from './tabela-raspolozivosti-soba-detail.component';

describe('TabelaRaspolozivostiSoba Management Detail Component', () => {
  let comp: TabelaRaspolozivostiSobaDetailComponent;
  let fixture: ComponentFixture<TabelaRaspolozivostiSobaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TabelaRaspolozivostiSobaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tabelaRaspolozivostiSoba: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TabelaRaspolozivostiSobaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TabelaRaspolozivostiSobaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tabelaRaspolozivostiSoba on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tabelaRaspolozivostiSoba).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
