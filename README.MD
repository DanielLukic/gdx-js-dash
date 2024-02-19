# JavaScript BoulderDash in Kotlin using libGdx

i stumbled across a JavaScript BoulderDash clone and was wondering how easy it would be to port this to kotlin.
additionally tryint to minimize the required changes.

for this, i used some parts of my old "IntensiGame" engine from early j2me and then android games i wrote back in
the days. adapted it somewhat quickly to libgdx. don't look at this code please... :-D it is literally about 25
years of age in some parts. i first used this "engine" in a java 2d uridium clone in the mid/late 90s.

for this project only two files are interesting to look at:

* `core/src/main/java/game/BoulderDash.kt`
* `core/src/main/java/game/Caves.kt`

some credits first in the next section before i give some brief "results" below.

## Credits

this is a quick port of the JavaScript BoulderDash found here:

* https://codeincomplete.com/articles/javascript-boulderdash/
* https://github.com/jakesgordon/javascript-boulderdash/

all credit goes to jake gordon for the JS code.

check out his website - really cool game demos available. including demos for outrun, nebulus, delta, ... awesome
work. all source code available. thank you kindly!

https://jakesgordon.com/

additional credit for the micromod code:

* https://github.com/martincameron/micromod

not used for this demo. but there will be a follow-up with some sound and music.

if you want to play the original c64 version including sound:

* https://boulder-dash.com/online-free-game/

# Result

finished the conversion in about half a day. mostly find&replace for js to kotlin. adapted the js objects to small
data classes, changed the iterations where necessary, and used the lambda based key handling because it was easier
to use than to repeat the engine's key handling. added some "also"s to mimic the single line style of the js code.
and iirc added handling of DIR.NONE in a few places.

after this was done, i had a few bugs to fix:

* had to invert the y-axis.
* had to fix a few "moving" / key-handling typos.
* and had to rework the colorizing to fix alpha channel handling.

all things considered this was a good result and a fun experiment.
