import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TabelaRaspolozivostiSobaService } from '../service/tabela-raspolozivosti-soba.service';

import { TabelaRaspolozivostiSobaComponent } from './tabela-raspolozivosti-soba.component';

describe('TabelaRaspolozivostiSoba Management Component', () => {
  let comp: TabelaRaspolozivostiSobaComponent;
  let fixture: ComponentFixture<TabelaRaspolozivostiSobaComponent>;
  let service: TabelaRaspolozivostiSobaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TabelaRaspolozivostiSobaComponent],
    })
      .overrideTemplate(TabelaRaspolozivostiSobaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TabelaRaspolozivostiSobaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TabelaRaspolozivostiSobaService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.tabelaRaspolozivostiSobas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
