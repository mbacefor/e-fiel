/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { EfielTestModule } from '../../../test.module';
import { SeloCartaoUpdateComponent } from 'app/entities/selo-cartao/selo-cartao-update.component';
import { SeloCartaoService } from 'app/entities/selo-cartao/selo-cartao.service';
import { SeloCartao } from 'app/shared/model/selo-cartao.model';

describe('Component Tests', () => {
    describe('SeloCartao Management Update Component', () => {
        let comp: SeloCartaoUpdateComponent;
        let fixture: ComponentFixture<SeloCartaoUpdateComponent>;
        let service: SeloCartaoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfielTestModule],
                declarations: [SeloCartaoUpdateComponent]
            })
                .overrideTemplate(SeloCartaoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SeloCartaoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SeloCartaoService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SeloCartao(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.seloCartao = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SeloCartao();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.seloCartao = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
