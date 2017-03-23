export MFLAGS := --no-print-directory -s
export GFLAGS := -C -E -t -L C++ -c
# export SRCDIR := /Users/lijianlin/usr/Cellar/UPPAAL-dbm-2.0.8/modules
# export DSTDIR := /Users/lijianlin/usr/Cellar/UPPAAL-dbm-2.0.8/modules
export CC  := gcc
export CCC := g++
export AR := /usr/bin/ar
export STRIP = /usr/bin/strip
# export XML_CONFIG := /usr/bin/xml2-config
export CFLAGS := -Wall -O2 -DNDEBUG -stdlib=libstdc++  -DBOOST_DISABLE_THREADS
export SWIG := swig
export SWIGFLAGS := -c++ -java
export JNIPATH := -I/Library/Java/JavaVirtualMachines/jdk1.8.0_71.jdk/Contents/Home/include -I//Library/Java/JavaVirtualMachines/jdk1.8.0_71.jdk/Contents/Home/include/darwin
PREFIX=/usr/local
export UDBMPATH := -I/usr/local/uppaal/include
export CXXFLAGS :=  -std=c++98 -stdlib=libstdc++
# Modules to compile


SOURCES := example.c
# Rules:


# wrap:
# 	$(SWIG) $(SWIGFLAGS) udbm_int.i
# wrap.o: wrap
	$(CCC) $(CXXFLAGS) -c  $(JNIPATH) $(UDBMPATH) udbm_int.h udbm_int_wrap.cxx
	# $(CCC) $(CXXFLAGS) -c  $(UDBMPATH)  -o udbm_int.o udbm_int.h /usr/local/uppaal/lib/libudbm.a
	# echo $@ $*
lib: wrap.o
	$(CCC) $(CXXFLAGS) -shared -fPIC udbm_int_wrap.o /usr/local/uppaal/lib/libudbm.a -o libudbm-swig.so
clean: 
	rm *.o
	rm *.so
	# rm *.java
all: lib
