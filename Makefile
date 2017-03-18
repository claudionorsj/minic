
JAVACUP   := java -jar ../../lib/java-cup-11a.jar

all: src/mini_c/Lexer.java src/mini_c/Parser.java src/mini_c/Main.class

.PHONY: clean
clean:
	rm -f src/mini_c/*.class src/mini_c/Lexer.java src/mini_c/Parser.java src/mini_c/sym.java

src/mini_c/Parser.java src/mini_c/sym.java: src/mini_c/Parser.cup
	cd src/mini_c/ && $(JAVACUP) -package mini_c -parser Parser Parser.cup

src/mini_c/Main.class:
	cd src/mini_c/ && javac -cp ../../lib/java-cup-11a-runtime.jar *.java

%.java: %.flex
	rm -f $@
	jflex $<
