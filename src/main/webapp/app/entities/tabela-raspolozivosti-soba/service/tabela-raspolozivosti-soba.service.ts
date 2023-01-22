import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITabelaRaspolozivostiSoba, getTabelaRaspolozivostiSobaIdentifier } from '../tabela-raspolozivosti-soba.model';

export type EntityResponseType = HttpResponse<ITabelaRaspolozivostiSoba>;
export type EntityArrayResponseType = HttpResponse<ITabelaRaspolozivostiSoba[]>;

@Injectable({ providedIn: 'root' })
export class TabelaRaspolozivostiSobaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tabela-raspolozivosti-sobas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tabelaRaspolozivostiSoba);
    return this.http
      .post<ITabelaRaspolozivostiSoba>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tabelaRaspolozivostiSoba);
    return this.http
      .put<ITabelaRaspolozivostiSoba>(
        `${this.resourceUrl}/${getTabelaRaspolozivostiSobaIdentifier(tabelaRaspolozivostiSoba) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tabelaRaspolozivostiSoba);
    return this.http
      .patch<ITabelaRaspolozivostiSoba>(
        `${this.resourceUrl}/${getTabelaRaspolozivostiSobaIdentifier(tabelaRaspolozivostiSoba) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITabelaRaspolozivostiSoba>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITabelaRaspolozivostiSoba[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTabelaRaspolozivostiSobaToCollectionIfMissing(
    tabelaRaspolozivostiSobaCollection: ITabelaRaspolozivostiSoba[],
    ...tabelaRaspolozivostiSobasToCheck: (ITabelaRaspolozivostiSoba | null | undefined)[]
  ): ITabelaRaspolozivostiSoba[] {
    const tabelaRaspolozivostiSobas: ITabelaRaspolozivostiSoba[] = tabelaRaspolozivostiSobasToCheck.filter(isPresent);
    if (tabelaRaspolozivostiSobas.length > 0) {
      const tabelaRaspolozivostiSobaCollectionIdentifiers = tabelaRaspolozivostiSobaCollection.map(
        tabelaRaspolozivostiSobaItem => getTabelaRaspolozivostiSobaIdentifier(tabelaRaspolozivostiSobaItem)!
      );
      const tabelaRaspolozivostiSobasToAdd = tabelaRaspolozivostiSobas.filter(tabelaRaspolozivostiSobaItem => {
        const tabelaRaspolozivostiSobaIdentifier = getTabelaRaspolozivostiSobaIdentifier(tabelaRaspolozivostiSobaItem);
        if (
          tabelaRaspolozivostiSobaIdentifier == null ||
          tabelaRaspolozivostiSobaCollectionIdentifiers.includes(tabelaRaspolozivostiSobaIdentifier)
        ) {
          return false;
        }
        tabelaRaspolozivostiSobaCollectionIdentifiers.push(tabelaRaspolozivostiSobaIdentifier);
        return true;
      });
      return [...tabelaRaspolozivostiSobasToAdd, ...tabelaRaspolozivostiSobaCollection];
    }
    return tabelaRaspolozivostiSobaCollection;
  }

  protected convertDateFromClient(tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba): ITabelaRaspolozivostiSoba {
    return Object.assign({}, tabelaRaspolozivostiSoba, {
      datumDolaska: tabelaRaspolozivostiSoba.datumDolaska?.isValid()
        ? tabelaRaspolozivostiSoba.datumDolaska.format(DATE_FORMAT)
        : undefined,
      datumOdlaska: tabelaRaspolozivostiSoba.datumOdlaska?.isValid()
        ? tabelaRaspolozivostiSoba.datumOdlaska.format(DATE_FORMAT)
        : undefined,
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
      res.body.forEach((tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba) => {
        tabelaRaspolozivostiSoba.datumDolaska = tabelaRaspolozivostiSoba.datumDolaska
          ? dayjs(tabelaRaspolozivostiSoba.datumDolaska)
          : undefined;
        tabelaRaspolozivostiSoba.datumOdlaska = tabelaRaspolozivostiSoba.datumOdlaska
          ? dayjs(tabelaRaspolozivostiSoba.datumOdlaska)
          : undefined;
      });
    }
    return res;
  }
}
