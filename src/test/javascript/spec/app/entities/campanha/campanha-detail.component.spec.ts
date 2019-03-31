/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EfielTestModule } from '../../../test.module';
import { CampanhaDetailComponent } from 'app/entities/campanha/campanha-detail.component';
import { Campanha } from 'app/shared/model/campanha.model';

describe('Component Tests', () => {
    describe('Campanha Management Detail Component', () => {
        let comp: CampanhaDetailComponent;
        let fixture: ComponentFixture<CampanhaDetailComponent>;
        const route = ({ data: of({ campanha: new Campanha(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfielTestModule],
                declarations: [CampanhaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CampanhaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CampanhaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.campanha).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
