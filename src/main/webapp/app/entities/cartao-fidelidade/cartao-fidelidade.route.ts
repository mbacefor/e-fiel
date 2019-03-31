import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';
import { CartaoFidelidadeService } from './cartao-fidelidade.service';
import { CartaoFidelidadeComponent } from './cartao-fidelidade.component';
import { CartaoFidelidadeDetailComponent } from './cartao-fidelidade-detail.component';
import { CartaoFidelidadeUpdateComponent } from './cartao-fidelidade-update.component';
import { CartaoFidelidadeDeletePopupComponent } from './cartao-fidelidade-delete-dialog.component';
import { ICartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';

@Injectable({ providedIn: 'root' })
export class CartaoFidelidadeResolve implements Resolve<ICartaoFidelidade> {
    constructor(private service: CartaoFidelidadeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICartaoFidelidade> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<CartaoFidelidade>) => response.ok),
                map((cartaoFidelidade: HttpResponse<CartaoFidelidade>) => cartaoFidelidade.body)
            );
        }
        return of(new CartaoFidelidade());
    }
}

export const cartaoFidelidadeRoute: Routes = [
    {
        path: '',
        component: CartaoFidelidadeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'efielApp.cartaoFidelidade.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: CartaoFidelidadeDetailComponent,
        resolve: {
            cartaoFidelidade: CartaoFidelidadeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.cartaoFidelidade.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CartaoFidelidadeUpdateComponent,
        resolve: {
            cartaoFidelidade: CartaoFidelidadeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.cartaoFidelidade.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: CartaoFidelidadeUpdateComponent,
        resolve: {
            cartaoFidelidade: CartaoFidelidadeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.cartaoFidelidade.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cartaoFidelidadePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CartaoFidelidadeDeletePopupComponent,
        resolve: {
            cartaoFidelidade: CartaoFidelidadeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efielApp.cartaoFidelidade.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
