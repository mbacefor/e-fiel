import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { EfielSharedModule } from 'app/shared';
import {
    CampanhaComponent,
    CampanhaDetailComponent,
    CampanhaUpdateComponent,
    CampanhaDeletePopupComponent,
    CampanhaDeleteDialogComponent,
    campanhaRoute,
    campanhaPopupRoute
} from './';

const ENTITY_STATES = [...campanhaRoute, ...campanhaPopupRoute];

@NgModule({
    imports: [EfielSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CampanhaComponent,
        CampanhaDetailComponent,
        CampanhaUpdateComponent,
        CampanhaDeleteDialogComponent,
        CampanhaDeletePopupComponent
    ],
    entryComponents: [CampanhaComponent, CampanhaUpdateComponent, CampanhaDeleteDialogComponent, CampanhaDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EfielCampanhaModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
