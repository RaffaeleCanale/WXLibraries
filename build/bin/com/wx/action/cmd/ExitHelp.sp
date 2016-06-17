exit:
	help =
		Exit the current context or the application.
	properties:
		all:
			help = If set, exits the application.
			markers = -a -s
			default = false


help:
	help =
		Display help for a command.
	properties:
		name:
			count = 0 1
			help = Name of the command to display help, or nothing to display this message.