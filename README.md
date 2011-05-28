# Development Guide for Straight Project

We use standard Java programming style guidelines found here:
http://www.oracle.com/technetwork/java/codeconv-138413.html

## Package Schema

The source files are organized in packages named using Octahedron domain and project name. Inside the project, source files can be divided in specific package:

*   database
Source files in this package are used to interact with real database API and implement some abstractions used by other system parts.

*    modules
Code in this package implements software modules, that are an independent set of functionalities that solves/describe a real world need. Modules files are divided follow packages:

    * data
	     * Code that implements DAOs. These classes must be named with DAO.
	     * Types that will be persisted to the Database;
	     * Read only types that will not be persisted but represent views of persisted data. These classes must be named with View.
    * manager
	        Code that implements modules functionalities and business logic
	* root
	        For the other cases 
	* test
	    For the test files.
	* view
	    Used to view layer files.
	    
## Facades

Facades are the entry points for a module functionality. You must use them! Every module should have at least one facade to export this functionalities. If a module is accessed only internally, by other modules, it only needs a internal facade. If a module export functionalities for the view layer, it needs an external facade too.

## Facades Tests
To write a facade test class you can use mocks for modules funcionalities and let the complex cases to be tested on more specifics tests (when you write an facade the module is probably already tested).

## Managers Tests
To write managers tests you can use mocks for the managers DAOs. Remember that all the possibly  and complex business cases must me tested here.
    
## DAO
We use some abstract DAOs do implement commons operations. It’s a good practice extends these DAOs and overwrite only absolutely necessary methods. You must test these overwrited methods! To test new DAO methods you can’t mock the GAE, you must use the provided local unit test helper.

## Exceptions
Exceptions must me thrown early as possible. But, for architectural purposes, we use generic DAOs, because of it many exceptions cannot be thrown at DAO layer (that is the earlier layer). Based in that architecture, we believe that the best place to throw exceptions is on the manager classes. 
We believe that exceptions should be caught as late as possible, and if you can’t treat the error, this exception must be unchecked. This decision leads to clear code and let’s the view layer do the job to warn the user.
    
## DataViews
Data views are classes that represents data that can only be read and will not be persisted. They are used when a module needs to pass data from his domain to other modules or to the view layer.

## InfoServices
InfoServices are classes that encapsulates specifics operations to recover data. Used when you want to separate data that will be accessible internally or externally.
