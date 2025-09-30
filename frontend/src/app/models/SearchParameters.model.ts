export interface SearchParameters {
  connectionId: string | null;
  departureCity: string | null;
  arrivalCity: string | null;
  departureTime: string | null;
  arrivalTime: string | null;
  trainType: string | null;
  daysOfOperation: string | null;
  firstClassRate: number | null;
  secondClassRate: number | null;
  sortBy: string | null;
  sortOrder: string | null;
}