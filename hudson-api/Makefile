VERSION=1.0.2

compile:
	xcc -o build -sp src src/

test: compile
	unittest -cp build org.hudson_ci.unittest.**

release: test
	mkdir -p dist ; cd build && xar cvf ../dist/hudson-api.$(VERSION).xar .

clean:
	rm -rvf build/*

