foo:
	help = A dummy foo command
	properties:
		arg1:
			help = A simple string argument
			count = 1 1
		arg2:
			help = A marker command with no defaults
			markers = -bar
			count = 1 1
		arg3:
			help = A multi-integer argument
			count = 1 -1
			type = integer
