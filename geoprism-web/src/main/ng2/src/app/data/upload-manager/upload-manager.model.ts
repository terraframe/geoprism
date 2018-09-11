
export class ExcelImportHistory {
  id: string;
  name: string;
  importCount: number;
  totalRecords: number;
  status: string;  
  startTime: string;
  hasErrorSpreadsheet: boolean;
  errorMsg: string;
  geoSyns: number;
  termSyns: number;
}
