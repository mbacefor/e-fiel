/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EfielTestModule } from '../../../test.module';
import { CartaoFidelidadeDeleteDialogComponent } from 'app/entities/cartao-fidelidade/cartao-fidelidade-delete-dialog.component';
import { CartaoFidelidadeService } from 'app/entities/cartao-fidelidade/cartao-fidelidade.service';

describe('Component Tests', () => {
    describe('CartaoFidelidade Management Delete Component', () => {
        let comp: CartaoFidelidadeDeleteDialogComponent;
        let fixture: ComponentFixture<CartaoFidelidadeDeleteDialogComponent>;
        let service: CartaoFidelidadeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfielTestModule],
                declarations: [CartaoFidelidadeDeleteDialogComponent]
            })
                .overrideTemplate(CartaoFidelidadeDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CartaoFidelidadeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CartaoFidelidadeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
