import dayjs from 'dayjs/esm';

export interface IRezervacije {
  id?: number;
  brojSobe?: number;
  datumDolaska?: dayjs.Dayjs;
  datumOdlaska?: dayjs.Dayjs;
}

export class Rezervacije implements IRezervacije {
  constructor(public id?: number, public brojSobe?: number, public datumDolaska?: dayjs.Dayjs, public datumOdlaska?: dayjs.Dayjs) {}
}

export function getRezervacijeIdentifier(rezervacije: IRezervacije): number | undefined {
  return rezervacije.id;
}
