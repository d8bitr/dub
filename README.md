dub
===
dub manages database modifications (schema-evolution) during the development process/maintenance in a centralized manner.
The change-sets will be executed against a target database, and will keep an overview of all exising changes (new, current and past).

Change-sets are simple plain sql-files, which are grouped by date (filename), version (directory). The structure of files and directories are placed in a jar-file with an additionally manifest file, containing meta information and configuration properties.
The project consist of two parts: the gui, which is responsible for importing/executing the desired versions aka modifications, and an ant-task, to create the structure and jar-file automatically during the build process.
Only Oracle is supported as database target platform.


Screenshots
-----------
Welcome screen (english)

![Welcome screen (english)](https://github.com/galan/dub/raw/master/images/readme-01.jpg)

Splashscreen

![Splashscreen](https://github.com/galan/dub/raw/master/images/readme-02.jpg)

Configuration (english)

![Configuration (english)](https://github.com/galan/dub/raw/master/images/readme-03.jpg)

Dub Table selection (german)

![Dub Table selection (german)](https://github.com/galan/dub/raw/master/images/readme-04.jpg)

Executing the changes (german)

![Executing the changes (german)](https://github.com/galan/dub/raw/master/images/readme-05.jpg)

