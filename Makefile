JAVAC=javac
JAVA = java
sources = $(shell find . -name '*.java')
classes = $(shell find . -name '*.class')

all: 
	$(JAVAC)  $(sources)
run: 
	$(JAVA) WebServerRunner
clean :
	rm -f $(classes)
