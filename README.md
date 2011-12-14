# JVersionNumberer 

This tool compares two Java libraries (or classes) and shows the differences in the API. You can also use it to automatically generate a version number according to the rules for compatibility as described in http://wiki.eclipse.org/Evolving_Java-based_APIs_2 and http://wiki.eclipse.org/Version_Numbering


## Contributors
The tool was written by Gian Perrone.

## Building
gradle build

## USAGE

java -jar JVersionNumberer/build/libs/JVersionNumberer.jar

This displays a help message explaining further usage.

## Known issues:

- JVersionNumberer has to be used manually; no gradle plugin is included
- The delta output cannot be filtered
- Protected inner classes are handled as if they were public
