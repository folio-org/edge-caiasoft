# edge-caiasoft

Copyright (C) 2021-2023 The Open Library Foundation

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

1. CaiaSoft connection should be established from the CaiaSoft edge Folio module. Therefore CaiaSoft edge module needs to know the name of all the tenants, which has CaiaSoft connection. For the ephemeral configuration these names locate in the `ephemeral.properties` (key `tenants`). In order to provide it before the deployment the list of tenant names (e.g. ids) should be put to AWS parameters store (as String). The tenant names list separated by coma (e.g. diku, someothertenantname) should be stored in AWS param store in the variable with key: `caiaSoftClient_tenants` by default or could be provided its own key through `caia_soft_tenants` parameter of starting module.
2. For each tenant using CaiaSoft the corresponding user should be added to the AWS parameter store with key in the following format `{{username}}_{{tenant}}_{{username}}` (where salt and username are the same - `{{username}}`) with value of corresponding `{{password}}` (as Secured String). This user should work as ordinary edge institutional user with the only one difference - his username and salt name are same. By default the value of `{{username}}` is `caiaSoftClient`. It could be changed through `caia_soft_client` parameter of starting module.
3. User with name `{{username}}`, password `{{password}}`, remote-storage.all permissions should be created on FOLIO. After that apikey can be generated for making calls to Edge CaiaSoft API.

#### Rancher and kubernetes deployment
1. Check that mod-remote-storage has been installed and has been registered to okapi.
2. Create a new user named `caiaSoftClient` in FOLIO. You may also use `diku_admin` for testing and avoid this step.
3. Create a secret in the rancher cluster. Make the key of this secret `ephemeral.properties` and the value similar to `secureStore.type=Ephemeral tenants=diku diku=diku_admin,admin`.
4. Add this secret as a volume mount to the workload for the edge module container. Set the mount point of this volume to `\etc\edge`.
5. Set the `JAVA_OPTIONS` environment variable for the workload to something similar to `-Dsecure_store_props=/etc/edge/ephemeral.properties -Dokapi_url=http://okapi:9130 -Dlog_level=DEBUG -Dcaia_soft_client=diku_admin` . 
6. Redeploy the container. This will make the container aware of the new secret and volume mount.

##### Other rancher considerations
If you are deploying using a FOLIO helm chart, you may want to take adavantage of overriding the chart's yml with answer keys and values to enable the ingress. Here is an example:

| Key | Value |
|---|---|
|ingress.annotations.external-dns\.alpha\.kubernetes\.io/target|f2b6996c-kubesystem-albing-accc-1096161577.us-west-2.elb.amazonaws.com|
|ingress.enabled|true|
|ingress.hosts[0].host|core-platform-edge-orders.ci.folio.org|
|ingress.hosts[0].paths[0]|/|

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

### TLS Configuration for HTTP Endpoints

To configure Transport Layer Security (TLS) for HTTP endpoints in edge module, the following configuration parameters can be used. These parameters allow you to specify key and keystore details necessary for setting up TLS.

#### Configuration Parameters

1. **`spring.ssl.bundle.jks.web-server.key.password`**
- **Description**: Specifies the password for the private key in the keystore.
- **Example**: `spring.ssl.bundle.jks.web-server.key.password=SecretPassword`

2. **`spring.ssl.bundle.jks.web-server.key.alias`**
- **Description**: Specifies the alias of the key within the keystore.
- **Example**: `spring.ssl.bundle.jks.web-server.key.alias=localhost`

3. **`spring.ssl.bundle.jks.web-server.keystore.location`**
- **Description**: Specifies the location of the keystore file in the local file system.
- **Example**: `spring.ssl.bundle.jks.web-server.keystore.location=/some/secure/path/test.keystore.bcfks`

4. **`spring.ssl.bundle.jks.web-server.keystore.password`**
- **Description**: Specifies the password for the keystore.
- **Example**: `spring.ssl.bundle.jks.web-server.keystore.password=SecretPassword`

5. **`spring.ssl.bundle.jks.web-server.keystore.type`**
- **Description**: Specifies the type of the keystore. Common types include `JKS`, `PKCS12`, and `BCFKS`.
- **Example**: `spring.ssl.bundle.jks.web-server.keystore.type=BCFKS`

6. **`server.ssl.bundle`**
- **Description**: Specifies which SSL bundle to use for configuring the server. This parameter links to the defined SSL bundle, for example, `web-server`.
- **Example**: `server.ssl.bundle=web-server`

7. **`server.port`**
- **Description**: Specifies the port on which the server will listen for HTTPS requests.
- **Example**: `server.port=8443`

#### Example Configuration

To enable TLS for the edge module using the above parameters, you need to provide them as the environment variables. Below is an example configuration:

```properties
spring.ssl.bundle.jks.web-server.key.password=SecretPassword
spring.ssl.bundle.jks.web-server.key.alias=localhost
spring.ssl.bundle.jks.web-server.keystore.location=classpath:test/test.keystore.bcfks
spring.ssl.bundle.jks.web-server.keystore.password=SecretPassword
spring.ssl.bundle.jks.web-server.keystore.type=BCFKS

server.ssl.bundle=web-server
server.port=8443
```
Also, you can use the relaxed binding with the upper case format, which is recommended when using system environment variables.
```properties
SPRING_SSL_BUNDLE_JKS_WEBSERVER_KEY_PASSWORD=SecretPassword
SPRING_SSL_BUNDLE_JKS_WEBSERVER_KEY_ALIAS=localhost
SPRING_SSL_BUNDLE_JKS_WEBSERVER_KEYSTORE_LOCATION=classpath:test/test.keystore.bcfks
SPRING_SSL_BUNDLE_JKS_WEBSERVER_KEYSTORE_PASSWORD=SecretPassword
SPRING_SSL_BUNDLE_JKS_WEBSERVER_KEYSTORE_TYPE=BCFKS

SERVER_SSL_BUNDLE=web-server
SERVER_PORT=8443
```

### TLS Configuration for Feign HTTP Clients

To configure Transport Layer Security (TLS) for HTTP clients created using Feign annotations in the edge module, you can use the following configuration parameters. These parameters allow you to specify trust store details necessary for setting up TLS for Feign clients.

#### Configuration Parameters

1. **`folio.client.okapiUrl`**
- **Description**: Specifies the base URL for the Okapi service.
- **Example**: `folio.client.okapiUrl=https://okapi:443`

2. **`folio.client.tls.enabled`**
- **Description**: Enables or disables TLS for the Feign clients.
- **Example**: `folio.client.tls.enabled=true`

3. **`folio.client.tls.trustStorePath`**
- **Description**: Specifies the location of the trust store file.
- **Example**: `folio.client.tls.trustStorePath=classpath:/some/secure/path/test.truststore.bcfks`

4. **`folio.client.tls.trustStorePassword`**
- **Description**: Specifies the password for the trust store.
- **Example**: `folio.client.tls.trustStorePassword="SecretPassword"`

5. **`folio.client.tls.trustStoreType`**
- **Description**: Specifies the type of the trust store. Common types include `JKS`, `PKCS12`, and `BCFKS`.
- **Example**: `folio.client.tls.trustStoreType=bcfks`

#### Note
The `trustStorePath`, `trustStorePassword`, and `trustStoreType` parameters can be omitted if the server provides a public certificate.

#### Example Configuration

To enable TLS for Feign HTTP clients using the above parameters, you need to provide them as the environment variables. Below is an example configuration:

```properties
folio.client.okapiUrl=https://okapi:443
folio.client.tls.enabled=true
folio.client.tls.trustStorePath=classpath:test/test.truststore.bcfks
folio.client.tls.trustStorePassword=SecretPassword
folio.client.tls.trustStoreType=bcfks
```
Also, you can use the relaxed binding with the upper case format, which is recommended when using system environment variables.
```properties
FOLIO_CLIENT_OKAPIURL=https://okapi:443
FOLIO_CLIENT_TLS_ENABLED=true
FOLIO_CLIENT_TLS_TRUSTSTOREPATH=classpath:test/test.truststore.bcfks
FOLIO_CLIENT_TLS_TRUSTSTOREPASSWORD=SecretPassword
FOLIO_CLIENT_TLS_TRUSTSTORETYPE=bcfks
```

### Issue tracker
See project [EDGCSOFT](https://issues.folio.org/browse/EDGCSOFT)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker).

### Other documentation
Other [modules](https://dev.folio.org/source-code/#server-side) are described,
with further FOLIO Developer documentation at
[dev.folio.org](https://dev.folio.org/)

