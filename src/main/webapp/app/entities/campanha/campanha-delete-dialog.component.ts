import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICampanha } from 'app/shared/model/campanha.model';
import { CampanhaService } from './campanha.service';

@Component({
    selector: 'jhi-campanha-delete-dialog',
    templateUrl: './campanha-delete-dialog.component.html'
})
export class CampanhaDeleteDialogComponent {
    campanha: ICampanha;

    constructor(protected campanhaService: CampanhaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.campanhaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'campanhaListModification',
                content: 'Deleted an campanha'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-campanha-delete-popup',
    template: ''
})
export class CampanhaDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ campanha }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CampanhaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.campanha = campanha;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/campanha', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/campanha', { outlets: { popup: null } }]);
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
