import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SeloCartao } from 'app/shared/model/selo-cartao.model';
import { SeloCartaoService } from './selo-cartao.service';
import { SeloCartaoComponent } from './selo-cartao.component';
import { SeloCartaoDetailComponent } from './selo-cartao-detail.component';
import { SeloCartaoUpdateComponent } from './selo-cartao-update.component';
import { SeloCartaoDeletePopupComponent } from './selo-cartao-delete-dialog.component';
import { ISeloCartao } from 'app/shared/model/selo-cartao.model';

@Injectable({ providedIn: 'root' })
export class SeloCartaoResolve implements Resolve<ISeloCartao> {
    constructor(private service: SeloCartaoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISeloCartao> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SeloCartao>) => response.ok),
                map((seloCartao: HttpResponse<SeloCartao>) => seloCartao.body)
            );
        }
        return of(new SeloCartao());
    }
}

export const seloCartaoRoute: Routes = [
    {
        path: '',
        component: SeloCartaoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.seloCartao.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SeloCartaoDetailComponent,
        resolve: {
            seloCartao: SeloCartaoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.seloCartao.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SeloCartaoUpdateComponent,
        resolve: {
            seloCartao: SeloCartaoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.seloCartao.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SeloCartaoUpdateComponent,
        resolve: {
            seloCartao: SeloCartaoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.seloCartao.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const seloCartaoPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SeloCartaoDeletePopupComponent,
        resolve: {
            seloCartao: SeloCartaoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.seloCartao.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
