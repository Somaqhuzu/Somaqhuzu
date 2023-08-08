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
	MonteCarloMinimizationParallel.class\

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default:$(CLASS_FILES)

clean:
	rm -r $(BINDIR)/*.class

runs: $(CLASS_FILES)
	java -cp bin MonteCarloMinimization 10000 5000 -300 400 -345 200 0.1
run:$(CLASS_FILES)
	java -cp bin MonteCarloMinimizationParallel 10000 5000 -300 400 -345 200 0.1