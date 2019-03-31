import { ICampanha } from 'app/shared/model/campanha.model';
import { IUser } from 'app/core/user/user.model';
import { ICategoria } from 'app/shared/model/categoria.model';

export interface IEmpresa {
    id?: number;
    nome?: string;
    cnpj?: string;
    endereco?: string;
    fone?: string;
    campanhas?: ICampanha[];
    criador?: IUser;
    cartegorias?: ICategoria[];
}

export class Empresa implements IEmpresa {
    constructor(
        public id?: number,
        public nome?: string,
        public cnpj?: string,
        public endereco?: string,
        public fone?: string,
        public campanhas?: ICampanha[],
        public criador?: IUser,
        public cartegorias?: ICategoria[]
    ) {}
}
