export interface Connection {
  connectionId: string;
  departureCity: string;
  arrivalCity: string;
  departureTime: string;
  arrivalTime: string;
  trainType: string;
  daysOfOperation: string;
  firstClassRate: number;
  secondClassRate: number;
  durationMinutes: number;
}