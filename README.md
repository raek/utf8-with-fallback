# UTF-8 with Fallback

Current version: v1.2.0

## What is this?

Sometimes in protocols and formats such as IRC and bad HTML, pieces of text with different encoding are mixed in the same byte stream. This library provides charsets to the JVM platform that can understand both the UTF-8 and the ISO 8859-1, ISO 8859-15, or windows-1252 encodings and choose the one that makes most sense.

## How do I use it?

This library integrates with the Java charset system. Wherever you would specify a charset, such as "UTF-8", you can also choose among one of the following charsets that this library provides:

* "X-UTF-8_with_ISO-8859-1_fallback"
* "X-UTF-8_with_ISO-8859-15_fallback"
* "X-UTF-8_with_windows-1252_fallback"

Note that these charsets can only be used for decoding. An example, using the first one to decode data from an InputStream:

    new InputStreamReader(in, "X-UTF-8_with_ISO-8859-1_fallback")

For an example program, see `examples/Mixed2Utf8.java`.

## How do I install it?

You can either download it manually or let Maven (or a Maven based build tool) fetch it for you.

### Download JAR manually

    https://github.com/raek/utf8-with-fallback/archives/master

### Leiningen

    [se.raek/utf8-with-fallback "1.2.0"]

### Maven

    <dependency>
      <groupId>se.raek</groupId>
      <artifactId>utf8-with-fallback</artifactId>
      <version>1.2.0</version>
    </dependency>

You might also need to add the Clojars repo:

    <repository>
      <id>clojars.org</id>
      <url>http://clojars.org/repo</url>
    </repository>

## Limitations

In rare circumstances, text encoded in one of the fallback charsets can be valid UTF-8, leading to wrongly decoded text. Example:

* Original text: `å½¤`
* Encoded in ISO 8859-1: `E5 BD A4`
* Decoded in UTF-8: `彤` (`\u5F64`)

## Version History

* 1.2.0 - Bugfixes and support for ISO 8859-15 (Java 8+)
* 1.1.0 - Bugfixes and support for windows-1252
* 1.0.0 - Initial version

## License

Copyright (C) 2010-2022 Rasmus Bondesson and the contributors

This library is distributed under the GNU Lesser General Public License v3.0 (http://www.gnu.org/licenses/lgpl-3.0-standalone.html) and the Eclipse Public License v1.0 (http://www.eclipse.org/org/documents/epl-v10.html).
