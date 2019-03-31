import { Moment } from 'moment';
import { ICartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';
import { IEmpresa } from 'app/shared/model/empresa.model';

export interface ICampanha {
    id?: number;
    nome?: string;
    logoContentType?: string;
    logo?: any;
    premio?: string;
    regras?: string;
    expira?: Moment;
    numeroSelos?: number;
    cartoes?: ICartaoFidelidade[];
    empresa?: IEmpresa;
}

export class Campanha implements ICampanha {
    constructor(
        public id?: number,
        public nome?: string,
        public logoContentType?: string,
        public logo?: any,
        public premio?: string,
        public regras?: string,
        public expira?: Moment,
        public numeroSelos?: number,
        public cartoes?: ICartaoFidelidade[],
        public empresa?: IEmpresa
    ) {}
}
