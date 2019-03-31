import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ICampanha } from 'app/shared/model/campanha.model';
import { CampanhaService } from './campanha.service';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { EmpresaService } from 'app/entities/empresa';

@Component({
    selector: 'jhi-campanha-update',
    templateUrl: './campanha-update.component.html'
})
export class CampanhaUpdateComponent implements OnInit {
    campanha: ICampanha;
    isSaving: boolean;

    empresas: IEmpresa[];
    expiraDp: any;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected campanhaService: CampanhaService,
        protected empresaService: EmpresaService,
        protected elementRef: ElementRef,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ campanha }) => {
            this.campanha = campanha;
        });
        this.empresaService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IEmpresa[]>) => mayBeOk.ok),
                map((response: HttpResponse<IEmpresa[]>) => response.body)
            )
            .subscribe((res: IEmpresa[]) => (this.empresas = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.campanha, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.campanha.id !== undefined) {
            this.subscribeToSaveResponse(this.campanhaService.update(this.campanha));
        } else {
            this.subscribeToSaveResponse(this.campanhaService.create(this.campanha));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICampanha>>) {
        result.subscribe((res: HttpResponse<ICampanha>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackEmpresaById(index: number, item: IEmpresa) {
        return item.id;
    }
}
