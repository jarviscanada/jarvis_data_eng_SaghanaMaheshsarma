#!/bin/bash
# Check the number of arguments
if [ "$#" -lt 8 ];then
  echo "Illegal number of arguments"
  exit 1
fi

# setup and validate the arguments
apiKey="$1"
psql_host="$2"
psql_port="$3"
db_name="$4"
psql_user="$5"
psql_password="$6"

shift 6

# function for API request and get global quote
get_quote(){
  local symbol="$1"
  local apiUrl="https://alpha-vantage.p.rapidapi.com/query"
  local endpoint="GLOBAL_QUOTE"
  local response=$(curl -s "$apiUrl" \
          --request GET \
          --url "$apiUrl?function=$endpoint&symbol=$symbol&datatype=json" \
          --header "X-RapidAPI-Host: alpha-vantage.p.rapidapi.com" \
          --header "X-RapidAPI-Key: $apiKey")

  # check for response from the API
  if [[ $response == *"Global Quote"* ]];then
    echo "$response"
    return 0
  else
    echo "Error:Unable to get valid response for $symbol"
    return 1
  fi
}

# Loop through symbols and fetch data
for symbol in "$@"; do
    quote_data=$(get_quote "$symbol")

    echo "Symbol:$symbol"
    echo "API Response: $quote_data"
    # Check for valid response from the API
    if [[ $quote_data == *"Global Quote"* ]]; then
        # Extract relevant data from the API response
        symbol=$(echo "$quote_data" | jq -r '.["Global Quote"]["01. symbol"] | sub("^ "; ""; "g")')
        open=$(echo "$quote_data" | jq -r '.["Global Quote"]["02. open"] | sub("^ "; ""; "g")')
        high=$(echo "$quote_data" | jq -r '.["Global Quote"]["03. high"] | sub("^ "; ""; "g")')
        low=$(echo "$quote_data" | jq -r '.["Global Quote"]["04. low"] | sub("^ "; ""; "g")')
        price=$(echo "$quote_data" | jq -r '.["Global Quote"]["05. price"] | sub("^ "; ""; "g")')
        volume=$(echo "$quote_data" | jq -r '.["Global Quote"]["06. volume"] | sub("^ "; ""; "g")')

        echo $symbol
        echo $open
        echo $high
        echo $low
        echo $price
        echo $volume
        # Insert data into PostgreSQL database
        insert_statement="INSERT INTO quotes(symbol,open,high,low,price,volume) VALUES('$symbol','$open','$high','$low','$price','$volume');"

        echo $insert_statement
        export PGPASSWORD=$psql_password

        psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_statement"
    else
        echo "Skipping symbol $symbol due to API error."
    fi
done

echo "Data insertion completed."


