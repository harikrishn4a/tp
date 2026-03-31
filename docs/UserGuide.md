---
layout: page
title: User Guide
---

* Table of Contents
{:toc}

## What is CrimeWatch?

CrimeWatch is a **command-line contact tracking tool** designed specifically for **law enforcement undercover agents and investigators** to manage suspect profiles and investigation encounters. Instead of carrying physical notebooks or risky digital records on your phone, CrimeWatch allows you to securely and efficiently track suspects, their aliases, risk levels, and encounter history from a single CLI interface.

### Why CrimeWatch?

Traditional contact apps clutter investigations with unnecessary fields and lack investigation-focused features. CrimeWatch is purpose-built for your workflow, enabling you to store contact details when relevant while prioritizing investigation stages, risk levels, and encounter logging:

- **Suspect-focused tracking**: Manage suspects (not regular contacts) with investigation stages and risk levels
- **Encounter logging**: Record every interaction location, time, and observations for building case evidence
- **Quick, keyboard-driven**: Fast command-line interface—no clicking through menus, no distractions
- **Secure data structure**: Stores only investigation-relevant information
- **Bulk reporting**: Export encounter logs by location for case analysis

### Who is this guide for?

This guide is intended for **undercover agents, detective investigators, and law enforcement personnel** who prefer fast, keyboard-driven workflows over graphical interfaces. You should be comfortable with:
- Basic computer operations (installing software, using command terminals)
- Following structured command formats
- Working in a CLI environment

No programming experience is required.

### How to use this guide

- If this is your first time using CrimeWatch, start at [Quick start](#quick-start).
- If you already installed the app, jump to [Features](#features).
- If you need only command syntax, use [Command summary](#command-summary).
- If you are troubleshooting, check [FAQ](#faq) and [Known issues](#known-issues).

### Key Features

CrimeWatch supports 11 core features: **Add**, **Edit**, **Delete** contacts | **Log** and **Edit** encounters | **View** contact details | **Set reminders** | **Search** by keywords | **Export** to CSV | **Sort** the contact list | **Protect** sensitive contacts with passwords. See [Command summary](#command-summary) for detailed formats.

### Password Feature

Optional, contact-level password protection. Each contact can be protected with a password to restrict viewing its full details.

| Feature | Description |
|---------|-------------|
| **Scope** | Per-contact (individual contacts can be protected) |
| **Type** | Optional (contacts do not require passwords) |
| **Usage** | Add `pw/PASSWORD` to `add` or `edit` commands to protect; provide it with `view` to access |
| **Validation** | Alphanumeric characters and spaces only |

**Usage**

```bash
# Add contact with password protection
add n/John Doe a/JD s/surveillance pw/password123

# Update/remove password
edit 1 pw/newpassword
edit 1 pw/

# View protected contact
view 1 pw/password123
view 1
```

**Behavior**
- Without password: contact viewable normally.
- With password: `view` requires correct password to display full details.
- Passwords are stored in plain text (not production-ready).

## Command summary

| Feature | Command format | Go to |
| --- | --- | --- |
| Add Contact | `add n/NAME a/ALIAS s/STAGE [r/RISK] [note/NOTES] [pw/PASSWORD]` | [1) Add Contact](#1-add-contact-add) |
| Edit Contact | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/STAGE] [al/ALIAS(,ALIAS...)] [note/NOTES] [r/RISK] [pw/PASSWORD] [t/TAG]...` | [2) Edit Contact](#2-edit-contact-edit) |
| Delete Contact | `delete INDEX` | [3) Delete Contact](#3-delete-contact-delete) |
| Log Encounter | `log INDEX d/DATE t/TIME l/LOCATION desc/DESCRIPTION [out/OUTCOME]` | [4) Log Encounter](#4-log-encounter-log) |
| Edit Encounter | `editencounter PERSON_INDEX ENCOUNTER_INDEX [d/DATE] [t/TIME] [l/LOCATION] [desc/DESCRIPTION] [out/OUTCOME]` | [5) Edit Encounter](#5-edit-encounter-editencounter) |
| View Contact | `view INDEX [pw/PASSWORD]` | [6) View Contact](#6-view-contact-view) |
| Set Reminder | `remind INDEX d/DATE t/TIME note/NOTE` | [7) Set Reminder](#7-set-reminder-remind) |
| Search Contacts | `find KEYWORD [MORE_KEYWORDS]` | [8) Search Contacts](#8-search-contacts-find) |
| Export encounters (CSV) | `export l/LOCATION` | [9) Export encounters](#9-export-encounters-to-csv-export) |
| Sort Contacts | `sort CRITERION` | [10) Sort Contacts](#10-sort-contacts-sort) |
| Clear All Data | `clear` | [11) Clear All Data](#11-clear-all-data-clear) |
| Exit Application | `exit` | [12) Exit Application](#12-exit-application-exit) |




--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure Java `17` or above is installed.
  **Mac users:** verify the exact JDK setup [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest CrimeWatch `.jar` file from [Releases](https://github.com/se-edu/addressbook-level3/releases).

3. Move the `.jar` file into a folder you want to use as your CrimeWatch home folder.
  A new empty folder is recommended.
  ![Moving .jar file to folder](images/MoveFile.gif)

4. Open a terminal in that folder and run:

   ```bash
   java -jar crimewatch.jar
   ```

   ![Opening the .jar file](images/OpenFile.gif)

5. Confirm the app opens and sample data is visible.
   ![Ui](images/Ui.png)

6. Try this 60-second CLI tutorial:
   - `help` to open this user guide.
   - `list` to show all contacts.
   - `add n/John Doe a/JD s/surveillance r/high note/Observed near station` to add a suspect profile.
   - `view 1` to inspect the first contact.
   - `log 1 d/2026-03-31 t/21:15 l/Maxwell Road desc/Short conversation out/Agreed to follow up` to log an encounter.

7. Expected result after Step 6:
   - You should see one newly added contact.
   - You should see one newly added encounter for that contact.

<div markdown="span" class="alert alert-warning">:exclamation: **Operational warning:**
Do not store classified or highly sensitive intelligence in `note/` or `desc/` fields. Data is saved locally and contact passwords are plain text.
</div>

8. Continue with [Features](#features) for full command details.

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

<div markdown="span" class="alert alert-info">:bulb: **Tip:**
For fastest field operations, use this command rhythm: `find` -> `view` -> `log` -> `remind`.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### 1) Add Contact: `add`

Creates a new suspect profile with aliases, investigation stage, and risk level.

**Format**
`add n/NAME a/ALIAS s/STAGE [r/RISK] [note/NOTES] [pw/PASSWORD]`

**Parameters**
- `n/NAME` (required): suspect's full name (alphanumeric + spaces, not blank)
- `a/ALIAS` (required): one or more aliases, **comma-separated** (e.g. `a/Ah Boy, Johnny T`)
- `s/STAGE` (required): investigation stage
- `r/RISK` (optional): risk level—one of `low`, `medium`, `high` (default: `medium`)
- `note/NOTES` (optional): initial notes (up to 500 characters, no newlines)
- `pw/PASSWORD` (optional): per-contact password used to restrict viewing full contact details

**Examples**
- `add n/John Tan a/Ah Boy s/surveillance`
- `add n/Michael Lee a/Big Mike s/approached r/high note/Seen at Marina Bay`
- `add n/John Doe a/JD s/surveillance pw/password123`

#### Validation rules

**NAME**
- Length: 1–100 characters
- Allowed characters: letters, spaces, apostrophes, hyphens
- Leading/trailing spaces ignored; multiple internal spaces collapsed
- Error if invalid: `Invalid name. Name must contain only letters, spaces, apostrophes or hyphens, and cannot be empty.`

**ALIAS**
- 1–50 characters per alias
- Allowed characters: alphanumeric and spaces
- Multiple aliases separated by commas (e.g., `a/Ah Boy, Johnny T`)
- Error if invalid: `Invalid alias. Alias must be non-empty and alphanumeric.`

**STAGE** (case-insensitive)
Allowed values: `surveillance`, `approached`, `cooperating`, `arrested`, `closed`
- Error if invalid: `Invalid stage. Allowed values: surveillance, approached, cooperating, arrested, closed.`

**RISK** (optional)
Allowed values: `low`, `medium`, `high` (default: `medium`)

**PASSWORD** (optional)
- Allowed characters: alphanumeric and spaces only

#### Duplicate detection
A new contact is considered a duplicate if:
- NAME is identical (case-insensitive, trimmed), **and**
- at least one ALIAS overlaps.

Error if duplicate: `Duplicate contact detected. A contact with similar name and alias already exists.`

**Success output**
`New contact added: [Name] (Stage: X, Risk: Y)`

--------------------------------------------------------------------------------------------------------------------

### 2) Edit Contact: `edit`

Updates details of an existing contact without deleting and re-adding the profile.

**Format**
`edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/STAGE] [al/ALIAS(,ALIAS...)] [note/NOTES] [r/RISK] [pw/PASSWORD] [t/TAG]...`

**Parameters**
- `INDEX` (compulsory): target contact in current list
- At least one prefixed field must be provided
- Any omitted field remains unchanged

**Examples**
- `edit 1 p/91234567 e/johndoe@example.com`
- `edit 2 r/high note/More cooperative in latest meeting`
- `edit 1 pw/newpassword`
- `edit 1 pw/`

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
`view INDEX [pw/PASSWORD]`

**Password behavior**
- Without password: contact is viewable normally.
- With password: `view` requires the correct `pw/PASSWORD` to display full details.
- `view INDEX` on a protected contact fails with password-required error.
- Passwords are stored in plain text (not production-ready).

**Expected output**
- For unprotected contacts: details are shown immediately.
- For protected contacts with correct password: full details and encounter history are shown.
- For protected contacts without/with wrong password: command fails with a password-related error.

**Output (view panel)**
- Name
- Alias(es)
- Stage
- Risk
- Notes
- Encounter History (sorted by date-time ascending)

--------------------------------------------------------------------------------------------------------------------

### 7) Set Reminder: `remind`

Adds a reminder entry to a contact.

**Format**
`remind INDEX d/DATE t/TIME note/NOTE`

**Parameters**
- `INDEX` (compulsory): target contact in current list
- `d/DATE` (compulsory): `YYYY-MM-DD`
- `t/TIME` (compulsory): `HH:mm` (24-hour)
- `note/NOTE` (compulsory): reminder text (not blank)

**Examples**
- `remind 1 d/2026-03-28 t/20:00 note/Meet informant`
- `remind 2 d/2026-04-01 t/09:15 note/Follow up on statement`

**Validation**
- INDEX must exist in the current contact list.
- DATE must be valid and use `YYYY-MM-DD`.
- TIME must be valid and use 24-hour `HH:mm`.
- NOTE cannot be blank.
- Repeating `d/`, `t/`, or `note/` in the same command is not allowed.

**Success output**
`Reminder set for [Name] on [DATE] [TIME].`

--------------------------------------------------------------------------------------------------------------------

### 8) Search Contacts: `find`

Retrieves contacts by keyword across multiple fields.

**Format**
`find KEYWORD [MORE_KEYWORDS]`

**Examples**
- `find john`
- `find mike marina`

**Behavior**
- Case-insensitive
- Partial match allowed
- Matched fields: **Name**, **Alias**, **Notes**
- If no matches:
  `No contacts found matching the given keywords.`

--------------------------------------------------------------------------------------------------------------------

### 9) Export encounters to CSV: `export`

Exports all encounters whose **location** matches the value you give, to a UTF-8 CSV file. Rows are sorted by encounter date-time (earliest first).

**Format**
`export l/LOCATION`

**Parameters**
- `l/LOCATION` (compulsory): must match encounter locations the same way as stored (see **Behavior**).

**Example**
`export l/Harbor District`

#### Behavior
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

### 10) Sort Contacts: `sort`

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

**Behavior**
- Sorting is applied to the displayed list view.
- `sort location`: uses each contact's most recently logged encounter location; contacts without encounters appear last.
- `sort tag`: uses each contact's alphabetically smallest tag; contacts without tags appear last.
- `sort alphabetical`: sorts by contact name (A-Z).
- `sort status`: sorts by stage/status alphabetically.
- `sort recent`: sorts by most recently encountered first.
- Ties are resolved by contact name in alphabetical order.

--------------------------------------------------------------------------------------------------------------------

### 11) Clear All Data: `clear`

Clears all entries from CrimeWatch.

Format: `clear`

### 12) Exit Application: `exit`

Exits the program.

Format: `exit`

### Saving the data

CrimeWatch data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

CrimeWatch data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file make its format invalid, CrimeWatch will discard all data and start with an empty data file at the next run. Hence, it is recommended to back up the file before editing it.<br>
Furthermore, certain edits can cause CrimeWatch to behave in unexpected ways (e.g., if a value entered is outside acceptable ranges). Edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v1.6]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q: How do I transfer my data to another computer?**<br>
**A**: Install the app on the other computer and overwrite the empty data file it creates with the `addressbook.json` file from your previous CrimeWatch home folder.

**Q: Can I edit suspect records after adding them?**<br>
**A**: Yes, use the `edit` command to update any field—name, aliases, stage, risk level, or notes. Existing encounters are preserved.

**Q: What happens if I delete a suspect profile?**<br>
**A**: All associated encounters and reminders are permanently deleted as well. Make sure you export encounter logs to CSV first if you need to retain that data.

**Q: How do I export encounter data for analysis?**<br>
**A**: Use the `export l/LOCATION` command to export all encounters at a specific location to a CSV file. The file is saved in the `exports/` folder under your app home directory.

**Q: What if I need to modify an encounter record I logged earlier?**<br>
**A**: Use the `editencounter` command with the person index and encounter index. Type `view INDEX` first to see all encounters for that suspect, then identify which encounter to edit.

**Q: How do I search for a suspect if I only remember part of their name or alias?**<br>
**A**: Use the `find` command with a keyword. The search is case-insensitive and matches across names, aliases, and notes. For example: `find mike marina`.

**Q: Can I track the same suspect across multiple investigation stages?**<br>
**A**: Yes. Use the `edit` command to update the `s/STAGE` field as the investigation progresses (e.g., from `surveillance` to `arrested` to `closed`).

**Q: My command is giving an error even though it looks correct. What should I check?**<br>
**A**: 1) Ensure you're not repeating prefixes (e.g., `n/... n/...` is invalid). 2) Check date/time formats are exactly `YYYY-MM-DD` and `HH:mm`. 3) Verify the index exists in the current contact list. 4) If copying from a PDF, manually retype the command to avoid hidden space issues.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format | Example
---|---|---
Add Contact | `add n/NAME a/ALIAS s/STAGE [r/RISK] [note/NOTES] [pw/PASSWORD]` | `add n/John Tan a/Ah Boy s/surveillance pw/password123`
Edit Contact | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [s/STAGE] [al/ALIAS(,ALIAS...)] [note/NOTES] [r/RISK] [pw/PASSWORD] [t/TAG]...` | `edit 1 pw/newpassword`
Delete Contact | `delete INDEX` | `delete 3`
Log Encounter | `log INDEX d/DATE t/TIME l/LOCATION desc/DESCRIPTION [out/OUTCOME]` | `log 1 d/2026-02-21 t/18:30 l/Maxwell Road desc/Met...`
Edit Encounter | `editencounter PERSON_INDEX ENCOUNTER_INDEX [d/DATE] [t/TIME] [l/LOCATION] [desc/DESCRIPTION] [out/OUTCOME]` | `editencounter 1 1 desc/Updated notes`
View Contact | `view INDEX [pw/PASSWORD]` | `view 1 pw/password123`
Set Reminder | `remind INDEX d/DATE t/TIME note/NOTE` | `remind 1 d/2026-03-28 t/20:00 note/Meet informant`
Search Contacts | `find KEYWORD [MORE_KEYWORDS]` | `find mike marina`
Export encounters (CSV) | `export l/LOCATION` | `export l/Harbor District`
Sort Contacts | `sort CRITERION` | `sort location`
Clear All Data | `clear` | `clear`
Exit Application | `exit` | `exit`
