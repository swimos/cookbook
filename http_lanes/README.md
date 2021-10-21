# Http Lanes

Code corresponding to the [Http Lanes cookbook](https://swimos.org/tutorials/http-lanes/).

## Running This Demo

This demo requires running the `swim.basic.BasicPlane` class. Then, the below curl commands can be used or run the `swim.basic.CustomClient` class. Review [`README.md` from the parent directory](../README.md) for instructions on how to do this. For those who pick Option 2, the `gradle` task corresponding to `swim.basic.CustomClient` is called `runClient`.

### Curl Commands

Get state from the server:
`curl --location --request GET 'localhost:9001/unit?lane=http'`

Get state from the server, in Json:
`curl --location --request GET 'localhost:9001/unit?lane=httpJson'`

Update the state of the server:
`curl --location --request POST 'localhost:9001/unit?lane=http' \
--header 'Content-Type: application/json' \
--data-raw '{"foo": 5}'`