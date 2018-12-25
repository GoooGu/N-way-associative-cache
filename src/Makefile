JFLAGS = -g -Xlint
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Cache.java \
	CacheEntry.java \
	ClientCache.java \
	NSetCache.java
	
default: classes test

classes: $(CLASSES:.java=.class)

test: ClientCacheTest.java
	javac -cp junit-4.12.jar:hamcrest-core-1.3.jar:. ClientCacheTest.java
clean:
	rm *.class
