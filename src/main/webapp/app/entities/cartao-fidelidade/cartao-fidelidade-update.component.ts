import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { ICartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';
import { CartaoFidelidadeService } from './cartao-fidelidade.service';
import { ICampanha } from 'app/shared/model/campanha.model';
import { CampanhaService } from 'app/entities/campanha';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-cartao-fidelidade-update',
    templateUrl: './cartao-fidelidade-update.component.html'
})
export class CartaoFidelidadeUpdateComponent implements OnInit {
    cartaoFidelidade: ICartaoFidelidade;
    isSaving: boolean;

    campanhas: ICampanha[];

    users: IUser[];
    dataCriacaoDp: any;
    dataPremioDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected cartaoFidelidadeService: CartaoFidelidadeService,
        protected campanhaService: CampanhaService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cartaoFidelidade }) => {
            this.cartaoFidelidade = cartaoFidelidade;
        });
        this.campanhaService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICampanha[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICampanha[]>) => response.body)
            )
            .subscribe((res: ICampanha[]) => (this.campanhas = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.cartaoFidelidade.id !== undefined) {
            this.subscribeToSaveResponse(this.cartaoFidelidadeService.update(this.cartaoFidelidade));
        } else {
            this.subscribeToSaveResponse(this.cartaoFidelidadeService.create(this.cartaoFidelidade));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICartaoFidelidade>>) {
        result.subscribe((res: HttpResponse<ICartaoFidelidade>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCampanhaById(index: number, item: ICampanha) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
