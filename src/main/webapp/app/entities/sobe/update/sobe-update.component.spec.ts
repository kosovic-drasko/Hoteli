import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SobeService } from '../service/sobe.service';
import { ISobe, Sobe } from '../sobe.model';

import { SobeUpdateComponent } from './sobe-update.component';

describe('Sobe Management Update Component', () => {
  let comp: SobeUpdateComponent;
  let fixture: ComponentFixture<SobeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sobeService: SobeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SobeUpdateComponent],
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
      .overrideTemplate(SobeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SobeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sobeService = TestBed.inject(SobeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sobe: ISobe = { id: 456 };

      activatedRoute.data = of({ sobe });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sobe));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sobe>>();
      const sobe = { id: 123 };
      jest.spyOn(sobeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sobe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sobe }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sobeService.update).toHaveBeenCalledWith(sobe);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sobe>>();
      const sobe = new Sobe();
      jest.spyOn(sobeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sobe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sobe }));
      saveSubject.complete();

      // THEN
      expect(sobeService.create).toHaveBeenCalledWith(sobe);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sobe>>();
      const sobe = { id: 123 };
      jest.spyOn(sobeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sobe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sobeService.update).toHaveBeenCalledWith(sobe);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
