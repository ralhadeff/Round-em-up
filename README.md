# Round-em-up
**This is a partially completed game, and was my first original android game created, back in ~2013.  
Game's APK file is provided.**


After having read and followed tutorials for developing games for android I wanted to create my own original game, rather than just follow the tutorials (snake, mario-like, etc). The game engine (`Screen`, etc) was imported from those tutorials (I unfortunately cannot recall the sources used at the time) but all the rest is my original coding.

**Aspects I explored in this project:**
 * Java
 * Android app development
 * open-GLES1.0
 * polymorphism
 * visitor pattern
 * scripted-AI
 * complex behavior of the group (herd) based on fairly simple rules within the individual (the sheep's behavior).


Since 2013 I haven't modified any of the code except for updating the icons.


The game's idea was that you control shepherd dogs and your goal is to guide the dogs (by selecting them, then drawing a running path for them) so that they herd the sheep on the screen into the pen.  
Currently however, there is only one screen, and no win/lose conditions. You can select any of the three dogs (one click) then swipe however you want on the screen, and the dog will follow that path as soon as your finger goes up.  
Because the game was still at the testing phase when I stopped working on it, the player can also control sheep manually, and even cheat by holding two fingers on the screen, and clicking anywhere on the screen with a third finger.

