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

### Rancher deployment

1. create the proper user needed for this module ( i dnt know the exact username )
2. add this user in ephemeral properties file
3. find the ephermeral properties file location inside the caiasoft container and modify the path of this file in the javaoption

### Deployment information
1. CaiaSoft connection should be established from the CaiaSoft edge Folio module. Therefore CaiaSoft edge module
   needs to know the name of all the tenants, which has CaiaSoft connection. For the ephemeral configuration these names locate in the
   `ephemeral.properties` (key `tenants`). In order to provide it before the deployment the list of tenant names (e.g. ids) should be put to AWS parameters store (as String). The tenant names list separated by
   coma (e.g. diku, someothertenantname) should be stored in AWS param store in the variable with
   key: `caiaSoftClient_tenants` by default or could be provided its own key through `caia_soft_tenants` parameter of starting module.
2. For each tenant using CaiaSoft the corresponding user should be added
   to the AWS parameter store with key in the following format `{{username}}_{{tenant}}_{{username}}` (where salt and username are the same - `{{username}}`) with value of corresponding `{{password}}` (as Secured String).
   This user should work as ordinary edge institutional user with the only one difference - his username and salt name are same. 
   By default the value of `{{username}}` is `caiaSoftClient`. It could be changed through `caia_soft_client` parameter of starting module.
3. User with name `{{username}}`, password `{{password}}`, remote-storage.all permissions should be created on FOLIO. After that apikey can
   be generated for making calls to Edge CaiaSoft API.

### Create CaiaSoft configuration
1. Log in to Folio, go to "Settings" -> "Remote storage" -> "Configurations", click "New" button.
2. Enter General information settings:
* Select "CaiaSoft" in Provider name box
* Enter Remote storage name
* Enter remote storage endpoint in URL and key in Credential properties field.
3. Set Data synchronization schedule. This setting defines timeframe to scan retrieval queues.
4. Set up Accession holding workflow preference and Returning workflow preference
5. Click "Save & close" button

*Note: Folio updates CaiaSoft remote storage configuration settings each 60 minutes, so it can take up to one hour before new or edited Remote storage configuration settings will be applied.*

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

