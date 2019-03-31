/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EfielTestModule } from '../../../test.module';
import { SeloCartaoDetailComponent } from 'app/entities/selo-cartao/selo-cartao-detail.component';
import { SeloCartao } from 'app/shared/model/selo-cartao.model';

describe('Component Tests', () => {
    describe('SeloCartao Management Detail Component', () => {
        let comp: SeloCartaoDetailComponent;
        let fixture: ComponentFixture<SeloCartaoDetailComponent>;
        const route = ({ data: of({ seloCartao: new SeloCartao(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfielTestModule],
                declarations: [SeloCartaoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SeloCartaoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SeloCartaoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.seloCartao).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
