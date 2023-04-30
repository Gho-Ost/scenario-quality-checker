Latest build status: 
![last integration](https://github.com/Gho-Ost/scenario-quality-checker/actions/workflows/integration.yml/badge.svg)

# Scenario Quality Checker (SQC)

For analysts documenting functional requirements with scenarios, our SQC application will provide quantitative information and enable detection of problems in functional requirements written in the form of scenarios. The application will be available via GUI and also as a remote API, thanks to which it can be integrated with existing tools.

Format of scenarios:
- The scenario includes a header specifying its title and actors (external and system)
- The scenario consists of steps (each step contains text)
- Steps can contain sub-scenarios (any level of nesting)
- The steps may start with the keywords IF, ELSE, FOR EACH

Example:<br>
Title: Book addition<br>
Actors:  Librarian<br>
System actor: System<br>

- Librarian selects options to add a new book item
- A form is displayed.
- Librarian provides the details of the book.
- IF: Librarian wishes to add copies of the book
    - Librarian chooses to define instances
    - System presents defined instances
    - FOR EACH: instance:
        - Librarian chooses to add an instance
        - System prompts to enter the instance details
        - Librarian enters the instance details and confirms them.
        - System informs about the correct addition of an instance and presents the updated list of instances.
- Librarian confirms book addition.
- System informs about the correct addition of the book.

Corresponding JSON input format:<br>
{
	"Title": "Book addition",<br>
	"Actors": ["Librarian"],<br>
	"System actor": "System",<br>
	"steps": [<br>
		{"Librarian": "selects options to add a new book item"},<br>
		"A form is displayed",<br>
		{"Librarian": "provides the details of the book."},<br>
		{"IF": {"Librarian": "wishes to add copies of the book"}},<br>
		[<br>
			{"Librarian": "chooses to define instances"},<br>
			{"System": "presents defined instances"},<br>
			{"FOR EACH": "instance:"},<br>
			[<br>
				{"Librarian": "chooses to add an instance"},<br>
				{"System": "prompts to enter the instance details"},<br>
				{"Librarian": "enters the instance details and confirms them."},<br>
				{"System": "informs about the correct addition of an instance and presents the updated list of instances."}<br>
			]<br>
		],<br>
		{"Librarian": "confirms book addition."},<br>
		{"System": "informs about the correct addition of the book."}<br>
	]<br>
}
