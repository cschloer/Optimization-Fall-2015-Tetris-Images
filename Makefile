all:
	javac -cp "lib/*" -d class/ Tetrisize/*.java

clean:
	rm -r class/*/*.class

