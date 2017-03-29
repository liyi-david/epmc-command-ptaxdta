export MFLAGS := --no-print-directory -s
export GFLAGS := -C -E -t -L C++ -c
# export SRCDIR := /Users/lijianlin/usr/Cellar/UPPAAL-dbm-2.0.8/modules
# export DSTDIR := /Users/lijianlin/usr/Cellar/UPPAAL-dbm-2.0.8/modules
# export CC  := gcc
export GXX := g++
export AR := /usr/bin/ar
export STRIP = /usr/bin/strip
# export XML_CONFIG := /usr/bin/xml2-config
export CFLAGS := -Wall -O2 -DNDEBUG -stdlib=libstdc++  -DBOOST_DISABLE_THREADS
export SWIG := swig
export SWIGFLAGS := -c++ -java
export JNIPATH := /Library/Java/JavaVirtualMachines/jdk1.8.0_71.jdk/Contents/Home/include -I//Library/Java/JavaVirtualMachines/jdk1.8.0_71.jdk/Contents/Home/include/darwin
PREFIX=/usr/local
export UDBMPATH := /usr/local/uppaal/include
export CXXFLAGS :=  -std=c++98 -stdlib=libstdc++
export LIBUDBM := /usr/local/uppaal/lib/libudbm.a
# Modules to compile


SOURCES := example.c
# Rules:

udbm_int_wrap.cxx:
	$(SWIG) $(SWIGFLAGS) -package epmc.udbm udbm_int.i

udbm_int.o:
	$(GXX) $(CXXFLAGS) -c -fpic udbm_int.cpp -I$(UDBMPATH)

udbm_int_wrap.o: udbm_int_wrap.cxx
	$(GXX) $(CXXFLAGS) -c -fpic udbm_int_wrap.cxx -I$(JNIPATH) -I$(UDBMPATH)

udbm_int.so: udbm_int_wrap.o udbm_int.o
	$(GXX) $(CXXFLAGS) -shared $(LIBUDBM) udbm_int.o udbm_int_wrap.o -o udbm_int.so 


# wrap:
# 	$(SWIG) $(SWIGFLAGS) udbm_int.i
# wrap.o: wrap
# 	$(GXX) $(CXXFLAGS) -c  $(JNIPATH) $(UDBMPATH) udbm_int.h udbm_int_wrap.cxx
# 	# $(GXX) $(CXXFLAGS) -c  $(UDBMPATH)  -o udbm_int.o udbm_int.h /usr/local/uppaal/lib/libudbm.a
# 	# echo $@ $*
# lib: wrap.o
# 	$(GXX) $(CXXFLAGS) -shared -fPIC udbm_int_wrap.o /usr/local/uppaal/lib/libudbm.a -o libudbm-swig.so
clean: 
	rm *.java
	rm *.cxx
	rm *.o
	rm *.so
all: udbm_int.so
