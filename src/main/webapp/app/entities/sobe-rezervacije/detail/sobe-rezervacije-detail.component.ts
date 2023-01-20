import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISobeRezervacije } from '../sobe-rezervacije.model';

@Component({
  selector: 'jhi-sobe-rezervacije-detail',
  templateUrl: './sobe-rezervacije-detail.component.html',
})
export class SobeRezervacijeDetailComponent implements OnInit {
  sobeRezervacije: ISobeRezervacije | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sobeRezervacije }) => {
      this.sobeRezervacije = sobeRezervacije;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
