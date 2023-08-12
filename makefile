JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin
ANALYIS=datanalysis

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

cleanbin:
	rm -r $(BINDIR)/*.class

run: $(CLASS_FILES)
	java -cp bin MonteCarloMinimization 20000 2000 -32300 412300 -345123 212300 0.1
runs:$(CLASS_FILES)
	java -cp bin MonteCarloMinimizationParallel 20000 2000 -32300 412300 -345123 212300 0.1 5000

data:
	python3 dataCleaner

clean:
	rm -r *.csv

