import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICampanha } from 'app/shared/model/campanha.model';

type EntityResponseType = HttpResponse<ICampanha>;
type EntityArrayResponseType = HttpResponse<ICampanha[]>;

@Injectable({ providedIn: 'root' })
export class CampanhaService {
    public resourceUrl = SERVER_API_URL + 'api/campanhas';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/campanhas';

    constructor(protected http: HttpClient) {}

    create(campanha: ICampanha): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(campanha);
        return this.http
            .post<ICampanha>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(campanha: ICampanha): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(campanha);
        return this.http
            .put<ICampanha>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICampanha>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICampanha[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICampanha[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(campanha: ICampanha): ICampanha {
        const copy: ICampanha = Object.assign({}, campanha, {
            expira: campanha.expira != null && campanha.expira.isValid() ? campanha.expira.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.expira = res.body.expira != null ? moment(res.body.expira) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((campanha: ICampanha) => {
                campanha.expira = campanha.expira != null ? moment(campanha.expira) : null;
            });
        }
        return res;
    }
}
