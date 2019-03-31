/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EfielTestModule } from '../../../test.module';
import { CartaoFidelidadeDetailComponent } from 'app/entities/cartao-fidelidade/cartao-fidelidade-detail.component';
import { CartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';

describe('Component Tests', () => {
    describe('CartaoFidelidade Management Detail Component', () => {
        let comp: CartaoFidelidadeDetailComponent;
        let fixture: ComponentFixture<CartaoFidelidadeDetailComponent>;
        const route = ({ data: of({ cartaoFidelidade: new CartaoFidelidade(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfielTestModule],
                declarations: [CartaoFidelidadeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CartaoFidelidadeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CartaoFidelidadeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.cartaoFidelidade).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
