/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { CampanhaService } from 'app/entities/campanha/campanha.service';
import { ICampanha, Campanha } from 'app/shared/model/campanha.model';

describe('Service Tests', () => {
    describe('Campanha Service', () => {
        let injector: TestBed;
        let service: CampanhaService;
        let httpMock: HttpTestingController;
        let elemDefault: ICampanha;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(CampanhaService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Campanha(0, 'AAAAAAA', 'image/png', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, 0);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        expira: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Campanha', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        expira: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        expira: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Campanha(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Campanha', async () => {
                const returnedFromService = Object.assign(
                    {
                        nome: 'BBBBBB',
                        logo: 'BBBBBB',
                        premio: 'BBBBBB',
                        regras: 'BBBBBB',
                        expira: currentDate.format(DATE_FORMAT),
                        numeroSelos: 1
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        expira: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Campanha', async () => {
                const returnedFromService = Object.assign(
                    {
                        nome: 'BBBBBB',
                        logo: 'BBBBBB',
                        premio: 'BBBBBB',
                        regras: 'BBBBBB',
                        expira: currentDate.format(DATE_FORMAT),
                        numeroSelos: 1
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        expira: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Campanha', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
