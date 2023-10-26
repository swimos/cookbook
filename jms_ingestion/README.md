# JMS Ingestion

Code corresponding to the [JMS Ingestion Guide](https://www.swimos.org/guides/jms-ingestion.html).

## Running This Demo

_All commands should be executed from the parent directory._

This demo only requires running the `swim.vehicle.Main` class.

Thus, `./gradlew jms-ingestion:run` is sufficient for those who pick Option 2 in [`README.md` from the parent directory](../README.md).

For those who pick Option 3, simply execute `./gradlew jms-ingestion:build`, un-package either artifact in `jms-ingestion/build/distributions`, and run the appropriate script in `bin/`; no file copying/editing is required.
