export class Gateway {
  name: string;
  url: string;

  latency: number;
  lastUpdate: number;
  statusCode: number;
  writable: number;

  cors: Array<string>;

  shortName(): string {
    if (this.url.length > 80) return this.url.substring(0, 80) + "..."
    return this.url
  }
}
