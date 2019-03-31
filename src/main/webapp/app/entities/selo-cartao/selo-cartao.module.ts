import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { EfielSharedModule } from 'app/shared';
import {
    SeloCartaoComponent,
    SeloCartaoDetailComponent,
    SeloCartaoUpdateComponent,
    SeloCartaoDeletePopupComponent,
    SeloCartaoDeleteDialogComponent,
    seloCartaoRoute,
    seloCartaoPopupRoute
} from './';

const ENTITY_STATES = [...seloCartaoRoute, ...seloCartaoPopupRoute];

@NgModule({
    imports: [EfielSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SeloCartaoComponent,
        SeloCartaoDetailComponent,
        SeloCartaoUpdateComponent,
        SeloCartaoDeleteDialogComponent,
        SeloCartaoDeletePopupComponent
    ],
    entryComponents: [SeloCartaoComponent, SeloCartaoUpdateComponent, SeloCartaoDeleteDialogComponent, SeloCartaoDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EfielSeloCartaoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
