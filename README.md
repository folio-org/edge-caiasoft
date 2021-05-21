# edge-caiasoft

Copyright (C) 2021 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

## Introduction
The purpose of this edge API is to bridge the gap between CaiaSoft remote storage provider and FOLIO.

## Additional information

### API Details
API provides the following URLs for working with remote storage configurations:

| Method | URL| Description | 
|---|---|---|
| GET | /caiasoftService/ItemBarcodes/{itemBarcode}/accessioned/{remoteStorageConfigurationId} | The polling API for accessions |
| POST | /caiasoftService/RequestBarcodes/{itemBarcode}/reshelved/{remoteStorageConfigurationId} | API for returning an item |

### Required Permissions
Institutional users should be granted the following permissions in order to use this edge API:
- `remote-storage.all`

### Configuration
Please refer to the [Configuration](https://github.com/folio-org/edge-common/blob/master/README.md#configuration) section in the [edge-common](https://github.com/folio-org/edge-common/blob/master/README.md) documentation to see all available system properties and their default values.

### Issue tracker
See project [EDGCSOFT](https://issues.folio.org/browse/EDGCSOFT)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker).

### Other documentation
Other [modules](https://dev.folio.org/source-code/#server-side) are described,
with further FOLIO Developer documentation at
[dev.folio.org](https://dev.folio.org/)

