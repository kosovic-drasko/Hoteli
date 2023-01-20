import dayjs from 'dayjs/esm';

export interface ISobeRezervacije {
  id?: number;
  brojSobe?: number | null;
  cijena?: number | null;
  datumDolaska?: dayjs.Dayjs | null;
  datumOdlaska?: dayjs.Dayjs | null;
}

export class SobeRezervacije implements ISobeRezervacije {
  constructor(
    public id?: number,
    public brojSobe?: number | null,
    public cijena?: number | null,
    public datumDolaska?: dayjs.Dayjs | null,
    public datumOdlaska?: dayjs.Dayjs | null
  ) {}
}

export function getSobeRezervacijeIdentifier(sobeRezervacije: ISobeRezervacije): number | undefined {
  return sobeRezervacije.id;
}
