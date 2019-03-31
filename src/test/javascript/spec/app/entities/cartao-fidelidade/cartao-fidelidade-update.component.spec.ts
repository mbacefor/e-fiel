/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { EfielTestModule } from '../../../test.module';
import { CartaoFidelidadeUpdateComponent } from 'app/entities/cartao-fidelidade/cartao-fidelidade-update.component';
import { CartaoFidelidadeService } from 'app/entities/cartao-fidelidade/cartao-fidelidade.service';
import { CartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';

describe('Component Tests', () => {
    describe('CartaoFidelidade Management Update Component', () => {
        let comp: CartaoFidelidadeUpdateComponent;
        let fixture: ComponentFixture<CartaoFidelidadeUpdateComponent>;
        let service: CartaoFidelidadeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfielTestModule],
                declarations: [CartaoFidelidadeUpdateComponent]
            })
                .overrideTemplate(CartaoFidelidadeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CartaoFidelidadeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CartaoFidelidadeService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new CartaoFidelidade(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.cartaoFidelidade = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new CartaoFidelidade();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.cartaoFidelidade = entity;
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
