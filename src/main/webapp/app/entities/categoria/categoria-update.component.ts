import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICategoria } from 'app/shared/model/categoria.model';
import { CategoriaService } from './categoria.service';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { EmpresaService } from 'app/entities/empresa';

@Component({
    selector: 'jhi-categoria-update',
    templateUrl: './categoria-update.component.html'
})
export class CategoriaUpdateComponent implements OnInit {
    categoria: ICategoria;
    isSaving: boolean;

    empresas: IEmpresa[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected categoriaService: CategoriaService,
        protected empresaService: EmpresaService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ categoria }) => {
            this.categoria = categoria;
        });
        this.empresaService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IEmpresa[]>) => mayBeOk.ok),
                map((response: HttpResponse<IEmpresa[]>) => response.body)
            )
            .subscribe((res: IEmpresa[]) => (this.empresas = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.categoria.id !== undefined) {
            this.subscribeToSaveResponse(this.categoriaService.update(this.categoria));
        } else {
            this.subscribeToSaveResponse(this.categoriaService.create(this.categoria));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoria>>) {
        result.subscribe((res: HttpResponse<ICategoria>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
