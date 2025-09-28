import { Component } from '@angular/core';
import * as XLSX from 'xlsx';
import { Connection } from '../models/Connection.model';
import { ApiConnectorService } from '../services/api-connector.service';

@Component({
  selector: 'app-excel-reader-component',
  standalone: true,
  imports: [],
  templateUrl: './excel-reader-component.html',
  styleUrl: './excel-reader-component.css',
})
export class ExcelReaderComponent {
  connections: Connection[] = [];
  constructor(private apiConnectorService: ApiConnectorService) {}

  onFileChange(event: any): void {
    const target: DataTransfer = <DataTransfer>event.target;

    if (!target.files || target.files.length !== 1) {
      console.error('Please select a single file');
      return;
    }

    const reader: FileReader = new FileReader();

    reader.onload = (e: any) => {
      const bstr: string = e.target.result;
      const workbook: XLSX.WorkBook = XLSX.read(bstr, { type: 'binary' });

      const sheetName: string = workbook.SheetNames[0];
      const sheet: XLSX.WorkSheet = workbook.Sheets[sheetName];

      const rows: any[] = XLSX.utils.sheet_to_json(sheet);

      this.connections = rows.map((row) => ({
        connectionId: row['Route ID'] ?? '',
        departureCity: row['Departure City'] ?? '',
        arrivalCity: row['Arrival City'] ?? '',
        departureTime: row['Departure Time'] ?? '',
        arrivalTime: row['Arrival Time'] ?? '',
        trainType: row['Train Type'] ?? '',
        daysOfOperation: row['Days of Operation'] ?? '',
        firstClassRate: Number(row['First Class ticket rate (in euro)']) || 0,
        secondClassRate: Number(row['Second Class ticket rate (in euro)']) || 0,
      }));

      console.log('Parsed Connections:', this.connections);
    };

    reader.readAsBinaryString(target.files[0]);
  }

  sendNewListOfConnection() {
    this.apiConnectorService.saveConnectionListFromExcel(this.connections).subscribe({
      next: (response) => {
        console.log('Success:', response);
      },
      error: (error) => {
        console.error('Error:', error);
      },
    });
  }
}
