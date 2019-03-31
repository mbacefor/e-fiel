/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EfielTestModule } from '../../../test.module';
import { CampanhaDeleteDialogComponent } from 'app/entities/campanha/campanha-delete-dialog.component';
import { CampanhaService } from 'app/entities/campanha/campanha.service';

describe('Component Tests', () => {
    describe('Campanha Management Delete Component', () => {
        let comp: CampanhaDeleteDialogComponent;
        let fixture: ComponentFixture<CampanhaDeleteDialogComponent>;
        let service: CampanhaService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfielTestModule],
                declarations: [CampanhaDeleteDialogComponent]
            })
                .overrideTemplate(CampanhaDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CampanhaDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CampanhaService);
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
