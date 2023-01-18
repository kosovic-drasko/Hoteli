export interface ISobe {
  id?: number;
  brojSobe?: number;
  cijena?: number;
}

export class Sobe implements ISobe {
  constructor(public id?: number, public brojSobe?: number, public cijena?: number) {}
}

export function getSobeIdentifier(sobe: ISobe): number | undefined {
  return sobe.id;
}
