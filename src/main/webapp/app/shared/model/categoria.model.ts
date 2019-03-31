import { IEmpresa } from 'app/shared/model/empresa.model';

export interface ICategoria {
    id?: number;
    descricao?: string;
    ativo?: boolean;
    empresas?: IEmpresa[];
}

export class Categoria implements ICategoria {
    constructor(public id?: number, public descricao?: string, public ativo?: boolean, public empresas?: IEmpresa[]) {
        this.ativo = this.ativo || false;
    }
}
