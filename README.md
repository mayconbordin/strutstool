StrutsTool
==========

A Struts2 command-line tool for creating projects, controllers, models and views without too much effort and XML configuration.

Why?
----

Well, I had no choice but to use Struts2 to develop web applications. So, I decided to make it as painless as possible.

This tool is meant to reduce as much as possible the configuration of applications, replacing it with convention.

Features
--------

### Basic project structure generation

Generates the basic structure to get started developing with Struts, Hibernate + Validator, Tiles and jQuery.

Command: <font color="red">*`$ ./strutstool new project `<font color="blue">`[project name] [project package]`</font>*</font>

### Entity Scaffolding

What it generates:
* Model entity (POJO)
* POJO basic validation (AntiXSS and NotNull)
* Lucene Search Annotations in POJO <font color="red">(new)</font>
* Hibernate Mapping (based on convention)
* Model Repository (extends a generic repository with basic database methods)
* Model Service (other parts of the application may use this class to get what they want)
* Struts Controller (a controller with CRUD actions)
* Struts Controller Action Map (a configuration file with the relation of the controller actions and their views).
* Dependency Injection with Spring <font color="red">(new)</font>
* Controller properties (a file with messages and other values used in the view)
* JSP Pages (generates four .jsp files: index.jsp, edit.jsp, add.jsp and form.jsp. It includes jQuery basic functionality)
* Tiles Template Configuration (configure each of the .jsp files to use a template)

Command: `applicationName$ ./strutstool scaffold [EntityClassName] [<attribute1:DataType> ...]`

What does it uses
-----------------

* [http://struts.apache.org/ Struts 2.2.1.1]
* [http://code.google.com/p/struts2-jquery/ Struts2 jQuery Plugin 2.5.3]
* [http://www.hibernate.org/ Hibernate 3.6.3]
* [http://www.hibernate.org/subprojects/validator.html Hibernate Validator 4.1.0]
* [http://www.hibernate.org/subprojects/search.html Hibernate Search 3.4.0]
* [http://tiles.apache.org/ Tiles 2.2.2]
* [http://code.google.com/p/htmlcompressor/ HTML Compressor 1.1]
* [http://code.google.com/closure/compiler/ Closure Compiler]
* [http://code.google.com/p/wro4j/ wro4j 1.3.6]
* [http://developer.yahoo.com/yui/compressor/ YUICompressor 2.4.2]
* [http://www.springsource.org/ Spring Framework 3.1.0]
* [http://www.displaytag.org/1.2/ displaytag 1.2]
