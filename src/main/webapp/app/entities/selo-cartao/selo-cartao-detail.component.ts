import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISeloCartao } from 'app/shared/model/selo-cartao.model';

@Component({
    selector: 'jhi-selo-cartao-detail',
    templateUrl: './selo-cartao-detail.component.html'
})
export class SeloCartaoDetailComponent implements OnInit {
    seloCartao: ISeloCartao;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ seloCartao }) => {
            this.seloCartao = seloCartao;
        });
    }

    previousState() {
        window.history.back();
    }
}
