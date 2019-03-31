import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'empresa',
                loadChildren: './empresa/empresa.module#EfielEmpresaModule'
            },
            {
                path: 'campanha',
                loadChildren: './campanha/campanha.module#EfielCampanhaModule'
            },
            {
                path: 'cartao-fidelidade',
                loadChildren: './cartao-fidelidade/cartao-fidelidade.module#EfielCartaoFidelidadeModule'
            },
            {
                path: 'selo-cartao',
                loadChildren: './selo-cartao/selo-cartao.module#EfielSeloCartaoModule'
            },
            {
                path: 'categoria',
                loadChildren: './categoria/categoria.module#EfielCategoriaModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EfielEntityModule {}
