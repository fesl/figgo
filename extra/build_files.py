#!/usr/bin/python
# -*- coding: utf-8 -*-

import sys, getopt, os

def generate_css(input_dir, output_file):
	os.system("sass " + input_dir + "/style.scss " + output_file + " -t compressed")

def generate_js(input_dir, output_file):
	def js_condition(string):
		return not string.startswith("_") and string.endswith(".js")

	files = get_files(input_dir, js_condition)
	if len(files) == 0:
		raise Exception("There is no javascript file in this input directory!")
	os.system("cat " + files + " > /tmp/tmp.js")
	os.system("java -jar closure-compiler.jar --compilation_level ADVANCED_OPTIMIZATIONS --js /tmp/tmp.js --js_output_file " + output_file)

def get_files(path, condition):
	files = ''
	for root, dirs, files_dir in os.walk(path):
		files += ' '.join([os.path.join(root, filename) for filename in files_dir if condition(filename)])
	return files

def valid_syntax(argv):
	opts, args = getopt.getopt(argv[1:], "hsj", ["help", "scss", "js"])

	if (len(argv) != 4):
		raise Exception("Invalid arguments length")
	elif not os.path.exists(args[0]):
		raise Exception("Input directory does not exist")

	return opts, args
	
def usage():
	print "\nUsage: ./build_files.py OPTION INPUT_DIR OUTPUT_FILE"
	print
	print "Options:"
	print "	-s, --scss		Generate solution on screen"
	print "	-j, --js		Print the solution into the specified file"
	print "	-h, --help		Display this help and exit"

def main():
	try:
		opts, args = valid_syntax(sys.argv)
	except Exception, err:
		print "Error:", str(err)
		usage()
		sys.exit(2)

	for opt in opts:
		if opt[0] in ('-s', '--scss'):
			generate_css(args[0], args[1])
		elif opt[0] in ('-j', '--js'):
			generate_js(args[0], args[1])

if __name__ == '__main__':
	main()
