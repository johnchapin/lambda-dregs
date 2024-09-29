# Lambda Dregs

Simple utility classes to ease working with AWS Lambda.

## Goals

1. Lightweight - no runtime dependencies, no frameworks.
2. Opinionated - behavior that makes sense for Lambda, with almost no configuration.

## Classes

### 1. Mapped Records

Mapped records are Java Records that are populated with values from the environment (or a Map).
The values are looked up using keys derived from the record components (fields).
Null checks, default values, and other post-processing can be added using the "compact" record constructor.

#### Example Lambda using a Mapped Record

```java
// Given a record definition, MappedRecord.fromSystem instantiates a record 
// by looking up environment variables based on the record component (field)
// names.
record Config(String baseUrl, int timeout) {}

// System.getEnv() = {
//   "BASE_URL": "https://www.test.com/",
//   "TIMEOUT":  "60"
// }

var config = MappedRecord.fromSystem(Config.class);

// Record field are typed
config.baseUrl == "https://www.test.com/";
config.timeout == 60;
```