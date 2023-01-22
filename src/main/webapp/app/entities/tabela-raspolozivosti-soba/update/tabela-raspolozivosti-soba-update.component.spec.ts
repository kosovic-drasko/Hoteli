import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TabelaRaspolozivostiSobaService } from '../service/tabela-raspolozivosti-soba.service';
import { ITabelaRaspolozivostiSoba, TabelaRaspolozivostiSoba } from '../tabela-raspolozivosti-soba.model';

import { TabelaRaspolozivostiSobaUpdateComponent } from './tabela-raspolozivosti-soba-update.component';

describe('TabelaRaspolozivostiSoba Management Update Component', () => {
  let comp: TabelaRaspolozivostiSobaUpdateComponent;
  let fixture: ComponentFixture<TabelaRaspolozivostiSobaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tabelaRaspolozivostiSobaService: TabelaRaspolozivostiSobaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TabelaRaspolozivostiSobaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TabelaRaspolozivostiSobaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TabelaRaspolozivostiSobaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tabelaRaspolozivostiSobaService = TestBed.inject(TabelaRaspolozivostiSobaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba = { id: 456 };

      activatedRoute.data = of({ tabelaRaspolozivostiSoba });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tabelaRaspolozivostiSoba));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TabelaRaspolozivostiSoba>>();
      const tabelaRaspolozivostiSoba = { id: 123 };
      jest.spyOn(tabelaRaspolozivostiSobaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tabelaRaspolozivostiSoba });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tabelaRaspolozivostiSoba }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tabelaRaspolozivostiSobaService.update).toHaveBeenCalledWith(tabelaRaspolozivostiSoba);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TabelaRaspolozivostiSoba>>();
      const tabelaRaspolozivostiSoba = new TabelaRaspolozivostiSoba();
      jest.spyOn(tabelaRaspolozivostiSobaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tabelaRaspolozivostiSoba });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tabelaRaspolozivostiSoba }));
      saveSubject.complete();

      // THEN
      expect(tabelaRaspolozivostiSobaService.create).toHaveBeenCalledWith(tabelaRaspolozivostiSoba);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TabelaRaspolozivostiSoba>>();
      const tabelaRaspolozivostiSoba = { id: 123 };
      jest.spyOn(tabelaRaspolozivostiSobaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tabelaRaspolozivostiSoba });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tabelaRaspolozivostiSobaService.update).toHaveBeenCalledWith(tabelaRaspolozivostiSoba);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
