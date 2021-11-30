# utemplate
µTemplate - A microscopic Java template engine

Replaces ${name} with their equivalent value.

## Usage

```java
var msg = new Utemplate()
  .with("name", "World")
  .render("Hello, ${name}!");
```
