import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITabelaRaspolozivostiSoba, TabelaRaspolozivostiSoba } from '../tabela-raspolozivosti-soba.model';
import { TabelaRaspolozivostiSobaService } from '../service/tabela-raspolozivosti-soba.service';

@Component({
  selector: 'jhi-tabela-raspolozivosti-soba-update',
  templateUrl: './tabela-raspolozivosti-soba-update.component.html',
})
export class TabelaRaspolozivostiSobaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    brojSobe: [null, [Validators.required]],
    datumDolaska: [],
    datumOdlaska: [],
  });

  constructor(
    protected tabelaRaspolozivostiSobaService: TabelaRaspolozivostiSobaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tabelaRaspolozivostiSoba }) => {
      this.updateForm(tabelaRaspolozivostiSoba);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tabelaRaspolozivostiSoba = this.createFromForm();
    if (tabelaRaspolozivostiSoba.id !== undefined) {
      this.subscribeToSaveResponse(this.tabelaRaspolozivostiSobaService.update(tabelaRaspolozivostiSoba));
    } else {
      this.subscribeToSaveResponse(this.tabelaRaspolozivostiSobaService.create(tabelaRaspolozivostiSoba));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITabelaRaspolozivostiSoba>>): void {
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

  protected updateForm(tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba): void {
    this.editForm.patchValue({
      id: tabelaRaspolozivostiSoba.id,
      brojSobe: tabelaRaspolozivostiSoba.brojSobe,
      datumDolaska: tabelaRaspolozivostiSoba.datumDolaska,
      datumOdlaska: tabelaRaspolozivostiSoba.datumOdlaska,
    });
  }

  protected createFromForm(): ITabelaRaspolozivostiSoba {
    return {
      ...new TabelaRaspolozivostiSoba(),
      id: this.editForm.get(['id'])!.value,
      brojSobe: this.editForm.get(['brojSobe'])!.value,
      datumDolaska: this.editForm.get(['datumDolaska'])!.value,
      datumOdlaska: this.editForm.get(['datumOdlaska'])!.value,
    };
  }
}
