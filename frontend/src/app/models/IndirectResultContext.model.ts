import { Connection } from "./Connection.model";
export interface IndirectResultContext {
    connections: Connection[];
    totalDuration: number;
    timeBetweenConnections: number;
    totalFirstClassRate: number;
    totalSecondClassRate: number;
}