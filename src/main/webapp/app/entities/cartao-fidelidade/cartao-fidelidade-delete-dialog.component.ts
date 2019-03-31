import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';
import { CartaoFidelidadeService } from './cartao-fidelidade.service';

@Component({
    selector: 'jhi-cartao-fidelidade-delete-dialog',
    templateUrl: './cartao-fidelidade-delete-dialog.component.html'
})
export class CartaoFidelidadeDeleteDialogComponent {
    cartaoFidelidade: ICartaoFidelidade;

    constructor(
        protected cartaoFidelidadeService: CartaoFidelidadeService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cartaoFidelidadeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cartaoFidelidadeListModification',
                content: 'Deleted an cartaoFidelidade'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cartao-fidelidade-delete-popup',
    template: ''
})
export class CartaoFidelidadeDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cartaoFidelidade }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CartaoFidelidadeDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.cartaoFidelidade = cartaoFidelidade;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/cartao-fidelidade', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/cartao-fidelidade', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
