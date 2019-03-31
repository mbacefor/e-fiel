import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISeloCartao } from 'app/shared/model/selo-cartao.model';
import { SeloCartaoService } from './selo-cartao.service';
import { ICartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';
import { CartaoFidelidadeService } from 'app/entities/cartao-fidelidade';

@Component({
    selector: 'jhi-selo-cartao-update',
    templateUrl: './selo-cartao-update.component.html'
})
export class SeloCartaoUpdateComponent implements OnInit {
    seloCartao: ISeloCartao;
    isSaving: boolean;

    cartaofidelidades: ICartaoFidelidade[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected seloCartaoService: SeloCartaoService,
        protected cartaoFidelidadeService: CartaoFidelidadeService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ seloCartao }) => {
            this.seloCartao = seloCartao;
        });
        this.cartaoFidelidadeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICartaoFidelidade[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICartaoFidelidade[]>) => response.body)
            )
            .subscribe((res: ICartaoFidelidade[]) => (this.cartaofidelidades = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.seloCartao.id !== undefined) {
            this.subscribeToSaveResponse(this.seloCartaoService.update(this.seloCartao));
        } else {
            this.subscribeToSaveResponse(this.seloCartaoService.create(this.seloCartao));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeloCartao>>) {
        result.subscribe((res: HttpResponse<ISeloCartao>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCartaoFidelidadeById(index: number, item: ICartaoFidelidade) {
        return item.id;
    }
}
