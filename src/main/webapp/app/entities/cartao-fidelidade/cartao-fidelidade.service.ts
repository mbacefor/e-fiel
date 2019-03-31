import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';

type EntityResponseType = HttpResponse<ICartaoFidelidade>;
type EntityArrayResponseType = HttpResponse<ICartaoFidelidade[]>;

@Injectable({ providedIn: 'root' })
export class CartaoFidelidadeService {
    public resourceUrl = SERVER_API_URL + 'api/cartao-fidelidades';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/cartao-fidelidades';

    constructor(protected http: HttpClient) {}

    create(cartaoFidelidade: ICartaoFidelidade): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cartaoFidelidade);
        return this.http
            .post<ICartaoFidelidade>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(cartaoFidelidade: ICartaoFidelidade): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cartaoFidelidade);
        return this.http
            .put<ICartaoFidelidade>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICartaoFidelidade>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICartaoFidelidade[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICartaoFidelidade[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(cartaoFidelidade: ICartaoFidelidade): ICartaoFidelidade {
        const copy: ICartaoFidelidade = Object.assign({}, cartaoFidelidade, {
            dataCriacao:
                cartaoFidelidade.dataCriacao != null && cartaoFidelidade.dataCriacao.isValid()
                    ? cartaoFidelidade.dataCriacao.format(DATE_FORMAT)
                    : null,
            dataPremio:
                cartaoFidelidade.dataPremio != null && cartaoFidelidade.dataPremio.isValid()
                    ? cartaoFidelidade.dataPremio.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dataCriacao = res.body.dataCriacao != null ? moment(res.body.dataCriacao) : null;
            res.body.dataPremio = res.body.dataPremio != null ? moment(res.body.dataPremio) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((cartaoFidelidade: ICartaoFidelidade) => {
                cartaoFidelidade.dataCriacao = cartaoFidelidade.dataCriacao != null ? moment(cartaoFidelidade.dataCriacao) : null;
                cartaoFidelidade.dataPremio = cartaoFidelidade.dataPremio != null ? moment(cartaoFidelidade.dataPremio) : null;
            });
        }
        return res;
    }
}
