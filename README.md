vending-machine-kata
====================

This is a simple exercise vending-machine-kata - in which you will simulate the...
vending machine ( https://en.wikipedia.org/wiki/Vending_machine )

The project is maven based. We are providing maven wrapper so if do not have maven installed call `./mvnw` (or `mvnw.cmd` on Windows) to 
build it.
On opening you will find two classes: 
`VendingMachine` and `VendingMachineTest`.
The second one contains one test, which you can easily remove, since it is not part of the assignment.
You can also rename or even remove provided classes.
Below you will find requirements, key aspects and most importantly assignment itself.

You are more than welcome to tell us (either in email or in the README) what you would do differently, if you had more time.


Requirements
---------

* Please solve the exercise using Java language. We prefer Java 7 or 8.
* We are providing Maven configuration, but if you want to switch to Gradle feel free to change that.
If you want to use different build tool, then provide a way to execute build **with tests**.
Take into account simplicity of provided solution, we would like to spent more time analyzing your code then configuring build environment.
* Please send us solution as pull request on GitHub or link to your repository which we can clone.

Key aspects
----------------

* we would like to see how you are using Test-Driven Development
* we will check your skills in object and domain design (Object-Oriented Programming, Domain-Driven Design)
* we will check craftsmanship of production and test code (Clean Code)
* we will check if the algorithm is correct
* we will check usage of design patterns (we will be also interested in why you chose them)
* we will check your commits for atomicity and readability

The assignment
------------

1. Vending machine contains products.
2. Products can be of different types (i.e. Cola drink 0.25l, chocolate bar, mineral water 0.33l and so on).
3. Products are on shelves.
4. One shelve can contain only one type of product (but multiple products).
5. Each product type has its own price.
6. Machine has a display.
7. If we select shelve number, display should show product price.
8. You can buy products by putting money into machine. Machine accepts denominations 5, 2, 1, 0.5, 0.2, 0.1.
9. After inserting a coin, display shows amount that must be added to cover product price.
10. After selecting a shelve and inserting enough money we will get the product and the change (but machine has to have money to be able to return the change).
11. After selecting a shelve and inserting insufficient money to buy a product, user has to press "Cancel" to get their money back.
12. If machine does not have enough money to give the change it must show a warning message and return the money user has put, and it should not give the product.
13. Machine can return change using only money that was put into it (or by someone at start or by people who bought goods). Machine cannot create it's own money!
