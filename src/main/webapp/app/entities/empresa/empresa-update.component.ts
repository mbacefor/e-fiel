import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IEmpresa } from 'app/shared/model/empresa.model';
import { EmpresaService } from './empresa.service';
import { IUser, UserService } from 'app/core';
import { ICategoria } from 'app/shared/model/categoria.model';
import { CategoriaService } from 'app/entities/categoria';

@Component({
    selector: 'jhi-empresa-update',
    templateUrl: './empresa-update.component.html'
})
export class EmpresaUpdateComponent implements OnInit {
    empresa: IEmpresa;
    isSaving: boolean;

    users: IUser[];

    categorias: ICategoria[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected empresaService: EmpresaService,
        protected userService: UserService,
        protected categoriaService: CategoriaService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ empresa }) => {
            this.empresa = empresa;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.categoriaService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICategoria[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICategoria[]>) => response.body)
            )
            .subscribe((res: ICategoria[]) => (this.categorias = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.empresa.id !== undefined) {
            this.subscribeToSaveResponse(this.empresaService.update(this.empresa));
        } else {
            this.subscribeToSaveResponse(this.empresaService.create(this.empresa));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpresa>>) {
        result.subscribe((res: HttpResponse<IEmpresa>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackCategoriaById(index: number, item: ICategoria) {
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
