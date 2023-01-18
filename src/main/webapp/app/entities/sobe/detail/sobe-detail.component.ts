import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISobe } from '../sobe.model';

@Component({
  selector: 'jhi-sobe-detail',
  templateUrl: './sobe-detail.component.html',
})
export class SobeDetailComponent implements OnInit {
  sobe: ISobe | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sobe }) => {
      this.sobe = sobe;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
