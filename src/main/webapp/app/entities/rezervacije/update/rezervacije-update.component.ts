import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRezervacije, Rezervacije } from '../rezervacije.model';
import { RezervacijeService } from '../service/rezervacije.service';

@Component({
  selector: 'jhi-rezervacije-update',
  templateUrl: './rezervacije-update.component.html',
})
export class RezervacijeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    brojSobe: [null, [Validators.required]],
    datumDolaska: [],
    datumOdlaska: [],
  });

  constructor(protected rezervacijeService: RezervacijeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rezervacije }) => {
      this.updateForm(rezervacije);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rezervacije = this.createFromForm();
    if (rezervacije.id !== undefined) {
      this.subscribeToSaveResponse(this.rezervacijeService.update(rezervacije));
    } else {
      this.subscribeToSaveResponse(this.rezervacijeService.create(rezervacije));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRezervacije>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(rezervacije: IRezervacije): void {
    this.editForm.patchValue({
      id: rezervacije.id,
      brojSobe: rezervacije.brojSobe,
      datumDolaska: rezervacije.datumDolaska,
      datumOdlaska: rezervacije.datumOdlaska,
    });
  }

  protected createFromForm(): IRezervacije {
    return {
      ...new Rezervacije(),
      id: this.editForm.get(['id'])!.value,
      brojSobe: this.editForm.get(['brojSobe'])!.value,
      datumDolaska: this.editForm.get(['datumDolaska'])!.value,
      datumOdlaska: this.editForm.get(['datumOdlaska'])!.value,
    };
  }
}
