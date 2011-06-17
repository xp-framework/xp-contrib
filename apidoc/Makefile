VERSION?=$(shell cat VERSION)

dist:
	cd src && xar cvf ../apidoc-$(VERSION).xar
	cd .. && zip apidoc-$(VERSION).zip apidoc/apidoc-$(VERSION).xar apidoc/styles/*

clean:
	-rm ../apidoc-*.zip apidoc-*.xar

release:
	scp ../apidoc-$(VERSION).zip xpdoku@xp-forge.net:/home/httpd/xp.php3.de/doc_root/downloads/projects/textproc/
