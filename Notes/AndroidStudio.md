# Debug

1. ```java
   Toast.makeText(this, "message", Toast.LENGTH_SHORT).show();
   ```

2. ```java
   Log.d("Tag", "Message");
   // e: error
   // d: debug
   // w: warning
   // i: information
   // v: verbose
   ```

   open Logcat at the bottom of android studio, select type of log you want and search for the Tag and Message.

   select soft wrap to avoid scrolling horizontally

3. Debugger, set a break point and click debug.