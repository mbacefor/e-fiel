import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISeloCartao } from 'app/shared/model/selo-cartao.model';
import { SeloCartaoService } from './selo-cartao.service';

@Component({
    selector: 'jhi-selo-cartao-delete-dialog',
    templateUrl: './selo-cartao-delete-dialog.component.html'
})
export class SeloCartaoDeleteDialogComponent {
    seloCartao: ISeloCartao;

    constructor(
        protected seloCartaoService: SeloCartaoService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.seloCartaoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'seloCartaoListModification',
                content: 'Deleted an seloCartao'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-selo-cartao-delete-popup',
    template: ''
})
export class SeloCartaoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ seloCartao }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SeloCartaoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.seloCartao = seloCartao;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/selo-cartao', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/selo-cartao', { outlets: { popup: null } }]);
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
