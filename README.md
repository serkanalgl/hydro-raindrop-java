# Java library for Hydro Raindrop Api

<img src="https://github.com/serkanalgl/hydro-raindrop-java/blob/master/hydro_logo.png">

## Introduction

<p>This java library provides a suite of convenience functions intended to simplify the integration of Hydro's Raindrop authentication into your project.</p>

<p>Offical Raindrop API documentation is available <a href="https://www.hydrogenplatform.com/docs/hydro/v1/">here</a></p>

## Dependency

- <a href="https://github.com/serkanalgl/hydro-oauth2-java">hydro-oauth2-java 1.0.0</a>
- unirest-java 1.4.9


## Compilation

- Java 1.8
- Maven 3


## Installation

### Recommended

Current version is 1.0.0-SNAPSHOT. Will be published in maven central after release.


### Manual

You can also install manually:

```shell
git clone https://github.com/serkanalgl/hydro-raindrop-java.git
cd hydro-raindrop-java
mvn clean install
```

## Usage
 
## Client-side Raindrop
First, you should initialize RaindropPartnerConfig with Builder and then create a new RaindropClient instance with it.

```java

try{

    RaindropPartnerConfig config = new RaindropPartnerConfig.Builder("client id", "client secret", Environment.PRODUCTION)
        .setApplicationId("application id")
        .build(); 
          
    RaindropClient client = new Raindrop().client(config);
    
}catch (Exception e) {
    //something went wrong
}
  
```

To create a new RaindropPartnerConfig object, you must pass the following parameters:

  - `clientId` (required): Your OAuth id for the Hydro API
  - `clientSecret` (required): Your OAuth secret for the Hydro API
  - `environment` (default: SANDBOX): `Environment.SANDBOX` | `Environment.PRODUCTION`
  - `applicationId` (required): Your application id for the Hydro API  
  

### RaindropClient Functions

#### `registerUser(String hydroId)`
To register a user, you will need to collect the HydroID that identifies them on the Hydro app and map it to your application. 

```java

try{

    BaseResponse response = client.registerUser(hydroId); 
    
}catch (RaindropException e) {
    //something went wrong
}
                 
```

- `BaseResponse`
    - `response.getStatus()` : Returns a 200 if the user has been successfully mapped to your application. 
    - `response.getMessage()`: Success/Error message


#### `generateMessage()`
This method generates 6-digit number using with SecureRandom. You should show this number to your users. The user will type into the hydro mobile app.

```java

try{

    Integer message = client.generateMessage(); 
    
}catch (RaindropException e) {
    //something went wrong
}
                 
```


#### `verifySignature(String hydroId, Integer message)`
When the users enter that message into their mobile device, the API will verify that the correct user signed the message against the information stored on the blockchain.

```java

try{

    VerifySignature response = client.verifySignature(hydroId, message); 
    
}catch (RaindropException e) {
    //something went worng
}
                 
```

- `VerifySignature`
    - `response.isVerified()`: Successful verifications will return `true`
    - `response.getVerificationId()`: Returns a UUID for this verification attempt.
    - `response.getTimestamp()`: The time of this verification attempt.


#### `deleteUser(String hydroId)`
For a variety of reasons, a user may want to disable MFA or may delete their account on your platform.

```java

try{

    BaseResponse response = client.deleteUser(hydroId); 
    
}catch (RaindropException e) {
    //something went wrong
}
                 
```

- `BaseResponse`
    - `response.getStatus()` : Returns a 200 deleting their mapping to your application. 
    - `response.getMessage()`: Success/Error message


## Server-side Raindrop
First, you should initialize RaindropPartnerConfig with Builder and then create a new RaindropServer instance with it.

```java

try{

    RaindropPartnerConfig config = new RaindropPartnerConfig.Builder("client id", "client secret", Environment.PRODUCTION).build(); 
        
    RaindropServer server = new Raindrop().server(config);
    
}catch (Exception e) {
    //something went wrong
}
  
```

To create a new `RaindropPartnerConfig` object, you must pass the following parameters:

  - `clientId` (required): Your OAuth id for the Hydro API
  - `clientSecret` (required): Your OAuth secret for the Hydro API
  - `environment` (default: SANDBOX): `Environment.SANDBOX` | `Environment.PRODUCTION`
  

### RaindropServer Functions

#### `whitelist(String address)`
Add address to whitelist ( be careful, for security purposes, the id will only be generated one time. ) The hydroAddressId should be stored on your databasse.

```java

try{

    String address = "0x..."; //The user’s Ethereum/Hydro address

    Whitelist whitelist = server.whitelist(address); 
    
}catch (RaindropException e) {
    //something went wrong
}
                 
```

- `Whitelist`
    - `whitelist.getStatus()` : Returns a 200 if the address has been whitelisted. 
    - `whitelist.getMessage()`: Success/Error message
    - `whitelist.getHydroAddressId()`: The authenticating user’s newly assigned address id
    - `whitelist.getTransactionHash()`: The hash of the transaction whitelisting the user




#### `challenge(String hydroAddressId)`
After being whitelisted, each user must authenticate through the Server-side Raindrop process once every 24 hours to retain access to the protected system.

```java

try{

    Challenge challenge = server.challenge(hydroAddressId);
    
}catch (RaindropException e) {
    //something went wrong
}
                 
```

- `Challenge`
    - `challenge.getStatus()` : Returns a 200 if challenge response is success. 
    - `challenge.getMessage()`: Success/Error message
    - `challenge.getAmount()`: The challenge amount
    - `challenge.getChallenge()`: The challenge string
    - `challenge.getPartnerId()`: The unique identifier assigned to your firm
    - `challenge.getTransactionHash()`: The hash of the transaction that updates the user’s raindrop requirements

You will need to relay these values to the authenticating user who will send them to a <a href="https://www.hydrogenplatform.com/docs/hydro/v1/#Smart-Contract">Hydro smart contract</a>, so treat them accordingly.


#### `authenticate(String hydroAddressId)`
Once the raindrop has been completed by the end user and confirmed in the blockchain, the final authentication check can be performed.

```java

try{

    Authenticate authenticate = server.authenticate(hydroAddressId);
    
}catch (RaindropException e) {
    //something went wrong
}
                 
```

- `Authenticate`
    - `authenticate.isAuthenticated()`: Returns `true` if authenticated
    - `authenticate.getAuthenticationId()`: A UUID for this verification attempt.
    - `authenticate.getTimestamp()`: The time of this verification attempt.



## Generic Functions for Client and Server

### `transactionStatus(String transactionHash)`
Certain methods in the Hydro API trigger transactions on the Ethereum blockchain. Transactions take time to be confirmed, so rather than waiting for confirmation, these methods will return a transaction_hash as soon as our internal logic has completed successfully and the appropriate transaction has been broadcast to the Ethereum network.

Example code for Raindrop Client:
```java

try{

    RaindropPartnerConfig config = new RaindropPartnerConfig.Builder("client id", "client secret", Environment.PRODUCTION)
        .setApplicationId("application id")
        .build(); 
              
    RaindropClient client = new Raindrop().client(config);

    TransactionStatus transactionStatus = client.transactionStatus("0x....");
    
}catch (RaindropException e) {
    //something went wrong
}
                 
```


Example code for Raindrop Server:
```java

try{

    RaindropPartnerConfig config = new RaindropPartnerConfig.Builder("client id", "client secret", Environment.PRODUCTION).build(); 
            
    RaindropServer server = new Raindrop().server(config);

    TransactionStatus transactionStatus = server.transactionStatus("0x....");
    
}catch (RaindropException e) {
    //something went wrong
}
                 
```

- `TransactionStatus`
    - `transactionStatus.isCompleted()`: Indication of whether the transaction has been confirmed
    - `transactionStatus.getTransactionHash()`: The transaction hash




## Contact

If you have any further question/suggestion/issue, do not hesitate to contact me.

serkanalgl@gmail.com


## Donate

### Ethereum <br />

<img src="https://github.com/serkanalgl/hydro-raindrop-java/blob/master/qr.png">


## Copyright

Copyright (c) 2018, Under MIT licence Serkan Algül. All rights reserved.
