export interface SearchParameters {
  connectionId: string | null;
  departureCity: string | null;
  arrivalCity: string | null;
  departureTime: string | null;
  arrivalTime: string | null;
  trainType: string | null;
  firstClassRate: number | null;
  secondClassRate: number | null;
  sortBy: string | null;
  sortOrder: string | null;
  bitmapDays: number;
  duration: number | null;
}