# HTTP Ingestion

Code corresponding to the [HTTP Ingestion Guide](https://www.swimos.org/guides/http-ingestion.html).

## Running This Demo

_All commands should be executed from the parent directory._

This demo only requires running the `swim.vehicle.Main` class.

Thus, `./gradlew http-ingestion:run` is sufficient for those who pick Option 2 in [`README.md` from the parent directory](../README.md).

For those who pick Option 3, simply execute `./gradlew http-ingestion:build`, unpackage either artifact in `http-ingestion/build/distributions`, and run the appropriate script in `bin/`; no file copying/editing is required.
