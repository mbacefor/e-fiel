import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISeloCartao } from 'app/shared/model/selo-cartao.model';

type EntityResponseType = HttpResponse<ISeloCartao>;
type EntityArrayResponseType = HttpResponse<ISeloCartao[]>;

@Injectable({ providedIn: 'root' })
export class SeloCartaoService {
    public resourceUrl = SERVER_API_URL + 'api/selo-cartaos';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/selo-cartaos';

    constructor(protected http: HttpClient) {}

    create(seloCartao: ISeloCartao): Observable<EntityResponseType> {
        return this.http.post<ISeloCartao>(this.resourceUrl, seloCartao, { observe: 'response' });
    }

    update(seloCartao: ISeloCartao): Observable<EntityResponseType> {
        return this.http.put<ISeloCartao>(this.resourceUrl, seloCartao, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISeloCartao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISeloCartao[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISeloCartao[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
