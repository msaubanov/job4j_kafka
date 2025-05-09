# block until kafka is reachable
kafka-topics --bootstrap-server kafka-broker-1:9092 --list
kafka-topics --bootstrap-server kafka-broker-2:9093,kafka-broker-3:9094 --list
echo -e 'Deleting kafka topics'
kafka-topics --bootstrap-server kafka-broker-1:9092 --topic tasks_job4j --delete --if-exists
kafka-topics --bootstrap-server kafka-broker-1:9092,kafka-broker-2:9093,kafka-broker-3:9094 --topic message-events --delete --if-exists

echo -e 'Creating kafka topics'
kafka-topics --bootstrap-server kafka-broker-1:9092 --create --if-not-exists --topic tasks_job4j --replication-factor 1 --partitions 3
kafka-topics --bootstrap-server kafka-broker-1:9092,kafka-broker-2:9093,kafka-broker-3:9094 --create --if-not-exists --topic message-events --replication-factor 3 --partitions 3

echo -e 'Successfully created the following topics:'
kafka-topics --bootstrap-server kafka-broker-1:9092 --list
kafka-topics --bootstrap-server kafka-broker-2:9093,kafka-broker-3:9094 --list