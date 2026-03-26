---
layout: page
title: User Guide
---
## Who is this guide for?
This guide is intended for users who prefer fast, keyboard-driven workflows. You should be comfortable with basic computer operations such as installing software and using a command terminal. No programming experience is required.

## What is CrimeWatch?
CrimeWatch is a CLI-based contact tracking tool for managing **person-of-interest profiles** and their **encounter logs**. It supports the following features:

1. Add Contact
2. Edit Contact
3. Delete Contact
4. Log Encounter
5. Edit Encounter
6. View Contact
7. Search Contacts
8. Export encounters (CSV)
9. Sort Contacts

## Command summary

| Feature | Command format |
| --- | --- |
| Add Contact | `add n/NAME a/ALIAS s/STAGE [r/RISK] [note/NOTES]` |
| Edit Contact | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/STAGE] [al/ALIAS(,ALIAS...)] [note/NOTES] [r/RISK] [t/TAG]...` |
| Delete Contact | `delete INDEX` |
| Log Encounter | `log INDEX d/DATE t/TIME l/LOCATION desc/DESCRIPTION [out/OUTCOME]` |
| Edit Encounter | `editencounter PERSON_INDEX ENCOUNTER_INDEX [d/DATE] [t/TIME] [l/LOCATION] [desc/DESCRIPTION] [out/OUTCOME]` |
| View Contact | `view INDEX` |
| Search Contacts | `find KEYWORD [MORE_KEYWORDS]` |
| Export encounters (CSV) | `export l/LOCATION` |
| Sort Contacts | `sort CRITERION` |

   
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. First, make sure you have Java `17` or above installed in your computer!<br>
   **Mac users:** Check that you have the exact JDK version [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Next, download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

3. Then, move the 'crimewatch.jar' file to the folder you want to use as the _home folder_ for your AddressBook. (A new, empty folder is recommended) <br>
![Moving .jar file to folder](images/MoveFile.gif)

5. Now, open a command terminal from the folder you put the .jar file in. In the terminal, use the `java -jar addressbook.jar` command to run the application. <br>
![Opening the .jar file](images/OpenFile.gif)

6. The crimewatch app should appear! By default, the app has some sample data. <br>
   ![Ui](images/Ui.png)

7. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

8. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Notes about the command format

- Words in `UPPER_CASE` are placeholders you replace with your own values.
- Prefixes use the format `prefix/value` (e.g. `n/John Tan`).
- Parameters can be in any order unless stated otherwise.
- Optional parameters are shown in square brackets `[LIKE_THIS]`.
- **Do not repeat prefixes** in the same command (e.g. `n/... n/...`) — this is treated as an error.
- Index-based commands (`view`, `log`, `delete`, `edit`) use the **INDEX shown in the current contact list panel**.
  - INDEX must be a positive integer: `1, 2, 3, ...`
- For `editencounter`, use two indices: `PERSON_INDEX` from contact list and `ENCOUNTER_INDEX` from the viewed encounter cards.
 
--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### 1) Add Contact: `add`

Creates a new contact profile (suspect / person of interest).

**Format**
`add n/NAME a/ALIAS s/STAGE [r/RISK] [note/NOTES]`

**Parameters**
- `n/NAME` (compulsory): full name
- `a/ALIAS` (compulsory): one or more aliases (**comma-separated**)
- `s/STAGE` (compulsory): investigation stage
- `r/RISK` (optional): risk level; default is `medium`
- `note/NOTES` (optional): initial notes (up to 500 characters)

**Examples**
- `add n/John Tan a/Ah Boy s/surveillance`
- `add n/Michael Lee a/Big Mike s/approached r/high note/Seen at Marina Bay`

#### Validation rules

**NAME**
- Length: 1–100 characters
- Allowed characters: letters, spaces, apostrophes, hyphens
- Leading/trailing spaces ignored; multiple internal spaces collapsed
- Error message (invalid):  
  `Invalid name. Name must contain only letters, spaces, apostrophes or hyphens, and cannot be empty.`

**ALIAS**
- 1–50 characters per alias
- Allowed characters: alphanumeric and spaces
- Multiple aliases separated by commas (e.g. `a/Ah Boy, Johnny T`)
- Error message (invalid):  
  `Invalid alias. Alias must be non-empty and alphanumeric.`

**STAGE** (case-insensitive)
Allowed values:
- `surveillance`
- `approached`
- `cooperating`
- `arrested`
- `closed`

Error message (invalid):  
`Invalid stage. Allowed values: surveillance, approached, cooperating, arrested, closed.`

**RISK** (optional)
Allowed values: `low`, `medium`, `high` (default: `medium`)

#### Duplicate handling
A new contact is considered a duplicate if:
- NAME is identical (case-insensitive, trimmed), **and**
- at least one alias overlaps.

Error message:
`Duplicate contact detected. A contact with similar name and alias already exists.`

**Success output**
`New contact added: [Name] (Stage: X, Risk: Y)`

--------------------------------------------------------------------------------------------------------------------

### 2) Edit Contact: `edit`

Updates details of an existing contact without deleting and re-adding the profile.

**Format**
`edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/STAGE] [al/ALIAS(,ALIAS...)] [note/NOTES] [r/RISK] [t/TAG]...`

**Parameters**
- `INDEX` (compulsory): target contact in current list
- At least one prefixed field must be provided
- Any omitted field remains unchanged

**Examples**
- `edit 1 p/91234567 e/johndoe@example.com`
- `edit 2 r/high note/More cooperative in latest meeting`

**Validation**
- INDEX must exist in the current list.
- Provided fields follow the same validation rules as `add`.
- Repeating non-tag prefixes in the same command is not allowed.

**Success output**
`Edited Person: [person details]`

--------------------------------------------------------------------------------------------------------------------

### 3) Delete Contact: `delete`

Removes a contact **and all associated encounters** permanently.

**Format**
`delete INDEX`

**Example**
`delete 3`

**Validation**
- INDEX must exist in the current list.
- Error: `Invalid index.`

**Success output**
`Deleted contact: [Name]. All associated encounters are removed.`

--------------------------------------------------------------------------------------------------------------------

### 4) Log Encounter: `log`

Records an interaction with a contact and appends it to the contact’s encounter history.

**Format**
`log INDEX d/DATE t/TIME l/LOCATION desc/DESCRIPTION [out/OUTCOME]`

**Parameters**
- `d/DATE` (compulsory): `YYYY-MM-DD`
- `t/TIME` (compulsory): `HH:mm` (24-hour)
- `l/LOCATION` (compulsory): location text
- `desc/DESCRIPTION` (compulsory): what happened (1–500 chars, not blank)
- `out/OUTCOME` (optional): result/follow-up (up to 300 chars)

**Example**
`log 1 d/2026-02-21 t/18:30 l/Maxwell Road desc/Met at coffee shop out/Agreed to cooperate`

#### Validation rules
- DATE must be a valid calendar date  
  Error: `Invalid date. Use format YYYY-MM-DD.`
- TIME must be valid 24-hour `HH:mm`  
  Error: `Invalid time. Use 24-hour format HH:mm.`
- DESCRIPTION cannot be blank; 1–500 characters

**Success output**
`Encounter logged for [Name] on 2026-02-21 18:30.`

--------------------------------------------------------------------------------------------------------------------

### 5) Edit Encounter: `editencounter`

Updates an existing encounter for a contact.

**Format**
`editencounter PERSON_INDEX ENCOUNTER_INDEX [d/DATE] [t/TIME] [l/LOCATION] [desc/DESCRIPTION] [out/OUTCOME]`

**Parameters**
- `PERSON_INDEX` (compulsory): target contact in current list
- `ENCOUNTER_INDEX` (compulsory): target encounter from the viewed encounter cards
- At least one prefixed field must be provided

**Encounter index mapping**
- Encounter cards shown in `view` are numbered newest first.
- `ENCOUNTER_INDEX 1` means the most recent encounter shown as `#1`.

**Examples**
- `editencounter 1 1 desc/Updated observation notes`
- `editencounter 1 2 d/2026-03-27 t/20:15 l/Tanjong Pagar out/`

**Validation**
- PERSON_INDEX must exist in the current contact list.
- ENCOUNTER_INDEX must exist for that contact.
- Provided fields use the same validation rules as `log`.
- `out/` with an empty value clears outcome.

**Success output**
`Edited encounter #[ENCOUNTER_INDEX] for [Name].`

--------------------------------------------------------------------------------------------------------------------

### 6) View Contact: `view`

Displays the full profile of a contact and their chronological encounter history.

**Format**
`view INDEX`

**Output (view panel)**
- Name
- Alias(es)
- Stage
- Risk
- Notes
- Encounter History (sorted by date-time ascending)

--------------------------------------------------------------------------------------------------------------------

### 7) Search Contacts: `find`

Retrieves contacts by keyword across multiple fields.

**Format**
`find KEYWORD [MORE_KEYWORDS]`

**Examples**
- `find john`
- `find mike marina`

**Behaviour**
- Case-insensitive
- Partial match allowed
- Matched fields: **Name**, **Alias**, **Notes**
- If no matches:  
  `No contacts found matching the given keywords.`

--------------------------------------------------------------------------------------------------------------------

### 8) Export encounters to CSV: `export`

Exports all encounters whose **location** matches the value you give, to a UTF-8 CSV file. Rows are sorted by encounter date-time (earliest first).

**Format**
`export l/LOCATION`

**Parameters**
- `l/LOCATION` (compulsory): must match encounter locations the same way as stored (see **Behaviour**).

**Example**
`export l/Harbor District`

#### Behaviour
- Matching is **case-insensitive**. Leading and trailing spaces on your input and on each stored encounter location are ignored; the trimmed strings must be equal.
- The file is written under the app home directory to `exports/CrimeWatch-export-<timestamp>.csv`, where `<timestamp>` is in `yyyyMMdd-HHmmss` form (local time when the command runs).
- CSV columns (header row): `encounterTimestamp`, `encounterDescription`, `encounterOutcome`, `contactName`, `contactTags`. Tags for a contact are comma-separated and sorted alphabetically. Fields are quoted and follow standard CSV escaping for double quotes.

#### Outcomes
- **Success:** `Exported N matching encounters to exports/CrimeWatch-export-<timestamp>.csv.` (with the actual path shown).
- **No matching encounters:** the command fails with `No encounters found at location <your location>.` — **no file** is created.
- **Invalid format** (e.g. missing `l/`, wrong shape): invalid command format message referencing `export` usage.
- **Blank location** (after trim): `Encounter location can take any value, and should not be blank`
- **Write error** (e.g. cannot create `exports/`): `Failed to export to <path>: <reason>`

--------------------------------------------------------------------------------------------------------------------

### 9) Sort Contacts: `sort`

Sorts the currently displayed contact list by a chosen criterion.

**Format**
`sort CRITERION`

**Allowed criteria** (case-insensitive)
- `location`
- `tag`
- `alphabetical`
- `status`
- `recent`

**Examples**
- `sort location`
- `sort tag`
- `sort alphabetical`
- `sort status`
- `sort recent`

**Behaviour**
- Sorting is applied to the displayed list view.
- `sort location`: uses each contact's most recently logged encounter location; contacts without encounters appear last.
- `sort tag`: uses each contact's alphabetically smallest tag; contacts without tags appear last.
- `sort alphabetical`: sorts by contact name (A-Z).
- `sort status`: sorts by stage/status alphabetically.
- `sort recent`: sorts by most recently encountered first.
- Ties are resolved by contact name in alphabetical order.

--------------------------------------------------------------------------------------------------------------------

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format | Example
---|---|---
Add Contact | `add n/NAME a/ALIAS s/STAGE [r/RISK] [note/NOTES]` | `add n/John Tan a/Ah Boy s/surveillance`
Edit Contact | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/STAGE] [al/ALIAS(,ALIAS...)] [note/NOTES] [r/RISK] [t/TAG]...` | `edit 1 p/91234567 r/high`
Delete Contact | `delete INDEX` | `delete 3`
Log Encounter | `log INDEX d/DATE t/TIME l/LOCATION desc/DESCRIPTION [out/OUTCOME]` | `log 1 d/2026-02-21 t/18:30 l/Maxwell Road desc/Met...`
Edit Encounter | `editencounter PERSON_INDEX ENCOUNTER_INDEX [d/DATE] [t/TIME] [l/LOCATION] [desc/DESCRIPTION] [out/OUTCOME]` | `editencounter 1 1 desc/Updated notes`
View Contact | `view INDEX` | `view 1`
Search Contacts | `find KEYWORD [MORE_KEYWORDS]` | `find mike marina`
Export encounters (CSV) | `export l/LOCATION` | `export l/Harbor District`
Sort Contacts | `sort CRITERION` | `sort location`
