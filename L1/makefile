
# Create the final executable
test: main.o libmystaticlib.a libmysharedlib.so
	gcc -o test main.o -L. -lmystaticlib -L. -lmysharedlib -Wl,-rpath=/home/yasim/OS/L1 -lm

# Compile main.c into main.o
main.o: main.c myheader.h
	gcc -c main.c

# Compile f1.c into f1.o
f1.o: f1.c
	gcc -c f1.c

# Compile f2.c into position independent object code
f2.o: f2.c
	gcc -c -fPIC f2.c

# Create shared library
libmysharedlib.so: f2.o
	gcc -shared -o libmysharedlib.so f2.o

# Create static library
libmystaticlib.a: f1.o
	ar cr libmystaticlib.a f1.o


# Remove generated files
clean:
	rm -f *.o libmystaticlib.a libmysharedlib.so test
