# Setup

This file describes how to install the plugin and all its dependencies.
Note that although instructions for Windows are also provided, it is an environment in which it has not been thoroughly tested.
The platform used for testing is OS X 10.9.2.

## Z3

Z3 and its Java bindings should be installed in the system and be accessible in order to use the plugin.
As Z3 is a research product and its Java functionality is still quite recent, the process to install it is somewhat tricky.
It can be done as follows:

+ Clone the unstable branch of Z3 from codeplex.
        Make sure that you have a Git version above 1.7.10.
        `git clone https://git01.codeplex.com/z3 -b unstable`
+ Checkout the **z3-java** tag:.
    1. `cd z3`
    2. `git checkout z3-java`
+ Generate the Z3 make file:  
        `python scripts/mk\_make.py --java`
+ Build Z3 and install.
    1. `cd build`
    2. `make`
    3. `make install`

+ Copy the generated libraries to the Java native libraries folder in your system.
    - **OS X:** `libz3java.dylib` into /Library/Java/Extensions
    - **Windows:** `libz3java.dll` into `\textless JAVA\_HOME\textgreater \textbackslash jre\textbackslash lib\textbackslash ext`  
                                E.g. `"c:\textbackslash Program Files\textbackslash Java\textbackslash jdk1.7.0\_{xx}\textbackslash jre\textbackslash lib\textbackslash ext"
`

## Ekeko

The plugin uses Ekeko to query the weaver about the aspects and classes of the model as well as to provide an interactive shell to run the checking process.

In order to install it, a plugin for working with Clojure within JDT, Counterclockwise, must be installed.

+ Install Counterclockwise from its Eclipse update site.  
   `http://updatesite.ccw-ide.org/stable/`
+ Install Ekeko from the update site:  
`http://soft.vub.ac.be/\textasciitilde cderoove/eclipse/`

## ASP checker

+ Clone the checker from the online repository  
  `git clone git@github.com:rapsioux/asp-checker.git`
+ Import it as an Eclipse project. Eclipse for RCP and RAP Developers is required.
+ Export as a deployable plugin.
+ Copy the generated `jar` file into the `dropins` folder of the Eclipse distribution in which you plan to use it.
+ Export the package `be.ac.ua.aspchecker.annotations` to a jar file in order to use it in the analysed projects.

