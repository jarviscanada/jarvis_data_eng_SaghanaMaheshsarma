# Project Overview

Welcome to the SQL Learning Project! In the dynamic landscape of data-driven roles, possessing a solid grasp of SQL (Structured Query Language) is indispensable. This project is designed to serve as a valuable learning experience for developers, data engineers, and analysts, offering an opportunity to hone essential SQL skills that are crucial for both day-to-day tasks and successful interviews.

### Project Highlights

1. **Developer Setup:**
    - Establish a PostgreSQL instance using Docker.
    - Choose a SQL IDE for enhanced development:
        - Option 1: [DBeaver](https://dbeaver.io/download/)
        - Option 2: [pgAdmin](https://www.pgadmin.org/download/)

2. **Data Modeling:**
    - Start a PostgreSQL instance using Docker.
    - Load sample data into your database using the provided `clubdata.sql` file in the `/sql` directory.

3. **Practice Queries:**
    - Engage with hands-on SQL queries in the `/sql` directory.
    - Modify and experiment to deepen your SQL knowledge.


# Database Schema Setup

This set of SQL queries will help create the necessary tables to manage members, bookings, and facilities within a country club setting.

## Table Setup (DDL)

To get started, use the following SQL queries to create the essential tables for your CD Database:

#### 1. Members Table

```sql
-- Create table members
CREATE TABLE cd.members (
    memid INT NOT NULL,
    surname VARCHAR(200) NOT NULL,
    firstname VARCHAR(200) NOT NULL,
    address VARCHAR(300) NOT NULL,
    zipcode INT NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    recommendedby INT,
    joindate TIMESTAMP NOT NULL,
    CONSTRAINT member_pk PRIMARY KEY (memid),
    CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members (memid) ON DELETE SET NULL
);
```

#### 2. Bookings Table

```sql
-- Create table bookings
CREATE TABLE cd.bookings (
    bookid INT NOT NULL,
    facid INT NOT NULL,
    memid INT NOT NULL,
    starttime TIMESTAMP NOT NULL,
    slots INT NOT NULL,
    CONSTRAINT bookings_pk PRIMARY KEY (bookid),
    CONSTRAINT fk_bookings_facid FOREIGN KEY (facid) REFERENCES cd.facilities (facid),
    CONSTRAINT fk_bookings_memid FOREIGN KEY (memid) REFERENCES cd.members (memid)
);
```


#### 3. Facilities Table

```sql
-- Create table facilities
CREATE TABLE cd.facilities (
    facid INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    membercost NUMERIC NOT NULL,
    guestcost NUMERIC NOT NULL,
    initialoutlay NUMERIC NOT NULL,
    monthlymaintenance NUMERIC NOT NULL,
    CONSTRAINT facilities_pk PRIMARY KEY (facid)
);
```
Once the tables are created, we will be ready to populate them with data and explore the functionalities.

# SQL Queries

## Modifying Data

This set of SQL queries is designed to modify data within the CD Database, allowing you to perform essential operations such as adding new facilities, correcting data errors, and managing member entries.

### Question 1 - Adding a New Facility (Spa)

To add a new facility (Spa) to the `facilities` table with specified values:

```sql
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
VALUES (9, 'Spa', 20, 30, 100000, 800);
```

### Question 2 - Adding Spa with Auto-Generated facid

To add the Spa to the `facilities` table with an auto-generated facid:

```sql
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT (SELECT max(facid) FROM cd.facilities) + 1, 'Spa', 20, 30, 100000, 800;
```

### Question 3 - Fixing Data Error for Second Tennis Court

To correct the initial outlay value for the second tennis court:

```sql
-- Update certain value
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE facid = 1;

-- Update multiple values
UPDATE cd.facilities
SET membercost = 6, guestcost = 30
WHERE facid IN (0, 1);
```

### Question 4 - Altering the Price of the Second Tennis Court

To adjust the price of the second tennis court to be 10% more than the first one:

```sql
UPDATE cd.facilities facs
SET membercost = facs2.membercost * 1.1, guestcost = facs2.guestcost * 1.1
FROM (SELECT * FROM cd.facilities WHERE facid = 0) facs2
WHERE facs.facid = 1;
```

### Question 5 - Deleting All Bookings

To delete all bookings from the `cd.bookings` table:

```sql
DELETE FROM cd.bookings;
-- Alternatively: TRUNCATE cd.bookings;
```

### Question 6 - Removing Member 37 with No Bookings

To remove member 37, who has never made a booking, from the database:

```sql
DELETE FROM cd.members WHERE memid = 37;
```


## Basics

### Question 1 - Facilities Charging Fee to Members

How can you produce a list of facilities that charge a fee to members, and that fee is less than 1/50th of the monthly maintenance cost? Return the `facid`, `facility name`, `member cost`, and `monthly maintenance` of the facilities in question.

**Answer:**

```sql
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0 AND (membercost < monthlymaintenance / 50.0);
```

---

### Question 2 - Facilities with 'Tennis' in Their Name

How can you produce a list of all facilities with the word 'Tennis' in their name?

**Answer:**

```sql
SELECT *
FROM cd.facilities
WHERE name LIKE '%Tennis%';
```

---

### Question 3 - Retrieve Details of Specific Facilities

How can you retrieve the details of facilities with ID 1 and 5? Try to do it without using the OR operator.

**Answer:**

```sql
SELECT *
FROM cd.facilities
WHERE facid IN (1, 5);
```

---

### Question 4 - Members Who Joined after September 2012

How can you produce a list of members who joined after the start of September 2012? Return the `memid`, `surname`, `firstname`, and `joindate` of the members in question.

**Answer:**

```sql
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01';
```

---

### Question 5 - Combined List of Surnames and Facility Names

You, for some reason, want a combined list of all surnames and all facility names. Yes, this is a contrived example :-). Produce that list!

**Answer:**

```sql
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;
```
## JOINS

### Question 1 - Bookings by Members Named 'David Farrell'

How can you produce a list of the start times for bookings by members named 'David Farrell'?

**Answer:**

```sql
SELECT starttime
FROM cd.bookings
JOIN cd.members ON cd.bookings.memid = cd.members.memid
WHERE cd.members.firstname = 'David' AND cd.members.surname = 'Farrell';
```

### Question 2 - Bookings for Tennis Courts on '2012-09-21'

How can you produce a list of the start times for bookings for tennis courts on the date '2012-09-21'? Return a list of start time and facility name pairings, ordered by time.

**Answer:**

```sql
SELECT cd.bookings.starttime AS START, cd.facilities.name
FROM cd.bookings
INNER JOIN cd.facilities ON cd.bookings.facid = cd.facilities.facid
WHERE cd.facilities.name IN ('Tennis Court 1', 'Tennis Court 2')
AND cd.bookings.starttime >= '2012-09-21' AND cd.bookings.starttime <= '2012-09-22'
ORDER BY Cd.bookings.starttime;
```

### Question 3 - List of All Members with Recommender Information

How can you output a list of all members, including the individual who recommended them (if any)? Ensure that results are ordered by (surname, firstname).

**Answer:**

```sql
SELECT mems.firstname AS memfname, mems.surname AS memsname,
       recs.firstname AS recfname, recs.surname AS recsname
FROM cd.members mems
LEFT OUTER JOIN cd.members recs ON recs.memid = mems.recommendedby
ORDER BY memsname, Memfname;
```

### Question 4 - List of Members Who Have Recommended Another Member

How can you output a list of all members who have recommended another member? Ensure that there are no duplicates in the list and that results are ordered by (surname, firstname).

**Answer:**

```sql
SELECT DISTINCT recs.firstname AS firstname, recs.surname AS surname
FROM cd.members mems
INNER JOIN cd.members recs ON recs.memid = mems.recommendedby
ORDER BY surname, Firstname;
```

### Question 5 - List of All Members with Recommender Information (Without Joins)

How can you output a list of all members, including the individual who recommended them (if any), without using any joins? Ensure that there are no duplicates in the list, and that each firstname + surname pairing is formatted as a column and ordered.

**Answer:**

```sql
SELECT DISTINCT mems.firstname || ' ' || mems.surname AS member,
                (SELECT recs.firstname || ' ' || recs.surname
                 FROM cd.members recs
                 WHERE recs.memid = mems.recommendedby) AS recommender
FROM cd.members mems
ORDER BY Member;
```

## Aggregations

### Question 1 - Count of Recommendations per Member

Produce a count of the number of recommendations each member has made. Order by member ID.

**Answer:**

```sql
SELECT recommendedby, COUNT(*) AS recommendations_count
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY Recommendedby;
```

### Question 2 - Total Slots Booked per Facility

Produce a list of the total number of slots booked per facility. For now, just produce an output table consisting of facility id and slots, sorted by facility id.

**Answer:**

```sql
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
GROUP BY facid
ORDER BY Facid;
```

### Question 3 - Total Slots Booked per Facility in September 2012

Produce a list of the total number of slots booked per facility in the month of September 2012. Produce an output table consisting of facility id and slots, sorted by the number of slots.

**Answer:**

```sql
SELECT facid, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime >= '2012-09-01' AND starttime < '2012-10-01'
GROUP BY facid
ORDER BY SUM(slots);
```

### Question 4 - Total Slots Booked per Facility per Month in 2012

Produce a list of the total number of slots booked per facility per month in the year of 2012. Produce an output table consisting of facility id and slots, sorted by the id and month.

**Answer:**

```sql
SELECT facid, EXTRACT(month FROM starttime) AS month, SUM(slots) AS "Total Slots"
FROM cd.bookings
WHERE starttime >= '2012-01-01' AND starttime < '2013-01-01'
GROUP BY facid, month
ORDER BY facid, Month;
```

### Question 5 - Total Number of Members (including Guests) with Bookings

Find the total number of members (including guests) who have made at least one booking.

**Answer:**

```sql
SELECT COUNT(DISTINCT memid) FROM Cd.bookings;
```

### Question 6 - Members and Their First Booking after September 1st, 2012

Produce a list of each member name, id, and their first booking after September 1st, 2012. Order by member ID.

**Answer:**

```sql
SELECT mems.surname, mems.firstname, mems.memid, MIN(bks.starttime) AS starttime
FROM cd.bookings bks
INNER JOIN cd.members mems ON mems.memid = bks.memid
WHERE starttime >= '2012-09-01'
GROUP BY mems.surname, mems.firstname, mems.memid
ORDER BY Memid;
```

### Question 7 - Member Names with Total Member Count

Produce a list of member names, with each row containing the total member count. Order by join date, and include guest members.

**Answer:**

```sql
SELECT (SELECT COUNT(*) FROM cd.members) AS count, firstname, surname
FROM cd.members
ORDER BY Joindate;
```

### Question 8 - Monotonically Increasing Numbered List of Members

Produce a monotonically increasing numbered list of members (including guests), ordered by their date of joining. Remember that member IDs are not guaranteed to be sequential.

**Answer:**

```sql
SELECT ROW_NUMBER() OVER (ORDER BY joindate), firstname, surname
FROM cd.members
ORDER BY Joindate;
```

### Question 9 - Facility with the Highest Number of Slots Booked

Output the facility id that has the highest number of slots booked. Ensure that in the event of a tie, all tying results get output.

**Answer:**

```sql
SELECT facid, SUM(slots) AS totalslots
FROM cd.bookings
GROUP BY facid
HAVING SUM(slots) = (SELECT MAX(SUM2.totalslots) FROM (SELECT SUM(slots) AS totalslots FROM cd.bookings GROUP BY facid) AS sum2);
```

## Strings

### Question 1 - Member Names Formatted as 'Surname, Firstname'

Output the names of all members, formatted as 'Surname, Firstname'.

**Answer:**

```sql
SELECT surname || ', ' || firstname AS name
FROM Cd.members;
```

### Question 2 - Telephone Numbers with Parentheses

You've noticed that the club's member table has telephone numbers with very inconsistent formatting. Find all the telephone numbers that contain parentheses, returning the member ID and telephone number sorted by member ID.

**Answer:**

```sql
SELECT memid, telephone
FROM cd.members
WHERE telephone SIMILAR TO '%[()]%';
```

### Question 3 - Count of Members per Surname Initial

Produce a count of how many members you have whose surname starts with each letter of the alphabet. Sort by the letter, and don't worry about printing out a letter if the count is 0.

**Answer:**

```sql
SELECT SUBSTR(mems.surname, 1, 1) AS letter, COUNT(*) AS count
FROM cd.members mems
GROUP BY letter
ORDER BY letter;
```






