
#setup and validate arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check the number of arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# Save machine statistics in MB and current machine hostname to variables
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

# Get hardware specifications and store it in a variable
cpu_number=$(lscpu | awk '/^On-line/ {print $4}')
cpu_architecture=$(lscpu | awk '/Architecture/ {print $2}')
cpu_model=$(lscpu | awk -F': ' '/Model name/ {print $2}')
cpu_mhz=$(lscpu | awk '/MHz/ {print $3}')
# Retrieve L2 cache size in kilobytes
l2_cache=$(lscpu | awk '/L2 cache/ {print $3}' | sed 's/K//')

# Retrieve total memory in kilobytes
total_mem=$(vmstat -s | awk '/total memory/ {print $1}')

# Get current time in `yyyy-mm-dd hh:mm:ss`format
timestamp=$(vmstat -t | awk 'NR==3{printf "%s %s", $18, $19}' | xargs)

# Insert to host info
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, \"timestamp\", total_mem) VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp', $total_mem);"

#set up env var for pql cmd
export PGPASSWORD=$psql_password
#Insert date into a database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?