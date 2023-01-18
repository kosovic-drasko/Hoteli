import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRezervacije } from '../rezervacije.model';

@Component({
  selector: 'jhi-rezervacije-detail',
  templateUrl: './rezervacije-detail.component.html',
})
export class RezervacijeDetailComponent implements OnInit {
  rezervacije: IRezervacije | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rezervacije }) => {
      this.rezervacije = rezervacije;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
