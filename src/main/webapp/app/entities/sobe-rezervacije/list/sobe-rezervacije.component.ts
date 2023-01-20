import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';

import { ISobeRezervacije } from '../sobe-rezervacije.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { SobeRezervacijeService } from '../service/sobe-rezervacije.service';
import { IRezervacije } from '../../rezervacije/rezervacije.model';
import { RezervacijeDeleteDialogComponent } from '../../rezervacije/delete/rezervacije-delete-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-sobe-rezervacije',
  templateUrl: './sobe-rezervacije.component.html',
})
export class SobeRezervacijeComponent implements OnInit {
  sobeRezervacijes?: ISobeRezervacije[] | null;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  dolazak?: string;
  odlazak?: string;

  constructor(
    protected sobeRezervacijeService: SobeRezervacijeService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.sobeRezervacijeService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ISobeRezervacije[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(_index: number, item: ISobeRezervacije): number {
    return item.id!;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ISobeRezervacije[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/sobe-rezervacije'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.sobeRezervacijes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  booking(): any {
    this.sobeRezervacijeService.getBookingsByDate(this.dolazak, this.odlazak).subscribe({
      next: (res: HttpResponse<ISobeRezervacije[]>) => {
        this.sobeRezervacijes = res.body;
        console.log('to je....', this.dolazak);
      },
    });
  }

  rezervacije(): any {
    this.sobeRezervacijeService.getRezervacije(this.dolazak, this.odlazak).subscribe({
      next: (res: HttpResponse<ISobeRezervacije[]>) => {
        this.sobeRezervacijes = res.body;
        console.log('to je....', this.dolazak);
      },
    });
  }

  delete(sobaRezervacije: ISobeRezervacije): void {
    const modalRef = this.modalService.open(RezervacijeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rezervacije = sobaRezervacije;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }
}
