# Overview

Version: 1.0

<p>
A simple implementation of a library management system. Able to add, remove,
and store book objects. The <code>Book</code> class have 5 attributes, a title, author/s, ISBN, genre/s,
and publisher. The library implements a singleton design pattern, only allowing one 
existence of a <code>Library</code> class at all times. Includes simple input validation,
error handling, and javadocs documentation.
</p>

### Features
* Add a book
* Remove an existing book
* Search for a book based on an attribute.
---
### Configurations
* Maven 3.9.8
* JDK 17.0
---
### Build & Run
1. Navigate to root directory of the project where `pom.xml` is located.
2. Use a CLI and enter: `mvn clean install` or use the maven section in Intellij to compile & install.
3. Run the application with `mvn exec:java`.
---
