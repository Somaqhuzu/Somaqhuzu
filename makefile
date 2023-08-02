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
	SearchParallel.class\
	MonteCarloMinimization.class\

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default:$(CLASS_FILES)

clean:
	rm -r $(BINDIR)/*.class

run: $(CLASS_FILES)
	java -cp bin MonteCarloMinimization 1000 1000 -300 400  -345 200 20