import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Campanha } from 'app/shared/model/campanha.model';
import { CampanhaService } from './campanha.service';
import { CampanhaComponent } from './campanha.component';
import { CampanhaDetailComponent } from './campanha-detail.component';
import { CampanhaUpdateComponent } from './campanha-update.component';
import { CampanhaDeletePopupComponent } from './campanha-delete-dialog.component';
import { ICampanha } from 'app/shared/model/campanha.model';

@Injectable({ providedIn: 'root' })
export class CampanhaResolve implements Resolve<ICampanha> {
    constructor(private service: CampanhaService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICampanha> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Campanha>) => response.ok),
                map((campanha: HttpResponse<Campanha>) => campanha.body)
            );
        }
        return of(new Campanha());
    }
}

export const campanhaRoute: Routes = [
    {
        path: '',
        component: CampanhaComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'efielApp.campanha.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: CampanhaDetailComponent,
        resolve: {
            campanha: CampanhaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.campanha.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CampanhaUpdateComponent,
        resolve: {
            campanha: CampanhaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.campanha.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: CampanhaUpdateComponent,
        resolve: {
            campanha: CampanhaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.campanha.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const campanhaPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CampanhaDeletePopupComponent,
        resolve: {
            campanha: CampanhaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.campanha.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
