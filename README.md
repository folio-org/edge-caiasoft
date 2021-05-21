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
| POST | /caiasoftService/Requests/{requestId}/route/{remoteStorageConfigurationId} | Сheck-in by requestId and remoteStorageConfigurationId |

### Deployment information
1. CaiaSoft connection should be established from the CaiaSoft edge Folio module. Therefore CaiaSoft edge module
   needs to know the name of all the tenants, which has CaiaSoft connection. For the ephemeral configuration these names locate in the
   `ephemeral.properties` (key `tenants`). In order to provide it before the deployment the list of tenant names (e.g. ids) should be put to AWS parameters store (as String). The tenant names list separated by
   coma (e.g. diku, someothertenantname) should be stored in AWS param store in the variable with
   key: `caiaSoftClient_tenants` by default or could be provided its own key through `caia_soft_tenants` parameter of starting module.
2. For each tenant using CaiaSoft the corresponding user should be added
   to the AWS parameter store with key in the following format `caiaSoftClient_{{tenant}}_caiaSoftClient` (where salt and username are the same - `caiaSoftClient`) with value of corresponding `{{password}}` (as Secured String).
   This user should work as ordinary edge institutional user with the only one difference
- his username and salt name are - caiaSoftClient.
3. User `caiaSoftClient` with password `{{password}}` and remote-storage.all permissions should be created on FOLIO. After that apikey can
   be generated for making calls to Edge CaiaSoft API.

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

