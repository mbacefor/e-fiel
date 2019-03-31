import { ICartaoFidelidade } from 'app/shared/model/cartao-fidelidade.model';

export const enum TipoSelo {
    COMPRA = 'COMPRA',
    PROMOCAO = 'PROMOCAO',
    OUTRO = 'OUTRO'
}

export interface ISeloCartao {
    id?: number;
    descricao?: string;
    valor?: number;
    tipo?: TipoSelo;
    cartaoFidelidade?: ICartaoFidelidade;
}

export class SeloCartao implements ISeloCartao {
    constructor(
        public id?: number,
        public descricao?: string,
        public valor?: number,
        public tipo?: TipoSelo,
        public cartaoFidelidade?: ICartaoFidelidade
    ) {}
}
