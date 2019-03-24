export class Gateway {
  name: string;
  url: string;

  latency: number;
  lastUpdate: number;
  statusCode: number;

  cors: Array<string>;
}
