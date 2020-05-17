#!/usr/bin/env bash

readonly region='us-east-1'

clear_delta_table() {
    delta_table_namespace="$1"
    delta_table_name="$2"

    delta_primary="${namespace}_pk"
    delta_secondary="${namespace}_sk"

    aws --region "$region" dynamodb scan \
        --table-name "$delta_table_name" \
        --attributes-to-get "$delta_primary" \
        --query "Items[].$delta_primary.S" --output text | \
        tr "\t" "\n" | \
            xargs -t -I keyvalue \
                aws --region "$region" dynamodb delete-item \
                    --table-name "$delta_table_name" \
                    --key "{\"$delta_primary\": {\"S\": \"keyvalue\"}, \"$delta_secondary\": {\"S\": \"keyvalue\"}}"
}

function clear_regular_table() {
    regular_table_name="$1"
    regular_primary='id'

    aws --region "$region" dynamodb scan \
        --table-name "$regular_table_name" \
        --attributes-to-get "$regular_primary" \
        --query "Items[].$regular_primary.S" --output text | \
        tr "\t" "\n" | \
            xargs -t -I keyvalue \
                aws --region "$region" dynamodb delete-item \
                    --table-name "$regular_table_name" \
                    --key "{\"$regular_primary\": {\"S\": \"keyvalue\"}}"
}


clear_delta_table 'ds' 'dbname'
clear_regular_table 'dbname'

