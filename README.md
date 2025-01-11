# CardClarity
CardClarity is a credit card management application designed to help users maintain their 
credit cards and maximize their benefits. The app has features such as geolocation-based 
purchase inference, receipt parsing, and credit card due date notifications. The primary 
objectives are to simplify credit card management and offer personalized recommendations 
based on user spending patterns.

[Video Demo](https://youtu.be/RDbTogTmcc0)

## Build from Source
For our application to function, both a Veryfi and a Google Places API key are required.
These keys are not committed to GitHub due to security reasons.

To build this Android application from source, these keys must be saved in a `secrects.properties`
file under the project root in the following format
```
PLACES_API_KEY=<Your Google Places API Key>
VERYFI_CLIENT_ID=<Your Veryfi API Client ID>
VERYFI_API_KEY=<Your Veryfi API Key>
```

A prebuilt APK file can be downloaded in the Releases section of this GitHub repository which
has these keys preinstalled.
