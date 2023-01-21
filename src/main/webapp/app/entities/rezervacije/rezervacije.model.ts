import dayjs from 'dayjs/esm';

export interface IRezervacije {
  id?: number;
  brojSobe?: number;
  datumDolaska?: dayjs.Dayjs | null;
  datumOdlaska?: dayjs.Dayjs | null;
}

export class Rezervacije implements IRezervacije {
  constructor(
    public id?: number,
    public brojSobe?: number,
    public datumDolaska?: dayjs.Dayjs | null,
    public datumOdlaska?: dayjs.Dayjs | null
  ) {}
}

export function getRezervacijeIdentifier(rezervacije: IRezervacije): number | undefined {
  return rezervacije.id;
}
