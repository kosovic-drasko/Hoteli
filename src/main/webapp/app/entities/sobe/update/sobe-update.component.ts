import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISobe, Sobe } from '../sobe.model';
import { SobeService } from '../service/sobe.service';

@Component({
  selector: 'jhi-sobe-update',
  templateUrl: './sobe-update.component.html',
})
export class SobeUpdateComponent implements OnInit {
  isSaving = false;
  sobes?: ISobe | undefined;
  editForm = this.fb.group({
    id: [],
    brojSobe: [null, [Validators.required]],
    cijena: [null, [Validators.required]],
  });

  constructor(protected sobeService: SobeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sobe }) => {
      this.updateForm(sobe);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sobe = this.createFromForm();
    if (sobe.id !== undefined) {
      this.subscribeToSaveResponse(this.sobeService.update(sobe));
    } else {
      this.subscribeToSaveResponse(this.sobeService.create(sobe));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISobe>>): void {
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

  protected updateForm(sobe: ISobe): void {
    this.editForm.patchValue({
      id: sobe.id,
      brojSobe: sobe.brojSobe,
      cijena: sobe.cijena,
    });
  }

  protected createFromForm(): ISobe {
    return {
      ...new Sobe(),
      id: this.editForm.get(['id'])!.value,
      brojSobe: this.editForm.get(['brojSobe'])!.value,
      cijena: this.editForm.get(['cijena'])!.value,
    };
  }
}
