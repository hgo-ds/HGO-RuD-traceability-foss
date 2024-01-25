# Architecture documentation (arc42)

### Notice

This work is licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0).

* SPDX-License-Identifier: Apache-2.0
* Licence Path: <https://creativecommons.org/licenses/by/4.0/legalcode>
* Copyright (c) 2021,2022,2023 Contributors to the Eclipse Foundation
* Copyright (c) 2022, 2023 ZF Friedrichshafen AG
* Copyright (c) 2022 ISTOS GmbH
* Copyright (c) 2022, 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
* Copyright (c) 2022,2023 BOSCH AG
* Source URL: <https://github.com/eclipse-tractusx/traceability-foss>

## Introduction and goals

This chapter gives you an overview about the goals of the service,
in which context the service runs and which stakeholders are involved.

## Requirements overview

### What is the purpose of the Trace-X application

* Empower all companies from SMEs to large OEMs to participate in parts traceability with an Open-Source solution to easily take part in Catena-X Traceability Use Case.
* It is a standalone application which can be self-hosted.
* Display the relations of the automotive value chain based on a standardized IT model.
* Overview and transparency across the supplier network enable faster intervention based on recorded events in the supply chain.
* Notifications/Messages regarding quality-related incidents and a tool for inspecting the supply chain.

### Essential features

* List and view manufactured parts based on BoM AsBuild
* List and view planned parts based on BoM AsPlanned
* Filter and search functionality on part views
* Show detailed information on manufactured parts from AAS description assets and Aspects
* Uses Submodels SerialPart, AssemblyPartRelationship and Batch
* List and view Supplier parts (AssemblyPartRelationship) based on BoM AsBuild lifecycle
* View parts and parts relations in a visualized parts tree
* Send and receive top-down notifications (quality investigations) along the supply chain
* Compliance with Catena-X Guidelines

## Quality goals

The following table entries define overall Trace-X quality goals. The order of topics does not imply a priority of the quality goals.

| Quality goal | Motivation and description |
| --- | --- |
| Running reference application for Catena-X Traceability | Consume traceability data, visualize it in a state-of-the-art frontend to the user and enable the exchange of notifications. |
| Accessible and easy to use | Enable SMEs to large enterprises. |
| Cloud agnostic solution | Trace-X is built as reference architecture and able to be run on different cloud solutions. It uses Helm, i.e. Helm charts, so that it can easily be deployed on different systems. |
| Trustworthy application | Use the Catena-X standards as a basis to fulfill the interoperability (with commercial as well as other solutions) and data sovereignty requirements. |
| Application reliability | The Trace-X architecture is set up so that provided part tree structures are consumed, aggregated and utilized to enable quality related actions such as notifications along the supply chain on which the costumers can rely. |
| Usability and user experience | Trace-X is aligned with the overarching UUX guidelines. This ensures ease of use for the user as well as a good user experience. |
| Security | Static Application Security Testing (SAST) and Dynamic Application Security Testing (DAST) are executed automatically and regularly. Findings are recorded and mitigated. |

## Stakeholder

The following table presents the stakeholders of Trace-X and their respective intentions.

| Who | Matters and concern |
| --- | --- |
| Software Developer | * Make an impact on Catena-X by participating in open-source software. * Example implementation for own use cases and applications. |
| Operating Environments | * Use Trace-X as a basis for industrialization. * Example implementation for own business applications, further features and fast ramp up. |
| Catena-X Partners | * Take part in Traceability Use Case. * See relationships in the value chain. * React on quality issues with standardized actions and notifications. |
| Larger OEMs / Tiers | * Want to use this implementation for further development and integration into their system landscape. |
| SMEs | * Need a solution to view data provided into the Catena-X network. * Act in a standardized way with partners within CX. |

## Architecture Constraints

### Technical Constraints

| Name | Description |
| --- | --- |
| Interoperability | Trace-X must use EDC together with Catena-X approved data models to guarantee interoperability between participants. |
| Data Sovereignty | Data owners and data consumers have to use usage policies for offering, consuming and therefore transferring data. |
| Kubernetes for Container Orchestration | Catena-X requires the application to run in a Kubernetes environment, deployed via Helm Charts. |
| Catena-X UUX Guidance (CX Style Guide) | Frontend follows Catena-X wide UUX consistency according to CX Style Guide |

### Organizational Constraints

| Name | Description |
| --- | --- |
| Schedule | Start of development in July 2022. Further development in alignment with the Foundation of the Catena-X Automotive Network e.V. requirements and timeline. |
| Process model | Iterative and incremental. SAFe Framework is used to align with Catena-X services, prerequisites, components and requirements to be Catena-X compatible. |
| Catena-X services / requirements | Trace-X needs to be Catena-X compliant and the application has to follow the CX standards as well as interact with the core services and components. |
| Release as Open Source | The source code, at least parts of it, are made available as open source and can be found in Github Catena-X ng as well as in Eclipse Tractus-X. |
| Technology Readiness Level (TRL) for Products developed within the CX Consortia | As Trace-X is a reference implementation, the Technology Readiness Level (TRL) must not be above TRL 8. |

### Political Constraints

| Name | Description |
| --- | --- |
| Open Source | FOSS licenses approved be the Eclipse foundation have to be used. |

### Development Conventions

| Name | Description |
| --- | --- |
| Architecture documentation | Architectural documentation of Trace-X reference application in arc42-Template terminology and structure. |
| Language | The project language is English to ensure the best possible accessibility for all participants. Therefore, classes, methods etc. are named in English and the documentation is written in English. |
| Code Coverage | More than 80% test coverage of the complete source code |

## System scope and context

Trace-X is an end user application to visualize and utilize data provided to the Catena-X network. This includes the traceability of manufactured parts and batches as well as the shipped and supplied components. To utilize the CX open ecosystem it is necessary to exchange information on serialized parts and batches with supply chain partners in a standardized, data sovereign and interoperable way. This section describes the environment of Trace-X, its intended users and which  systems and components it interacts with.

## Business context

Trace-X exchanges data with any other Traceability applications within the CX ecosystem. This is implemented by integrating the service into the CX network and the usage of required central components and services provided by CX.

![arc42_000](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_000.png)

### User

Trace-X can be deployed, hosted and run by every party that is part of the Catena-X network. They need to be registered, have a BPN, technical user as well as provide valid credentials. An end-user in Trace-X can have roles provided by the CX Portal (User, Supervisor, Admin). As a prerequisite for Trace-X, valid data must already be provided to CX network since Trace-X only consumes data but has no import interface or data provisioning functionality.

### IRS

Item Relationship Service is the component that is used by Trace-X to fetch the data chains from Catena-X network. Data that is provided by IRS gets extracted, aggregated, transformed and is used for further actions in Trace-X. Further information can be found in the [IRS architecture documentation (arc42)](https://eclipse-tractusx.github.io/item-relationship-service/docs/arc42/).

### Catena-X network

Trace-X and IRS are retrieving necessary information and data from the Catena-X network (Users, digital twins, aspects, submodels, Business Partner information). If CX services are unavailable, Trace-X will not be able to perform most of its work.

### Any other Traceability App

Trace-X interacts with any other Traceability app using the CX standards. This enables sovereign data exchange as well as receiving and sending notifications to interact between different parties.

## Technical context

### Component overview

#### Trace-X API

We provide a REST API that is consumed by Trace-X frontend in order to deliver Trace-X related features such as quality-investigations or assets chain visibility.
Since Trace-X component is a very last component in the Catena-X ecosystem we are mostly depend on the other services and theirs APIs in other to deliver desired functionalities. The development of the services is not a part of the Trace-X application and each of the system that we utilize exposes REST API that we consume and interact directly.

Trace-X is a Spring Boot based application and is secured with the OpenID connector provider Keycloak and the OAuth2. This means for the company that utilize Trace-X component
it is required to obtain technical user in order to be authorized to get access to the external components within Catena-X ecosystem.

In order to use Trace-X frontend with Trace-X backend, users need to authenticate themselves in order to be authorized to get access to the Trace-X.
By the frontend UI users provide valid credentials and the system generates a bearer token that it gets from Keycloak and attaches it to the HTTP header parameter Authorization.
Then once a user is authorized and has proper role within Trace-X backend, the backend delegates HTTP calls to specific service in their behalf as technical user in order to fulfill specific functionality.

#### Registry API

![arc42_001](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_001.png)

The Trace-X acts as a consumer of the Asset Administration Shell Registry component. The Trace-X contains a Restful client (REST template) that build a REST call to the mentioned Digital Twin Registry API based on its known URL (the AAS registry URL is configurable in the Trace-X).
Requests contain "assetIds" provided by the component during assets synchronization. Like described in the above section, the security aspect is required in order to achieve a REST call against the AAS Registry. As a response, the Trace-X gets the corresponding shells and shell descriptors utilized then later for assets synchronization. The HTTP(s) transport protocol is used for the REST call communication.

#### IRS API

![arc42_002](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_002.png)

The Trace-X acts as a consumer of the IRS component. The Trace-X contains a Restful client (REST template) that build a REST call to the mentioned IRS API based on its known URL (the IRS URL is configurable in the Trace-X).
Request contains details required to start IRS fetch job provided by the component during assets synchronization. Like described in the above section, the security aspect is required in order to achieve a REST call against the IRS. As a response, the Trace-X gets the created job id and periodically pulls for the job details that contains assets that will be uploaded to the system. And as mentioned above, the transport protocol HTTP(S) is used for the REST call communication.

#### Portal API

![arc42_003](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_003.png)

The Trace-X acts as a consumer of the Portal component. The Trace-X contains a Restful client (REST template) that build a REST call to the mentioned Portal API based on its known URL (the Portal URL is configurable in the Trace-X).
Request contains "bpns" provided by the component during sending notifications. Like described in the above section, the security aspect is required in order to achieve a REST call against the Portal. As a response, the Trace-X gets the corresponding BPN mappings to EDC urls where a notification should be send over. And as mentioned above, the transport protocol HTTP(S) is used for the REST call communication.

#### EDC API

![arc42_004](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_004.png)

The Trace-X acts as a consumer of the EDC component. In Trace-X we communicate with EDC directly only for the sake of fulfilling quality-investigation functionality. Specific use cases can be viewed in [Runtime view](../runtime-view/index.adoc) section. For this purposes the integrated EDC clients in the Trace-X are responsible for creating restful requests to the EDC component. And as mentioned above, the transport protocol HTTP(S) is used for the REST call communication.

## Solution strategy

This section contains summarized architectural overview. A comparison of the most important goals and the corresponding solution approaches.

## Introduction

Following table describes the quality goals of Trace-X (see chapter quality goals) and their matching solution approaches.

| Quality goal | Matching approaches in the solution |
| --- | --- |
| Running reference application for Catena-X Traceability | * Published open source, Trace-X application can be used as a reference by anyone. |
| Accessible and easy to use | * Established programming languages are used. * Backend written in Java * Frontend written in Typescript based on the Angular framework. |
| Cloud agnostic solution | * Helm charts to support the deployment of the application in a Kubernetes environment |
| Application reliability | * Data source is the Catena-X notwork. Data is fetched with IRS directly from the data owner and the Digital Twin Registry of CX. * Trace-X can be hosted decentralized since it is an open source reference implementation. |
| Security | * Static Application Security Testing (SAST) and Dynamic Application Security Testing (DAST) are executed automatically and regularly with tools as part of the pipeline. |

## Technology

Trace-X is developed using Java and the Spring Boot framework for the Backend and Typescript based on the Angular framework for the Frontend. This decision was taken due to the support of the frameworks as well as technical knowledge of the team members.

The application can be hosted using Docker and Kubernetes. This is commonly used and widespread. With this the application has no vendor lock in regarding the hosting provider.

The communication between Frontend and Backend is done using REST APIs. This is the standard method in the Catena-X landscape and makes the application components easy to use for any third party client.

As the database to store parts information etc. PostgreSQL Database is used.

## Structure

Trace-X is divided into two components: Frontend and Backend.
It roughly can be broken down into the following parts:

* Asset controllers to get the asset information
* Dashboard controller to get dashboard related summed up information
* Registry controller to fetch assets from the Digital Twin Registry
* Notification controllers to get notification information
* Submodel controller for providing asset data functionality

The backend does a request to the Digital Twin Registry utilizing the Registry controller. Extracted data from the response is made available through the Asset controller and the Dashboard controller to the Frontend.

## Building block view

## Whitebox overall system

### Component diagram

![arc42_005](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_005.png)
```bash
The interfaces show how the components interact with each other and which interfaces the TraceX is providing.

Component Diagram

```

### Component description

|Components |Description
|IRS
|The IRS consumes relationship information across the CX-Network and builds the graph view. Within this Documentation, the focus lies on the IRS

|EDC Consumer
|The EDC Consumer Component is there to fulfill the GAIA-X and IDSA-data sovereignty principles. The EDC Consumer consists out of a control plane and a data plane.

|EDC Provider
|The EDC Provider Component connects with EDC Consumer component and  forms the end point for the actual exchange of data. It handles automatic contract negotiation and the subsequent exchange of data assets for connected applications.

|Submodel Server
|The Submodel Server offers endpoints for requesting the Submodel aspects.

|IAM/DAPS
|DAPS as central Identity Provider

## Level 1

### Component diagram

![arc42_006](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_006.png)

### Component description

| Components | Description |
| --- | --- |
| **Trace-X** | Trace-X is a system allowing the user to review the parts/assets catalogue, start a quality investigations and receive quality alerts |
| **Trace-X API** | The **Trace-X API** is the Interface over which the Data Consumer is communicating. |
| **AssetsController** | The **AssetsController** provides a REST Interface for retrieving the parts/assets information. |
| **DashboardController** | The **DashboardController** provides a REST Interface for retrieving overall statistics displayed on a dashboard screen. |
| **RegistryController** | The **RegistryController** provides a REST Interface for retrieving the data from parts registry. |
| **AssetRepository** | The **AssetRepository** is a component responsible for storing and getting assets from database. |
| **BPNRepository** | The **BPNRepository** is a component which stores BPN -> company name mappings. |
| **NotificationsRepository** | The **NotificationsRepository** is a component responsible for storing and holding status of sent/received notifications |
| **Database** | Place for storing assets, relations as well as sent/received notifications |

## Runtime view

This section describes the different functionalities of TraceX-FOSS application

## Assets

## Scenario 1: Return Assets

This section describes what happens when user lists stored assets.
In this example, the user requests as built assets.
The same can be done with as planned assets.

![arc42_007](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_007.png)

##### Overview

When a user requests stored assets, TraceX-FOSS checks if the user has an adequate role ('ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_USER').
If yes, then the endpoint returns a pageable result of assets.

The returned pageable result can be empty if no suitable asset has been found.

## Scenario 2: Return specific Assets

This section describes what happens when user searches for a specific asset.
This example shows the request of one as built asset.
The same can be done with as planned assets.

![arc42_008](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_008.png)

##### Overview

When a user requests a specific asset, TraceX-FOSS checks if the user has an adequate role ('ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_USER'). If yes, then the endpoint returns a precise Asset for the given assetId, if it is found.

If no asset has been found for the given ID, an AssetNotFoundException is thrown.

## Notifications

## Receive Quality Investigation

This sequence diagram describes the process of receiving a quality investigation from another Traceability partner.

![arc42_009](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_009.png)

### Overview

As for the sending of a quality investigation also for receiving of a notification EDC is used to push data from a sender to a receiver.
To enable receiving a notification by a partner you need to

* Create notification endpoint for qualitynotifications/receive
* Create EDC assets
* Create EDC usage policies
* Create EDC contract definitions

Trace-X implements a functionality to create the assets and their corresponding policies in the admin panel.

With the notification asset is possible to enable EDC contract negotiation and EDC data transfer based on access policies defined. Only if the sender is able to browse the asset in the catalog offer and perform a successful contract negotiation there will be the possibility to push a notification to the specified http endpoint on the receiver side.

## Send Quality Investigation

This sequence diagram describes the process of sending a quality investigation between Traceability applications.

![arc42_010](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_010.png)

### Overview

For the notification feature EDC is used to push data from a sender to a receiver.
To enable sending respective more precisely receiving a notification by a partner you need to

* Create notification endpoint for qualitynotifications/receive
* Create EDC assets
* Create EDC usage policies
* Create EDC contract definitions

Trace-X implements a functionality to create the assets and their corresponding policies in the admin panel. With the notification asset is possible to enable EDC contract negotiation and EDC data transfer process so that the quality investigation can be pushed by the sender.

In the above UML sequence diagram the sending of quality notifications from Trace-X to a receiver (any other Traceability application) is described.

## Data consumption

This sequence diagram describes the process of fetching data from a DTR and the Catena-X ecosystem.

![arc42_011](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_011.png)

### Overview

Data is fetched by a Trace-X instance using Digital Twin Registry (DTR), Item Relationship Service (IRS) and Trace-X Consumer EDC. Data has to be provided to the Catena-X network using a EDC Provider, since Trace-X is not a data provider and therefore no possibility for data provisioning with Trace-X exists.
For digital twins the Asset Administration Shell (AAS) standard is used. For fetching data with Trace-X, a Digital Twin Registry and an IRS instance are required. Data should represent parts, supplier and customer parts, parts tree / parts relations.

## Data Provisioning

This sequence diagrams describes the process of importing data from a Trace-X Dataformat

Modul 1

Data will be imported by the Trace-X Frontend into Trace-X backend and will be persisted as asset by a Trace-X instance in a transient state.
The raw data which is needed for the shared services (DTR / EDC) will be persisted as well.

![arc42_012](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_012.png)
```bash

```

Modul 2 - DRAFT

The frontend is able to select assets and publish / syncronize them with the shared services. DTR / EDC / Submodel API.

![arc42_013](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_013.png)
```bash

```

Modul 3 - DRAFT

The backend is able to persist the data in the DTR / EDC and allows to use IRS for resolving assets.

![arc42_014](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_014.png)
```bash

```

## Deployment view

## Cross-cutting concepts

## Entity Relationship Model

Please be informed that the 'as-planned' version currently lacks the database relations. However, kindly maintain the Entity-Relationship Model (ERM) in its current state.

```bash
image::./assets/arc42/arc42_015.png[]
```

## Safety and security concepts

### Authentication / Authorization

#### Trace-X API

The Trace-X is secured using OAuth2.0 / Open ID Connect.
Every request to the Trace-X API requires a valid bearer token.
JWT token should also contain two claims:

* 'bpn' which is equal to the configuration value from `API_ALLOWED_BPN` property
* 'resource_access' with the specific key for C-X environments.
The list of values will be converted to roles by Trace-X.
Currently, Trace-X API handles three roles: ***'User'*** and ***'Supervisor'*** and ***'Admin'.***

You can have a look at the rights and roles matrix in the system overview of the administration guide.

#### Trace-X as EDC client

The Trace-X accesses the Catena-X network via the EDC consumer connector.
This component requires authentication via a Verifiable Credential (VC), which is provided to the EDC via the Managed Identity Wallet.

The VC identifies and authenticates the EDC and is used to acquire access permissions for the data transferred via EDC.

### Credentials

Credentials must never be stored in Git!

## Architecture and design patterns

### Module / Package structure

* Main domain name
  * application
    * optional: subdomain name (in case of inheritance) → following same structure as main domain
    * mapper: holds the mapper of transforming domain models to response models
    * service: holds the interface for implementation in the domain package
    * rest: holds the controller for providing the api for the domain
  * domain
    * optional: subdomain name (in case of inheritance)
    * model: holds the domain model
    * service: Implementation of the interface provided in the application package
    * repository: holds the interface for accessing the infrastructure package
  * infrastructure
    * optional: subdomain name (in case of inheritance)
    * model: holds the technical entities
    * repository: holds the data access layer
      * e.g. JPARepository / Impl
  * All models (Request / Response) used in the API should be saved in the tx-model project.
  To be reusable for cucumber testing.

## "Under-the-hood" concepts

### Exception and error handling

There are two types of potential errors in TraceX:

#### Technical errors

Technical errors occur when there is a problem with the application itself, its configuration or directly connected infrastructure, e.g. the Postgres database.
Usually, the application cannot solve these problems by itself and requires some external support (manual work or automated recovery mechanisms, e.g. Kubernetes liveness probes).

These errors are printed mainly to the application log and are relevant for the healthchecks.

#### Functional errors

Functional errors occur when there is a problem with the data that is being processed or external systems are unavailable and data cannot be sent / fetched as required for the process.
While the system might not be able to provide the required function at that moment, it may work with a different dataset or as soon as the external systems recover.

#### Rules for exception handling

##### Throw or log, don't do both

When catching an exception, either log the exception and handle the problem or rethrow it, so it can be handled at a higher level of the code.
By doing both, an exception might be written to the log multiple times, which can be confusing.

##### Write own base exceptions for (internal) interfaces

By defining a common (checked) base exception for an interface, the caller is forced to handle potential errors, but can keep the logic simple.
On the other hand, you still have the possibility to derive various, meaningful exceptions for different error cases, which can then be thrown via the API.

Of course, when using only RuntimeExceptions, this is not necessary - but those can be overlooked quite easily, so be careful there.

##### Central fallback exception handler

There will always be some exception that cannot be handled inside of the code correctly - or it may just have been unforeseen.
A central fallback exception handler is required so all problems are visible in the log and the API always returns meaningful responses.
In some cases, this is as simple as a HTTP 500.

##### Dont expose too much exception details over API

It’s good to inform the user, why their request did not work, but only if they can do something about it (HTTP 4xx).
So in case of application problems, you should not expose details of the problem to the caller.
This way, we avoid opening potential attack vectors.

## Development concepts

### Build, test, deploy

TraceX is built using Maven and utilizes all the standard concepts of it.
Test execution is part of the build process and a minimum test coverage of 80% is enforced.

The project setup contains a multi-module Maven build.
Commonly used classes (like the TraceX data model) should be extracted into a separate submodule and reused across the project.
However, this is not a "one-size-fits-all" solution.
New submodules should be created with care and require a review by the team.

The Maven build alone only leads up to the JAR artifact of TraceX.
Do create Docker images, the Docker build feature is used.
This copies all resources into a builder image, builds the software and creates a final Docker image at the end that can then be deployed.

Although the Docker image can be deployed in various ways, the standard solution are the provided Helm charts, which describe the required components as well.

### Code generation

There are two methods of code generation in TraceX:

#### Lombok

The Lombok library is heavily used to generate boilerplate code (like Constructors, Getters, Setters, Builders...).
This way, code can be written faster and this boilerplate code is excluded from test coverage, which keeps the test base lean.

#### Swagger / OpenAPI

The API uses OpenAPI annotations to describe the endpoints with all necessary information.
The annotations are then used to automatically generate the OpenAPI specification file, which can be viewed in the Swagger UI that is deployed with the application.

The generated OpenAPI specification file is automatically compared to a fixed, stored version of it to avoid unwanted changes of the API.

### Migration

There currently is no data migration mechanism for TraceX.
In case the model of the persisted data (Jobs) changes, data is dropped and Jobs will need to be recreated.

### Configurability

TraceX utilizes the configuration mechanism provided by Spring Boot.
Configuration properties can be defined in the file `src/main/resources/application.yml`

Other profiles should be avoided.
Instead, the configuration can be overwritten using Spring’s external configuration mechanism (see <https://docs.spring.io/spring-boot/docs/2.1.9.RELEASE/reference/html/boot-features-external-config.html).>
The operator must have total control over the configuration of TraceX.

### Java Style Guide

We generally follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

### API Guide

We generally follow the <https://swagger.io/specification/>

### Unit and Functional Testing

#### General Unit testing

* Code coverage >= 80%
* Writing methods which provide a response to be better testable (avoid void if feasible).
* Naming of unit tests are as follows:

![unit_test_naming](https://raw.githubusercontent.com/eclipse-tractusx/traceability-foss/main/docs/src/images/arc42/user-guide/unit_test_naming.png)

* Use given/when/then pattern for unit test structuring.
E.g:

![given_when_then_pattern](https://raw.githubusercontent.com/eclipse-tractusx/traceability-foss/main/docs/src/images/arc42/user-guide/given_when_then_pattern.png)

#### Integration Testing

Each public api should have at least two integration tests (positive / negative).
For integration testing, the `tx-backend/src/main/resources/application-integration.yml` is used.
Additionally, you need to have a local Docker environment running.
For this, we recommend [Rancher Dektop](https://rancherdesktop.io/).

### Clean Code

We follow the rules and behaviour of: <https://clean-code-developer.com/.>

### Secure Coding standards

As there is no other guideline of C-X, we fix any Vulnerabilities, Exposures, Flaw detected by one of our SAST, DAST, Pentesting tools which is higher than "Very Low".

![vulnerability_level](https://raw.githubusercontent.com/eclipse-tractusx/traceability-foss/main/docs/src/images/arc42/user-guide/vulnerability_level.png)

### TRACE-X Technical class responsibilities

#### Controllers

* Have only one dependency to a facade or a service or a validator annotation
* Have no own logic
* Including the swagger documentation annotations
* For each controller exists and Integration Test class
* Uses a static mapper to transform a domain model into the response model
* Returns a ResponseEntity&lt;T>

#### Response object

* Should be a public version of the domain object
* It is a result of the transformation which will be done in the facade
* Is not necessary if the domain object can be fully public
* Is not allowed to be implemented in a repository or a DAO

#### Facade

* Should have multiple service classes injected
* Can be implemented in a controller

#### ServiceImpl

* Responsible for retrieving data from storage
* Performs business logic
* Can be a http client
* Returns a jpaEntity → Domain Object
* Should only be implemented in a controller through an interface

#### Repository

* Represents an interface to the underlying repository implementation which uses then the spring repository

#### Domain Object

* Mapped from an entity or external data received
* Will be used as working model until it will be finally transformed to a response object or another domain which will be later on persisted

#### Config Object

* Should have the suffix Config at the end of the class
* Including beans which are automatically created by app startup

#### Constructing objects

* Using builder pattern
  * Currently we are using the constructor to create objects in our application.
  Main reason is probably to provide immutable objects.
  * As the handling with big loaded constructors is not easy and error prune, I would recommend using the builder pattern to have a clear understanding about what we creating at the point of implementation.
* Using lombok for annotation processing

## Operational concepts

### Administration

#### Configuration

TraceX can be configured using two mechanisms:

##### application.yml

If you build TraceX yourself, you can modify the application.yml config that is shipped with TraceX.
This file contains all possible config entries for the application.
Once the Docker image has been built, these values can only be overwritten using the Spring external config mechanism (see <https://docs.spring.io/spring-boot/docs/2.1.9.RELEASE/reference/html/boot-features-external-config.html),> e.g. by mounting a config file in the right path or using environment variables.

##### Helm Chart

The most relevant config properties are exposed as environment variables and must be set in the Helm chart so the application can run at all.
Check the TraceX Helm chart in Git for all available variables.

### Scaling

If the number of consumers raises, TraceX can be scaled up by using more resources for the Deployment Pod.
Those resources can be used to utilize more parallel threads to handle Job execution.

### Clustering

TraceX can run in clustered mode, as each running job is only present in one pod at a time.
Note: as soon as a resume feature is implemented, this needs to be addressed here.

### Logging

Logs are being written directly to stdout and are picked up by the cluster management.

### Monitoring

Currently, there is on monitoring supported in TraceX.

## Quality requirements

This section includes concrete quality scenarios to better capture the key quality objectives but also other required quality attributes.

## Quality tree

The tree structure provides an overview for a sometimes large number of quality requirements.

![arc42_016](https://hgo-ds.github.io/HGO-RuD-traceability-foss/docs/assets/arc42/arc42_016.png)

## Quality scenarios

The initial letters of the scenario identifiers (IDs) in the following table each stand for the higher-level quality characteristic. For example M for maintainability. These identifiers are also used in the quality tree. The scenarios cannot always be clearly assigned to a characteristic. Therefore, they sometimes appear more than once in the quality tree.

| ID | Scenario |
| --- | --- |
| M01 | A developer with basic knowledge of the Traceability Use Case looks for an introduction to the Traceability architecture. He or she gets the idea of essential design very fast. |
| M02 | A senior developer looks for a reference implementation of the Traceability Use case functionalities. He or she gets it within the source code. |
| M03 | A developer wants to implement new features. He or she is able to add it to the source code easily. |
| M04 | A developer wants to implement a new Frontend or change some components. The efforts can be reduced by using the standardized API endpoints to do so. |
| I01 | An user wants to switch from FOSS application to a COTS application or the other way round. This is possible since the application is interoperable with other applications within the CX network. |
| F01 | The application uses the Catena-X standards to ensure the correct interoperability and exchange with other participants. |
| F02 | An OEM or Tier n supplier needs more information regarding specific parts. He can mark the parts in question and send a top-down notification (Quality Investigation) to the next entity / the partner. |
| F03 | A company wants to have more transparency and a visualized status of the supply / value chain. By using the application they are enabled to work with the structures and enable new features / functionalities. |
| F04 | Notifications are sent using the EDC to ensure interoperability and reliability to the CX network. |
| E01 | Notifications between traceability apps need to be send out and received within a specified timeframe to minimize the negative impact of e.g. recalled serialized products/batches on the value chain. |

## Glossary

| Term | Description |
| --- | --- |
| Aspect | Collection of various aspects from the Digital Twin. An aspect can, for example, bundle all information about a specific part. |
| AAS | ***A****sset **A****dministration **S***hell (Industry 4.0). Is the implementation of the digital twin for Industry 4.0 and enables interoperability between different companies. |
| BoM | ***B****ill **o****f **M***aterial. BoM is a list of parts and materials that a product contains. Without them, manufacturing would not be possible. |
| BoM AsBuilt | Bill of material lifecycle of the built entity |
| BoM AsPlanned | Bill of material lifecycle of the planned entity |
| BPN | ***B****usiness **P****artner **N***umber. A BPN is used to identify a partner in the Catena-X network |
| CX | Abbreviation for Catena-X |
| Data Sovereignty | Ability to keep control over own data, that is shared with other participants |
| EDC | ***E****clipse **D****ataspace **C***onnector. The connector version used in Catena-X |
| FOSS | ***F****ree and **O****pen **S****ource **S***oftware |
| Interoperability | Communication and interaction with other components or Catena-X partners/players |
| IRS | ***I****tem **R****elationship **S***ervice. Component to provide data chains. [IRS architecture documentation (arc42)](https://eclipse-tractusx.github.io/item-relationship-service/docs/arc42/) |
| SME | ***S****mall / **M****edium **E***nterprise |
| Trace-X | Proper name of open-source software application of Catena-X Use Case Traceability |
| UI | ***U****ser **I***nterface |
