/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { EfielTestModule } from '../../../test.module';
import { SeloCartaoComponent } from 'app/entities/selo-cartao/selo-cartao.component';
import { SeloCartaoService } from 'app/entities/selo-cartao/selo-cartao.service';
import { SeloCartao } from 'app/shared/model/selo-cartao.model';

describe('Component Tests', () => {
    describe('SeloCartao Management Component', () => {
        let comp: SeloCartaoComponent;
        let fixture: ComponentFixture<SeloCartaoComponent>;
        let service: SeloCartaoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfielTestModule],
                declarations: [SeloCartaoComponent],
                providers: []
            })
                .overrideTemplate(SeloCartaoComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SeloCartaoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SeloCartaoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SeloCartao(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.seloCartaos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
