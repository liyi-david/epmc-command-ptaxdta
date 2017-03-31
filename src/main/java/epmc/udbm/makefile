
export CXX := g++
export CFLAGS := -Wall -O2 -DNDEBUG -stdlib=libstdc++  -DBOOST_DISABLE_THREADS
export CXXFLAGS :=  -std=c++98 -stdlib=libstdc++

export SWIG := swig
export SWIGFLAGS := -c++ -java

export JNIPATH := -I/Library/Java/JavaVirtualMachines/jdk1.8.0_71.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_71.jdk/Contents/Home/include/darwin
export UDBM := /usr/local/uppaal
export UDBMPATH := -I$(UDBM)/include
export LIBUDBM := $(UDBM)/lib/libudbm.a

udbm_int_wrap.cxx:
	$(SWIG) $(SWIGFLAGS) -package epmc.udbm udbm_int.i

udbm_int.o:
	$(CXX) $(CXXFLAGS) -c -fpic udbm_int.cpp $(UDBMPATH)

udbm_int_wrap.o: udbm_int_wrap.cxx
	$(CXX) $(CXXFLAGS) -c -fpic udbm_int_wrap.cxx $(JNIPATH) $(UDBMPATH)

udbm_int.so: udbm_int_wrap.o udbm_int.o
	$(CXX) $(CXXFLAGS) -shared $(LIBUDBM) udbm_int.o udbm_int_wrap.o -o udbm_int.so 

clean: 
	rm *.java
	rm *.cxx
	rm *.o
	rm *.so
all: udbm_int.so
