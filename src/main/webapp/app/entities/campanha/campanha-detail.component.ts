import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ICampanha } from 'app/shared/model/campanha.model';

@Component({
    selector: 'jhi-campanha-detail',
    templateUrl: './campanha-detail.component.html'
})
export class CampanhaDetailComponent implements OnInit {
    campanha: ICampanha;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ campanha }) => {
            this.campanha = campanha;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
