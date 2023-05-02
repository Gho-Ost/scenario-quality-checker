Latest build status: 
![last integration](https://github.com/Gho-Ost/scenario-quality-checker/actions/workflows/integration.yml/badge.svg)

# Scenario Quality Checker (SQC)

For analysts documenting functional requirements with scenarios, our SQC application will provide quantitative information and enable detection of problems in functional requirements written in the form of scenarios. The application will be available via GUI and also as a remote API, thanks to which it can be integrated with existing tools.

---

Backlog sheet access: [backlog](https://docs.google.com/spreadsheets/d/10xPEoCOPM9XNCSuVxjXvBpm4q6dU80llhul9FbcC-B8/edit?usp=sharing)<br>
Definition of Done sheet: [DoD](https://docs.google.com/spreadsheets/d/1tAZz23FwqmvO13xXP5R6w4fjLJbzPUflcsjcx2UOEy8/edit?usp=sharing)

---

## Format of scenarios:
- The scenario includes a header specifying its title and actors (external and system)
- The scenario consists of steps (each step contains text)
- Steps can contain sub-scenarios (any level of nesting)
- The steps may start with the keywords IF, ELSE, FOR EACH

### Example:<br>
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
```json
{
	"title": "Book addition",
	"actors": ["Librarian"],
	"systemActor": "System",
	"steps": [
		{"Librarian": "selects options to add a new book item"},
		"A form is displayed",
		{"Librarian": "provides the details of the book."},
		{"IF": {"Librarian": "wishes to add copies of the book"}},
		[
			{"Librarian": "chooses to define instances"},
			{"System": "presents defined instances"},
			{"FOR EACH": "instance:"},
			[
				{"Librarian": "chooses to add an instance"},
				{"System": "prompts to enter the instance details"},
				{"Librarian": "enters the instance details and confirms them."},
				{"System": "informs about the correct addition of an instance and presents the updated list of instances."}
			]
		],
		{"Librarian": "confirms book addition."},
		{"System": "informs about the correct addition of the book."}
	]
}
```

---

## Working with REST api

### working with JSON input in request body

Step counter:
GET /scenario/stepcount

Keyword counter:
GET /scenario/keywordcount

Level cutting:
GET /scenario/levelcut/{maxLevel}

Missing actor:
GET /scenario/missingactor

Download text:
GET /scenario/download

### working with stored scenarios

Adding a scenario to storage:
POST /scenario
(with included request body in JSON)

Getting all scenarios:
GET /scenarios

Getting a scenario by title:
GET /scenario/{title}

Deleting all scenarios:
DELETE /scenarios

Deleting a scenario by title:
DELETE /scenario/{title}

Step counter:
GET /scenarios/{title}/stepcount

Keyword counter:
GET /scenarios/{title}/keywordcount

Level cutting:
GET /scenarios/{title}/levelcut/{maxLevel}

Missing actor:
GET /scenarios/{title}/missingactor

Download:
GET /scenarios/{title}/download
