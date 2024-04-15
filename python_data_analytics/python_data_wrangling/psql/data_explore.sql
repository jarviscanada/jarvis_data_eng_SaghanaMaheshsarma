-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
select count(*) from retail;

-- number of clients (e.g. unique client ID)
select count(distinct customer_id) from retail;

--invoice date range (e.g. max/min dates)
select min(invoice_date), max(invoice_date) from retail;

--number of SKU/merchants (e.g. unique stock code)
select count(distinct stock_code) from retail;

--Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
select avg(total_amount) from (select invoice_no,SUM(unit_price * quantity) as total_amount from retail group by invoice_no having SUM(unit_price * quantity)>0;);

-- Calculate total revenue (e.g. sum of unit_price * quantity)
select SUM(unit_price * quantity) as total_amount from retail;

--Calculate total revenue by YYYYMM
select EXTRACT(year from invoice_date) * 100 + extract(month from invoice_date) as yymm, sum(unit_price * quantity) as total_revenue from retail group by yymm order by yymm asc;