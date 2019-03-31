import { Moment } from 'moment';
import { ICampanha } from 'app/shared/model/campanha.model';
import { ISeloCartao } from 'app/shared/model/selo-cartao.model';
import { IUser } from 'app/core/user/user.model';

export interface ICartaoFidelidade {
    id?: number;
    dataCriacao?: Moment;
    dataPremio?: Moment;
    campanha?: ICampanha;
    selos?: ISeloCartao[];
    dono?: IUser;
}

export class CartaoFidelidade implements ICartaoFidelidade {
    constructor(
        public id?: number,
        public dataCriacao?: Moment,
        public dataPremio?: Moment,
        public campanha?: ICampanha,
        public selos?: ISeloCartao[],
        public dono?: IUser
    ) {}
}
