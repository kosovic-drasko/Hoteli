import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RezervacijeService } from '../service/rezervacije.service';
import { IRezervacije, Rezervacije } from '../rezervacije.model';

import { RezervacijeUpdateComponent } from './rezervacije-update.component';

describe('Rezervacije Management Update Component', () => {
  let comp: RezervacijeUpdateComponent;
  let fixture: ComponentFixture<RezervacijeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rezervacijeService: RezervacijeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RezervacijeUpdateComponent],
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
      .overrideTemplate(RezervacijeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RezervacijeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rezervacijeService = TestBed.inject(RezervacijeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const rezervacije: IRezervacije = { id: 456 };

      activatedRoute.data = of({ rezervacije });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rezervacije));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rezervacije>>();
      const rezervacije = { id: 123 };
      jest.spyOn(rezervacijeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rezervacije });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rezervacije }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rezervacijeService.update).toHaveBeenCalledWith(rezervacije);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rezervacije>>();
      const rezervacije = new Rezervacije();
      jest.spyOn(rezervacijeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rezervacije });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rezervacije }));
      saveSubject.complete();

      // THEN
      expect(rezervacijeService.create).toHaveBeenCalledWith(rezervacije);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rezervacije>>();
      const rezervacije = { id: 123 };
      jest.spyOn(rezervacijeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rezervacije });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rezervacijeService.update).toHaveBeenCalledWith(rezervacije);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
