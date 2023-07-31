JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES=\
	Direction.class\
	TerrainArea.class\
	Search.class\
	MonteCarloMinimization.class\

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default:$(CLASS_FILES)

clean:
	rm -r $(BINDIR)/*.class

run: $(CLASS_FILES)
	java -cp bin MonteCarloMinimization 50 200 22 19 1 3 