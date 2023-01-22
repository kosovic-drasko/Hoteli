import dayjs from 'dayjs/esm';

export interface ITabelaRaspolozivostiSoba {
  id?: number;
  brojSobe?: number;
  datumDolaska?: dayjs.Dayjs | null;
  datumOdlaska?: dayjs.Dayjs | null;
}

export class TabelaRaspolozivostiSoba implements ITabelaRaspolozivostiSoba {
  constructor(
    public id?: number,
    public brojSobe?: number,
    public datumDolaska?: dayjs.Dayjs | null,
    public datumOdlaska?: dayjs.Dayjs | null
  ) {}
}

export function getTabelaRaspolozivostiSobaIdentifier(tabelaRaspolozivostiSoba: ITabelaRaspolozivostiSoba): number | undefined {
  return tabelaRaspolozivostiSoba.id;
}
