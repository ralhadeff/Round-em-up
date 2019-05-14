# Round-em-up
**This is a partially completed game, which was my first original android game created, back in ~2013.  
Game's APK file is provided.**


After having read and followed tutorials for developing games for android I wanted to create my own original game, rather than just follow the tutorials (snake, mario-like, etc). The game engine (`framework` folder) was imported from those tutorials (I unfortunately cannot recall the sources used at the time) but all the rest is my original coding.  

**Aspects I explored in this project:**
 * Coding with Java
 * Android app development
 * open-GLES1.0
 * polymorphism
 * visitor pattern
 * scripted-AI
 * complex behavior of a group (the "herd") based on fairly simple behavior rules for the individual (behavior of a single sheep with its surrounding).


Since 2013 I haven't modified any of the code except for updating the icons.


The game's principle was to control shepherd dogs in order to herd all the sheep into the pen. During gameplay the player would guide the dogs (by selecting them and drawing a path for them to run), and the running dogs would scare the sheep away. The player would need to strategize a pattern such that the running sheep would enter the pen on the screen.    
Currently however, there is only one screen, and no win/lose conditions. You can select any of the three dogs (by touching them once) then swipe however you want on the screen, and the dog will follow that path as soon as your finger goes up.  
Because the game was still at the testing phase when I stopped working on it, the player can also control a sheep manually, and even cheat by holding two fingers on the screen, and clicking anywhere on the screen with a third finger, to guide all the sheep forcibly to that point. Note that the sheep have an AI, so they do not follow orders obediently.
