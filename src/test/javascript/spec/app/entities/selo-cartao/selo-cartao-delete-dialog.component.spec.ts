/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EfielTestModule } from '../../../test.module';
import { SeloCartaoDeleteDialogComponent } from 'app/entities/selo-cartao/selo-cartao-delete-dialog.component';
import { SeloCartaoService } from 'app/entities/selo-cartao/selo-cartao.service';

describe('Component Tests', () => {
    describe('SeloCartao Management Delete Component', () => {
        let comp: SeloCartaoDeleteDialogComponent;
        let fixture: ComponentFixture<SeloCartaoDeleteDialogComponent>;
        let service: SeloCartaoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfielTestModule],
                declarations: [SeloCartaoDeleteDialogComponent]
            })
                .overrideTemplate(SeloCartaoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SeloCartaoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SeloCartaoService);
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
