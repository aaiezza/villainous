# Disney's Villainous

An object-oriented representation of the board game Villainous.

## Dev Notes

To build and sign, perform the following from the project root:

```
mvn clean install site gpg:sign
```

To deploy to maven repository, run:

```
mvn deploy
```

To view the artifact, visit: https://oss.sonatype.org/ and see in https://central.sonatype.com/
