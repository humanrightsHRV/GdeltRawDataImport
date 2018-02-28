# GdeltRawDataImport
##sub-section of the dataLoaderMYSQL project

This program is used to download and extract the data for country "India" from GDELT. The downloaded data contains the following headers:
* Uniqueid
* Day
* Month
* Year
* EventCode
* AvgTone
* CountryCode
* Type
* Fullname
* Lat
* Lon
* url
* eventCategory (converted by looking at the EventCode) 

There are 8 types of unrest categories used in the project, so only the 8 are imported.
