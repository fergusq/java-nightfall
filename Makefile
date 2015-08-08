all: clear classes

clear:
	echo "" >build/game/joku.class
	rm build/*/*.class

classes:
	javac -source 1.7 -target 1.7 -sourcepath src -cp build src/game/TestApp.java -d build

build/game/WMain.class: src/game/WMain.java
	javac -source 1.7 -target 1.7 -sourcepath src src/game/WMain.java -d build

nightfall.jar: classes build/game/WMain.class
	jar cvf nightfall.jar -C build/ .
