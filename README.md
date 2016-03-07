# Prerequisites

Make sure you stop all processes that are not relevant for the system to run, e.g.

- Dropbox (`kill pid`) or Browsers

- tracker (`tracker-control -t`)

- mysql (`sudo service mysqld stop`), postgresql (`pgctl main stop`)

# Prepare Evaluation

- Make sure the system that you want to evaluate is running.

- cd to the root of the project.

## System Configuration

Edit the `application.properties` file to match your configuration.

- The `evaluate`-property is a label for the system you are evaluating. It is also used in other properties. This is the 
property to change when you go from evaluating one system to evaluating another.

- Customize all properties starting with `server.db`. If you are evaluating sesame or neo4j, just leave the property 
`server.db.X.driverClass` empty.

## Queries

Per default, the queries are placed in a folder like `queries-X` (stated by property `queries.folder`), where `X` is the system 
you want to evaluate.

## Distributions

Each query will _not_ be executed subsequently a specific number of times. Rather all queries are spread across a bucket of 
specific size. They are spread according to a distribution you specify.

- `queries.total` is the total size of the bucket. In order to execute each query the same number of times, this value should 
be a multiplier of the number of all queries, e.g 35 queries x 20 executions = 700 bucket size.

- `queries.distribution` says something about the distribution of the queries in the bucket. Possible values are 
`equalDistribution` and `probabilityDistribution`. This value will instantiate the corresponding evaluator AND will give the 
program a hint where to place or find the distributions of your queries.

When you first run the program it looks for a file called `${queries.distribution}.txt` in the folder `project-root/queries`. 
If it is not there, it will be created according to the value of the property `queries.distribution`.

If you already have a specific distribution in mind, please save the queries on a per line basis in this folder 
`project-root/queries`.

- `queries.probabilities` assigns each query a probability value, which states the likelyhood of the query to be executed. The 
number of probability values has to be equal to the number of queries available.

# Start Evaluation

To start the evaluation just use the java command `java -jar disco-evaluation.jar`. Results are written into the file specified 
in property `statistics.output.filename`.
