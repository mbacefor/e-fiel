import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';

@Component({
    selector: 'jhi-cartao-fidelidade-detail',
    templateUrl: './cartao-fidelidade-detail.component.html'
})
export class CartaoFidelidadeDetailComponent implements OnInit {
    cartaoFidelidade: ICartaoFidelidade;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cartaoFidelidade }) => {
            this.cartaoFidelidade = cartaoFidelidade;
        });
    }

    previousState() {
        window.history.back();
    }
}
