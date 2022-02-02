# Call Log App
## This app records all your calls and servers them as an API

## Features

- Shows current call info on the device
- Provides history of calls since server started

## How to use?

Just connect the device to WiFi and hit start server. The address displayed in phone is accessible on local wifi network.
you can use your phone normally while server runs in background

## Installation

Import in Android studio and run the app.

## APIs

The server root URL is shown on the device after running the app.

#### Root:
Endpoint: `/`
Response:
```json
{
  "services": [
    {
      "name": "status",
      "uri": "http://192.168.2.108:8000/status"
    },
    {
      "name": "log",
      "uri": "http://192.168.2.108:8000/log"
    }
  ],
  "start": "2022-02-02T18:17:51+01:00"
}
```
`start`: when server was started
#### Status:
Shows status of currently ongoing call
Endpoint: `/status`
Response:
```json
{
  "name": "DHL Paketshop", 
  "number": "022890243511",
  "ongoing": true
}
```
`ongoing`: mandatory, whether a call is running or not
`number`: number with which call is ongoing. **optional**, missing if ongoing is false
`name`: name of the caller as saved on device. **optional**, missing if ongoing is false or number is not saved on device

#### Log:
Shows history of calls since server was started
```json
[
  {
    "beginning": "2022-02-02T18:27:26+01:00",
    "duration": "8",
    "name": "DHL Paketshop",
    "number": "022890243511",
    "timesQueried": 1
  },
  {
    "beginning": "2022-02-02T18:26:54+01:00",
    "duration": "12",
    "number": "022890243511",
    "timesQueried": 1
  }
]
```
`beginning`: start time of the call (in device local time)
`duration`: duration of the call
`name`: name of the caller as saved on device. **optional**, missing if number is not saved on device at the `beginning` of call
`number`: number with which call is ongoing.
`timesQueried`: how many times data on this call was requested while ongoing using `/status`
## TODO:
* Check whether device is connected to Wifi before starting server
* Update name in old records if number was saved later/updated on device.