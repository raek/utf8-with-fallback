# UTF-8 with Fallback

Current version: v1.0.0

## What is this?

Sometimes in protocols and formats such as IRC and bad HTML, pieces of text with different encoding are mixed in the same byte stream. This library provides a charset to the JVM platform that can understand both the UTF-8 and the ISO 8859-1 encoding and choose the one that makes most sense.

## How do I use it?

A charset named "X-UTF-8_with_ISO-8859-1_fallback" is provided by this library and can be used in the same way as any built in charset:

    new InputStreamReader(in, "X-UTF-8_with_ISO-8859-1_fallback")

For an example program, see `examples/Mixed2Utf8.java`.

## License

Copyright (C) 2010 Rasmus Svensson

This library is distributed under the GNU Lesser General Public License v3.0 (http://www.gnu.org/licenses/lgpl-3.0-standalone.html) and the Eclipse Public License v1.0 (http://www.eclipse.org/org/documents/epl-v10.html). See files lgpl-3.0.txt and epl-v10.html respectively.
