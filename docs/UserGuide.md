# User Guide

## Getting Started

CrimeWatch is a **keyboard-driven CLI application**.

* Commands are entered directly into the terminal
* Each command follows a structured format using prefixes (e.g., `n/`, `a/`)
* Inputs must follow strict validation rules

---

## Command Summary

| Feature         | Command Format                                                      |
| --------------- | ------------------------------------------------------------------- |
| Add Contact     | `add n/NAME p/PHONENO e/EMAIL a/ADDRESS s/STAGE [r/RISK] [t/TAG] [al/ALIAS] [note/NOTE]`                  |
| Delete Contact  | `delete INDEX`                                                      |
| Log Encounter   | `log INDEX d/DATE t/TIME l/LOCATION desc/DESCRIPTION [out/OUTCOME]` |
| View Contact    | `view INDEX`                                                        |
| Search Contacts | `find KEYWORD [MORE_KEYWORDS]`                                      |

---

## Feature Guides

### 1. Add Contact

Creates a new person-of-interest profile.

#### Command

```bash
add n/NAME p/PHONENO e/EMAIL a/ADDRESS s/STAGE [r/RISK] [t/TAG] [al/ALIAS] [note/NOTE]
```

#### Required Parameters

* `n/NAME` — Full name
* `p/PHONENO` — Phone number
* `e/EMAIL` — Email address
* `a/ADDRESS` — Address
* `s/STAGE` — Investigation stage

#### Optional Parameters

* `r/RISK` — Risk level (default: `medium`)
* `t/TAG` — Contact tags
* `al/ALIAS` — Alternate name
* `note/NOTE` — Additional notes (max 500 characters)

#### Example

```bash
add n/John Tan p/91234567 e/john@example.com a/Blk 1 Maxwell Road s/surveillance
add n/Michael Lee p/92345678 e/mike@example.com a/Marina Bay s/approached r/high t/suspect al/Big Mike note/Seen at coffee shop
```

#### Success Output

```
New contact added: John Tan (Stage: surveillance, Risk: medium)
```

#### Duplicate Rule

A contact is considered duplicate if:

* Same name (case-insensitive), AND
* At least one alias overlaps (if aliases are provided)

---

### 2. Delete Contact

Removes a contact and all associated encounters permanently.

#### Command

```bash
delete INDEX
```

#### Example

```bash
delete 3
```

#### Success Output

```
Deleted contact: [Name]. All associated encounters are removed.
```

#### Error

```
Invalid index.
```

---

### 3. Log Encounter

Records an interaction with a contact.

#### Command

```bash
log INDEX d/DATE t/TIME l/LOCATION desc/DESCRIPTION [out/OUTCOME]
```

#### Required Parameters

* `d/DATE` — Format: YYYY-MM-DD
* `t/TIME` — Format: HH:mm (24-hour)
* `l/LOCATION` — Encounter location
* `desc/DESCRIPTION` — Description (1–500 characters)

#### Optional

* `out/OUTCOME` — Result or follow-up (max 300 characters)

#### Example

```bash
log 1 d/2026-02-21 t/18:30 l/Maxwell Road desc/Met at coffee shop out/Agreed to cooperate
```

#### Success Output

```
Encounter logged for [Name] on 2026-02-21 18:30.
```

---

### 4. View Contact

Displays full profile and encounter history.

#### Command

```bash
view INDEX
```

#### Output Includes

* Name
* Aliases
* Stage
* Risk
* Notes
* Encounter history (chronological)

---

### 5. Search Contacts

Find contacts using keywords.

#### Command

```bash
find KEYWORD [MORE_KEYWORDS]
```

#### Examples

```bash
find john
find mike marina
```

#### Behavior

* Case-insensitive
* Matches:

  * Name
  * Alias
  * Notes
* Partial matches allowed

#### No Results Output

```
No contacts found matching the given keywords.
```

---

## Data Rules & Validation

### Name (`n/`)

* 1–100 characters
* Letters, spaces, apostrophes, hyphens only
* No numbers or symbols

**Invalid Example**

```
John123
```

---

### Alias (`a/`)

* 1–50 characters each
* Alphanumeric and spaces only
* Multiple aliases separated by commas

---

### Stage (`s/`)

Allowed values:

* surveillance
* approached
* cooperating
* arrested
* closed

---

### Risk (`r/`)

* low
* medium (default)
* high

---

### Date (`d/`)

* Format: YYYY-MM-DD
* Must be a valid calendar date

---

### Time (`t/`)

* Format: HH:mm (24-hour)

---

### Description (`desc/`)

* 1–500 characters
* Cannot be empty

---

### Notes (`note/`)

* Up to 500 characters

---

## Notes

* All commands must follow exact formats
* Invalid input results in no changes
* Error messages clearly indicate the issue
* All operations are immediate and persistent
