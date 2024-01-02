--all the queries for the questions

INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
		values(9, 'Spa', 20, 30, 100000, 800);

INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
SELECT
	(
		SELECT
			max(facid)
		FROM
			cd.facilities) + 1,
	'Spa',
	20,
	30,
	100000,
	800;

--update certain value
UPDATE
	cd.facilities
SET
	initialoutlay = 10000
WHERE
	facid = 1;

--update multiple values
UPDATE
	cd.facilities
SET
	membercost = 6,
	guestcost = 30
WHERE
	facid in(0, 1);


UPDATE
	cd.facilities facs
SET
	membercost = facs2.membercost * 1.1,
	guestcost = facs2.guestcost * 1.1
FROM (
	SELECT
		*
	FROM
		cd.facilities
	WHERE
		facid = 0) facs2
WHERE
	facs.facid = 1;

DELETE FROM cd.bookings;

TRUNCATE cd.bookings;

DELETE FROM cd.members
WHERE memid = 37;


SELECT
	facid,
	name,
	membercost,
	monthlymaintenance
FROM
	cd.facilities
WHERE
	membercost > 0
	and(membercost < monthlymaintenance / 50.0);


SELECT
	*
FROM
	cd.facilities
WHERE
	name LIKE '%Tennis%';

SELECT
	*
FROM
	cd.facilities
WHERE
	facid in(1, 5);


SELECT
	memid,
	surname,
	firstname,
	joindate
FROM
	cd.members
WHERE
	joindate >= '2012-09-01';


SELECT
	surname
FROM
	cd.members
UNION
SELECT
	name
FROM
	cd.facilities;


SELECT
	starttime
FROM
	cd.bookings
	JOIN cd.members ON cd.bookings.memid = cd.members.memid
WHERE
	cd.members.firstname = 'David'
	AND cd.members.surname = 'Farrell';


SELECT
	cd.bookings.starttime AS START,
	cd.facilities.name
FROM
	cd.bookings
	INNER JOIN cd.facilities ON cd.bookings.facid = cd.facilities.facid
WHERE
	cd.facilities.name in('Tennis Court 1', 'Tennis Court 2')
	AND cd.bookings.starttime >= '2012-09-21'
	AND cd.bookings.starttime <= '2012-09-22'
ORDER BY
	cd.bookings.starttime;


SELECT
	mems.firstname AS memfname,
	mems.surname AS memsname,
	recs.firstname AS recfname,
	recs.surname AS recsname
FROM
	cd.members mems
	LEFT OUTER JOIN cd.members recs ON recs.memid = mems.recommendedby
ORDER BY
	memsname,
	memfname;


SELECT DISTINCT
	recs.firstname AS firstname,
	recs.surname AS surname
FROM
	cd.members mems
	INNER JOIN cd.members recs ON recs.memid = mems.recommendedby
ORDER BY
	surname,
	firstname;


SELECT DISTINCT
	mems.firstname || ' ' || mems.surname AS member,
	(
		SELECT
			recs.firstname || ' ' || recs.surname AS recommender
		FROM
			cd.members recs
		WHERE
			recs.memid = mems.recommendedby)
	FROM
		cd.members mems
	ORDER BY
		member;


SELECT
	recommendedby,
	count(*)
FROM
	cd.members
WHERE
	recommendedby IS NOT NULL
GROUP BY
	recommendedby
ORDER BY
	recommendedby;


SELECT
	facid,
	sum(slots) AS "Total Slots"
FROM
	cd.bookings
GROUP BY
	facid
ORDER BY
	facid;


SELECT
	facid,
	sum(slots) AS "Total Slots"
FROM
	cd.bookings
WHERE
	starttime >= '2012-09-01'
	AND starttime < '2012-10-01'
GROUP BY
	facid
ORDER BY
	sum(slots);


SELECT
	facid,
	extract(month FROM starttime) AS month,
	sum(slots) AS "Total Slots"
FROM
	cd.bookings
WHERE
	starttime >= '2012-01-01'
	AND starttime < '2013-01-01'
GROUP BY
	facid,
	month
ORDER BY
	facid,
	month;


SELECT
	count(DISTINCT memid)
FROM
	cd.bookings;


SELECT
	mems.surname,
	mems.firstname,
	mems.memid,
	min(bks.starttime) AS starttime
FROM
	cd.bookings bks
	INNER JOIN cd.members mems ON mems.memid = bks.memid
WHERE
	starttime >= '2012-09-01'
GROUP BY
	mems.surname,
	mems.firstname,
	mems.memid
ORDER BY
	mems.memid;


SELECT
	(
		SELECT
			count(*)
		FROM
			cd.members) AS count,
	firstname,
	surname
FROM
	cd.members
ORDER BY
	joindate;


SELECT
	row_number() OVER (ORDER BY joindate),
	firstname,
	surname
FROM
	cd.members
ORDER BY
	joindate;


SELECT
	facid,
	sum(slots) AS totalslots
FROM
	cd.bookings
GROUP BY
	facid
HAVING
	sum(slots) = (
		SELECT
			max(sum2.totalslots)
		FROM (
			SELECT
				sum(slots) AS totalslots
			FROM
				cd.bookings
			GROUP BY
				facid) AS sum2);


SELECT
	surname || ', ' || firstname AS name
FROM
	cd.members;


SELECT
	memid,
	telephone
FROM
	cd.members
WHERE
	telephone SIMILAR TO '%[()]%';


SELECT
	substr(mems.surname, 1, 1) AS letter,
	count(*) AS count
FROM
	cd.members mems
GROUP BY
	letter
ORDER BY
	letter;