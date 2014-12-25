ASP-checker
===========

Tool designed to check whether the advices affecting the execution of a method comply with the Advice Substitution Principle. Developed as a Proof of Concept for a Master Thesis done in the University of Antwerp.
Instructions on how to use the tool as well as a detailed explanation of the checking tasks performed are availble at the [Thesis report](report.pdf).

The ASP-checker uses the [behavioural subtyle checker](https://github.com/luismayorga/behavioural-subtyping) as a basis and provides similar functionality for AspectJ. It is available as an AJDT plugin.

##Purpose

As it happens due to polymorphism in object oriented inheritance, the execution of aspects can carry unexpected consequences. If an aspect advising a method does not behave as a behavioural subtype it can cause undesired effects that are unknown for the code that executes the method.

This checker retrieves all the advices affecting a method and checks whether they comply with the ASP rules. The contracts that can be specified are:

* **Requires**
* **Ensures**
* **Invariant**

There is one additional contract that can be used in the cases in which compliance with the ASP cannot be achieved, such as security or authentication, where a consequence of the advice could be not executing the originally intended functionality.
This is achieved by using the **advisedBy** contract. This contract attaches to the method and marks these changes in its behaviour.

##Usage

The 3 contracts can be attached as Java annotations to both methods an advices. *advisedBy* clauses can be used with methods, while a **name** annotation must be used with the advices in order to give them a name and be included in *advisedBy* clauses.

Contracts can be specified in a subset of Java that covers the basic expressions. For more information about what it is supported, refer to the report. A simple example of a par of advice and method could be the following:

```Java
@requires("a.length > 10")
@advisedBy("bar")
void foo(String a){...}
```

```Java
@ensures("a.isEmpty()")
@name("bar")
void before(String str): call( void *.foo (String)) && args(str) {...}
```

##Install
Information regarding installation can be found in the [INSTALL.md file](https://github.com/rapsioux/asp-checker/blob/master/INSTALL.md).
