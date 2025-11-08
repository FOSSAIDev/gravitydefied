## Project Overview

This project is an Android port of the classic J2ME trial racing game "Gravity Defied". The original game was developed by Codebrew Software in 2004. This port aims to replicate the original experience on Android devices.

The application is written in **Java** and appears to be an older Android project configured to be built with **Ant** or imported into an IDE like Eclipse or Android Studio. The project targets Android API level 19 (KitKat).

The core components of the application are:
- **`GDActivity.java`**: The main and only `Activity` in the application. It manages the main game loop, handles the UI, and orchestrates the transitions between the game and the menu.
- **`GameView.java`**: A custom `View` that handles the rendering of the entire game scene, including the player's bike, the level, flags, and other UI elements like the timer.
- **`Physics.java`**: The physics engine responsible for the game's dynamics.
- **`Loader.java`**: This class loads the game levels.
- **`Menu.java`**: Manages the in-game menu system.

The package name is `org.happysanta.gd`.

## Building and Running

This project does not use Gradle, the modern standard for building Android applications. Instead, it appears to be set up for an older build system like **Ant** or to be imported directly into an IDE.

**To build and run this project, you will likely need to:**

1.  Import the project into an IDE that supports older Android project structures, such as a specific version of **Android Studio** or **Eclipse with the ADT plugin**.
2.  When importing, you may need to configure the project to use the correct Android SDK (API 19).
3.  The project has a dependency on `acra-4.5.0.jar` located in the `libs` directory. The IDE should automatically include this in the build path.

There are no build scripts like `build.gradle` or `pom.xml` to automate the build process from the command line.

## Development Conventions

- The code is written in Java and follows standard Java conventions.
- The project is structured into packages based on functionality (e.g., `Game`, `Menu`, `API`).
- The UI is created programmatically within the `GDActivity` and `GameView` classes, rather than using XML layouts for the main game screen.
- The project includes assets for different screen densities (`hdpi`, `mdpi`, `xhdpi`, etc.).
- The code includes a significant number of comments, some of which appear to be translated or transcribed from another language.
- The project uses the ACRA library for crash reporting.
