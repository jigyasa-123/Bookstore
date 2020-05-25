#!/bin/bash

# Run the server and send it to the background
/entrypoint.sh couchbase-server &
# Check if couchbase server is up
check_db() {
curl --silent http://127.0.0.1:8091/pools > /dev/null
echo $?
}
# Variable used in echo
i=1
# Echo with
log() {
echo "[$i] [$(date +"%T")] $@"
i=`expr $i + 1`
}
# Wait until it's ready
until [[ $(check_db) = 0 ]]; do
>&2 log "Waiting for Couchbase Server to be available ..."
sleep 1
done
# Setup index and memory quota
log "$(date +"%T") Init cluster ........."
couchbase-cli cluster-init -c 127.0.0.1 --cluster-username admin --cluster-password hello.123 \
--cluster-name myCluster --cluster-ramsize 1024 --cluster-index-ramsize 512 --services data,index,query,fts \
--index-storage-setting default
# Create the buckets
log "$(date +"%T") Create buckets ........."
couchbase-cli bucket-create -c 127.0.0.1 --username admin --password hello.123 --bucket-type couchbase --bucket-ramsize 250 --bucket books

# Need to wait until query service is ready to process N1QL queries
log "$(date +"%T") Waiting ........."
sleep 20
# Create bookstore indexes
echo "$(date +"%T") Create books indexes ........."
cbq -u admin -p hello.123 -s "CREATE PRIMARY INDEX idx_primary ON \`books\`;"
tail -f /dev/null

