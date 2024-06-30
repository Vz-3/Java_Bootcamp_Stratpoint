# Overview

Version: 1.0

<p>
A simple Ecommerce application that allows users to manage products, add items to their cart, 
and view catalog information. The project includes features for both administrators and regular users.
Includes logging and simple error handling. Unit tests to follow.
</p>

### Features
- <b> Admin Options </b>:
  
    - Add, update, and remove products from the catalog.
    - View products briefly or verbose.
    - Search for products based on various criteria.
        - By name
        - By seller
        - By serial number
    

- <b> User Options </b>:

    - Add, update quantity, and remove products from the cart.
    - View products briefly or verbose.
    - Empty cart
    - View cart items
    - View cart's total price.
---
### Configurations
* Maven 3.9.8
* JDK 17.0

Note: for dependencies, refer to `pom.xml`.

---
### Build & Run
0. Git clone repository, navigate to mini-project-3.
1. Navigate to root directory of the project where `pom.xml` is located.
2. Use a CLI and enter: `mvn clean install` or use the maven section in Intellij to compile & install.
3. Run the application with `mvn exec:java`.
---
### Screenshots
- tbd