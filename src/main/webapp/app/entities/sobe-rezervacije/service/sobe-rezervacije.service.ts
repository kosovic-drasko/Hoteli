import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISobeRezervacije, getSobeRezervacijeIdentifier } from '../sobe-rezervacije.model';

export type EntityResponseType = HttpResponse<ISobeRezervacije>;
export type EntityArrayResponseType = HttpResponse<ISobeRezervacije[]>;

@Injectable({ providedIn: 'root' })
export class SobeRezervacijeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sobe-rezervacijes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISobeRezervacije>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISobeRezervacije[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addSobeRezervacijeToCollectionIfMissing(
    sobeRezervacijeCollection: ISobeRezervacije[],
    ...sobeRezervacijesToCheck: (ISobeRezervacije | null | undefined)[]
  ): ISobeRezervacije[] {
    const sobeRezervacijes: ISobeRezervacije[] = sobeRezervacijesToCheck.filter(isPresent);
    if (sobeRezervacijes.length > 0) {
      const sobeRezervacijeCollectionIdentifiers = sobeRezervacijeCollection.map(
        sobeRezervacijeItem => getSobeRezervacijeIdentifier(sobeRezervacijeItem)!
      );
      const sobeRezervacijesToAdd = sobeRezervacijes.filter(sobeRezervacijeItem => {
        const sobeRezervacijeIdentifier = getSobeRezervacijeIdentifier(sobeRezervacijeItem);
        if (sobeRezervacijeIdentifier == null || sobeRezervacijeCollectionIdentifiers.includes(sobeRezervacijeIdentifier)) {
          return false;
        }
        sobeRezervacijeCollectionIdentifiers.push(sobeRezervacijeIdentifier);
        return true;
      });
      return [...sobeRezervacijesToAdd, ...sobeRezervacijeCollection];
    }
    return sobeRezervacijeCollection;
  }

  protected convertDateFromClient(sobeRezervacije: ISobeRezervacije): ISobeRezervacije {
    return Object.assign({}, sobeRezervacije, {
      datumDolaska: sobeRezervacije.datumDolaska?.isValid() ? sobeRezervacije.datumDolaska.format(DATE_FORMAT) : undefined,
      datumOdlaska: sobeRezervacije.datumOdlaska?.isValid() ? sobeRezervacije.datumOdlaska.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((sobeRezervacije: ISobeRezervacije) => {
        sobeRezervacije.datumDolaska = sobeRezervacije.datumDolaska ? dayjs(sobeRezervacije.datumDolaska) : undefined;
        sobeRezervacije.datumOdlaska = sobeRezervacije.datumOdlaska ? dayjs(sobeRezervacije.datumOdlaska) : undefined;
      });
    }
    return res;
  }
}
