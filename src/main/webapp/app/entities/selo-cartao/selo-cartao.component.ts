import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISeloCartao } from 'app/shared/model/selo-cartao.model';
import { AccountService } from 'app/core';
import { SeloCartaoService } from './selo-cartao.service';

@Component({
    selector: 'jhi-selo-cartao',
    templateUrl: './selo-cartao.component.html'
})
export class SeloCartaoComponent implements OnInit, OnDestroy {
    seloCartaos: ISeloCartao[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected seloCartaoService: SeloCartaoService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.seloCartaoService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<ISeloCartao[]>) => res.ok),
                    map((res: HttpResponse<ISeloCartao[]>) => res.body)
                )
                .subscribe((res: ISeloCartao[]) => (this.seloCartaos = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.seloCartaoService
            .query()
            .pipe(
                filter((res: HttpResponse<ISeloCartao[]>) => res.ok),
                map((res: HttpResponse<ISeloCartao[]>) => res.body)
            )
            .subscribe(
                (res: ISeloCartao[]) => {
                    this.seloCartaos = res;
                    this.currentSearch = '';
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSeloCartaos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISeloCartao) {
        return item.id;
    }

    registerChangeInSeloCartaos() {
        this.eventSubscriber = this.eventManager.subscribe('seloCartaoListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
