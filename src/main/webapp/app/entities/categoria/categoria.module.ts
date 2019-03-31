import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { EfielSharedModule } from 'app/shared';
import {
    CategoriaComponent,
    CategoriaDetailComponent,
    CategoriaUpdateComponent,
    CategoriaDeletePopupComponent,
    CategoriaDeleteDialogComponent,
    categoriaRoute,
    categoriaPopupRoute
} from './';

const ENTITY_STATES = [...categoriaRoute, ...categoriaPopupRoute];

@NgModule({
    imports: [EfielSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CategoriaComponent,
        CategoriaDetailComponent,
        CategoriaUpdateComponent,
        CategoriaDeleteDialogComponent,
        CategoriaDeletePopupComponent
    ],
    entryComponents: [CategoriaComponent, CategoriaUpdateComponent, CategoriaDeleteDialogComponent, CategoriaDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EfielCategoriaModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
