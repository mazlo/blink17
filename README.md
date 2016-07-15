# Prerequisites

Stop all processes that are not relevant for the system to run, e.g.

- Dropbox (`kill pid`) or Browsers
- tracker (`tracker-control -t`)
- mysql (`sudo service mysqld stop`), postgresql (`pgctl main stop`)

Make sure the system that you want to evaluate is running.

```bash
cd disco-evaluation
```

## System Configuration

Edit property-file to match your configuration.

```bash
vim application.properties
```

- The `evaluate`-property is a label for the system you are evaluating. It is also used in other properties. This is the property to change when you go from evaluating one system to evaluating another.

- Customize all properties starting with `server.db`. If you are evaluating sesame or neo4j, just leave the property `server.db.X.driverClass` empty.

## Queries

Per default, the queries are placed in a folder like `queries-X`, where `X` is the system you want to evaluate, e.g. `queries-sesame`. This is stated by the property `queries.folder`.

## Distributions

There are two scenarios: either there is a file in folder `queries/equalDistribution.txt` or `queries/probabilityDistribution.txt` or not. In case there is a file this file is used as the execution sequence of queries. If there is no file the sequence is computed according to the configuration given in the property file.

- Property value for `queries.distribution` says something about the distribution of the queries in the bucket. Possible values are `equalDistribution` and `probabilityDistribution`. This value will instantiate the corresponding evaluator AND will give the program a hint where to place or find the distributions of your queries.

When you first run the program it looks for a file called `${queries.distribution}.txt` in the folder `project-root/queries`.
If it is not there, it will be created according to the value of the property `queries.distribution`.

If you already have a specific distribution in mind, please save the queries on a per line basis in this folder
`project-root/queries`.

### Equal Distribution

- `queries.multiplier` is the value by which all queries will be multiplied. For instance, if you have 40 queries and a multiplier value of 25, you will get 1000 queries in total. 

The resulting bucket queries will then be shuffled.

### Probability Distribution

In probability distribution there is no multiplier, instead you will have to give a total number of queries and the according query distributions.

- `queries.total` is the total size of the bucket. 

- `queries.probabilities` assigns each query a probability value, which states the likelyhood of the query to be executed. The 
number of probability values has to be equal to the number of queries available.

# Start Evaluation

To start the evaluation just use the following java command 

```bash
java -jar disco-evaluation.jar
```

Results are written into the file specified in property `statistics.output.filename`.
