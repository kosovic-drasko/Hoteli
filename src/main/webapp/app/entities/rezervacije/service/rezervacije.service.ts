import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRezervacije, getRezervacijeIdentifier } from '../rezervacije.model';

export type EntityResponseType = HttpResponse<IRezervacije>;
export type EntityArrayResponseType = HttpResponse<IRezervacije[]>;

@Injectable({ providedIn: 'root' })
export class RezervacijeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rezervacijes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rezervacije: IRezervacije): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rezervacije);
    return this.http
      .post<IRezervacije>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rezervacije: IRezervacije): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rezervacije);
    return this.http
      .put<IRezervacije>(`${this.resourceUrl}/${getRezervacijeIdentifier(rezervacije) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rezervacije: IRezervacije): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rezervacije);
    return this.http
      .patch<IRezervacije>(`${this.resourceUrl}/${getRezervacijeIdentifier(rezervacije) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRezervacije>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRezervacije[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRezervacijeToCollectionIfMissing(
    rezervacijeCollection: IRezervacije[],
    ...rezervacijesToCheck: (IRezervacije | null | undefined)[]
  ): IRezervacije[] {
    const rezervacijes: IRezervacije[] = rezervacijesToCheck.filter(isPresent);
    if (rezervacijes.length > 0) {
      const rezervacijeCollectionIdentifiers = rezervacijeCollection.map(rezervacijeItem => getRezervacijeIdentifier(rezervacijeItem)!);
      const rezervacijesToAdd = rezervacijes.filter(rezervacijeItem => {
        const rezervacijeIdentifier = getRezervacijeIdentifier(rezervacijeItem);
        if (rezervacijeIdentifier == null || rezervacijeCollectionIdentifiers.includes(rezervacijeIdentifier)) {
          return false;
        }
        rezervacijeCollectionIdentifiers.push(rezervacijeIdentifier);
        return true;
      });
      return [...rezervacijesToAdd, ...rezervacijeCollection];
    }
    return rezervacijeCollection;
  }

  protected convertDateFromClient(rezervacije: IRezervacije): IRezervacije {
    return Object.assign({}, rezervacije, {
      datumDolaska: rezervacije.datumDolaska?.isValid() ? rezervacije.datumDolaska.format(DATE_FORMAT) : undefined,
      datumOdlaska: rezervacije.datumOdlaska?.isValid() ? rezervacije.datumOdlaska.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datumDolaska = res.body.datumDolaska ? dayjs(res.body.datumDolaska) : undefined;
      res.body.datumOdlaska = res.body.datumOdlaska ? dayjs(res.body.datumOdlaska) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rezervacije: IRezervacije) => {
        rezervacije.datumDolaska = rezervacije.datumDolaska ? dayjs(rezervacije.datumDolaska) : undefined;
        rezervacije.datumOdlaska = rezervacije.datumOdlaska ? dayjs(rezervacije.datumOdlaska) : undefined;
      });
    }
    return res;
  }

  getBookingsByDate(startDate: string, endDate: string): Observable<any> {
    return this.http.get('http://localhost:9000/bookings/date/' + startDate + '/to/' + endDate);
  }
}
