import { Connection } from "./Connection.model";
import { Traveller } from "./Traveller.model";
export interface TripDTO {
  connections: Connection[],
  travellers: Traveller[],
  tripStatus: string; // RESERVED OR COMPLETED
}