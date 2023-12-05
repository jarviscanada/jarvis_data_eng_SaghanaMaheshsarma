## Introduction
The Linux cluster monitoring agent is focused on the efficient operation of a Linux cluster comprised of numerous nodes/servers running CentOS 7. The project's goal is to collect complete hardware characteristics for each node and provide real-time resource monitoring, including CPU and memory data. A reliable Relational Database Management System (RDBMS) stores the data, allowing the Linux cluster management team to provide analytical reports for resource allocation.

### Key Highlights
- **Functionality:** Recording hardware specifications, real-time resource usage monitoring.
- **Users:** LCA team for resource planning purposes.
- **Technologies:** Linux command lines, Bash scripts, PostgreSQL, Docker, Git.

## Quick Start

```bash
# Initialize the psql instance using psql_docker.sh
./scripts/psql_docker.sh start

# Set up tables with ddl.sql
psql -h localhost -U postgres -d host_agent -a -f sql/ddl.sql

# Inject hardware specs data into the DB using host_info.sh
./scripts/host_info.sh localhost 5432 host_agent postgres password

# Inject hardware usage data into the DB using host_usage.sh
./scripts/host_usage.sh localhost 5432 host_agent postgres password

# Configure the crontab
crontab -e

```

## Implementation

### Architecture

![Linux Clustering][https://github.com/jarviscanada/jarvis_data_eng_SaghanaMaheshsarma/tree/develop/linux_sql/assets/LinuxClustering.jpg]
- A PostgreSQL instance persists all data.
- The bash agent collects server usage data and inserts it into the PostgreSQL instance.
  - host_info.sh acquires hardware info and runs once at installation.
  - host_usage.sh captures current usage data, scheduled at intervals using cron.

### Database and Table Setup

- `host_info` Table

The `host_info` table captures the hardware specifications of each node:

<table>
  <thead>
    <tr>
      <th>Column</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>id</td>
      <td>Unique host identifier</td>
    </tr>
    <tr>
      <td>hostname</td>
      <td>Server hostname</td>
    </tr>
    <tr>
      <td>cpu_number</td>
      <td>Number of CPUs</td>
    </tr>
    <tr>
      <td>cpu_arch</td>
      <td>CPU architecture</td>
    </tr>
    <tr>
      <td>cpu_model</td>
      <td>CPU model</td>
    </tr>
    <tr>
      <td>cpu_mhz</td>
      <td>CPU speed (in MHz)</td>
    </tr>
    <tr>
      <td>L2_cache</td>
      <td>L2 cache size (in KB)</td>
    </tr>
    <tr>
      <td>total_mem</td>
      <td>Total memory (in MB)</td>
    </tr>
    <tr>
      <td>timestamp</td>
      <td>Timestamp of insertion</td>
    </tr>
  </tbody>
</table>

- `host_usage` Table

The `host_usage` table records real-time resource usage metrics:

<table>
  <thead>
    <tr>
      <th>Column</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>timestamp</td>
      <td>Timestamp of insertion</td>
    </tr>
    <tr>
      <td>host_id</td>
      <td>Foreign key to host_info</td>
    </tr>
    <tr>
      <td>memory_free</td>
      <td>Free memory (in MB)</td>
    </tr>
    <tr>
      <td>cpu_idle</td>
      <td>CPU idle percentage</td>
    </tr>
    <tr>
      <td>cpu_kernel</td>
      <td>CPU kernel percentage</td>
    </tr>
    <tr>
      <td>disk_io</td>
      <td>Disk I/O (in MB/s)</td>
    </tr>
    <tr>
      <td>disk_available</td>
      <td>Available disk space (in MB)</td>
    </tr>
  </tbody>
</table>

This structured schema enables efficient organization and retrieval of data.

The project utilizes a PostgreSQL database to store and manage the collected data. The initial database setup involves executing the ddl.sql file which sets up the necessary tables, including `host_info` and `host_usage`, to capture hardware specifications and real-time resource usage, respectively.

## Scripts Description And Usage

- psql_docker.sh

This script initiates a PostgreSQL Docker container. Simply execute the following command:

```bash

./scripts/psql_docker.sh

```

- host_info.sh

The `host_info.sh` script is responsible for gathering detailed hardware information for a specific host and inserting it into the database. It takes the following parameters:

```bash

./scripts/host_info.sh <db_host> <db_port> <db_name> <db_user> <db_password>

```


- host_usage.sh

The `host_usage.sh` script captures the current usage metrics (CPU, memory) for a host and inserts the data into the database. It is designed to be triggered at regular intervals using `cron`. The usage command is as follows:

```bash

./scripts/host_usage.sh <db_host> <db_port> <db_name> <db_user> <db_password>

```
- crontab

To schedule the host_usage.sh script at regular intervals, add the following line to your crontab:

```
# Example crontab setup to run host_usage.sh every minute
* * * * * /path/to/host_usage.sh <db_host> <db_port> <db_name> <db_user> <db_password> 

```
Replace all the above placeholders with respective values.

## Test
The bash scripts and DDL were tested manually. The scripts were tested on a single machine, ensuring functionality. The accuracy of SQL scripts was validated using test data.

## Deployment
Cron is used to schedule the execution of agent scripts. The source code is managed on Github, while Docker is employed for setting up the database.

## Improvements
Handle hardware updates dynamically.
Implement secure communication between agents and the database.
Enhance error handling and logging in bash scripts.

