import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISobe, getSobeIdentifier } from '../sobe.model';

export type EntityResponseType = HttpResponse<ISobe>;
export type EntityArrayResponseType = HttpResponse<ISobe[]>;

@Injectable({ providedIn: 'root' })
export class SobeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sobes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sobe: ISobe): Observable<EntityResponseType> {
    return this.http.post<ISobe>(this.resourceUrl, sobe, { observe: 'response' });
  }

  update(sobe: ISobe): Observable<EntityResponseType> {
    return this.http.put<ISobe>(`${this.resourceUrl}/${getSobeIdentifier(sobe) as number}`, sobe, { observe: 'response' });
  }

  partialUpdate(sobe: ISobe): Observable<EntityResponseType> {
    return this.http.patch<ISobe>(`${this.resourceUrl}/${getSobeIdentifier(sobe) as number}`, sobe, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISobe>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISobe[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSobeToCollectionIfMissing(sobeCollection: ISobe[], ...sobesToCheck: (ISobe | null | undefined)[]): ISobe[] {
    const sobes: ISobe[] = sobesToCheck.filter(isPresent);
    if (sobes.length > 0) {
      const sobeCollectionIdentifiers = sobeCollection.map(sobeItem => getSobeIdentifier(sobeItem)!);
      const sobesToAdd = sobes.filter(sobeItem => {
        const sobeIdentifier = getSobeIdentifier(sobeItem);
        if (sobeIdentifier == null || sobeCollectionIdentifiers.includes(sobeIdentifier)) {
          return false;
        }
        sobeCollectionIdentifiers.push(sobeIdentifier);
        return true;
      });
      return [...sobesToAdd, ...sobeCollection];
    }
    return sobeCollection;
  }
}
