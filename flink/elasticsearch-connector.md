Flink Elasticsearch Connector
===

## Install Elasticsearch
Elasticsearch was installed on mac via Homebrew.
```
$ brew tap elastic/tap
$ brew install elastic/tap/elasticsearch-full
```

## Run Elasticsearch
Run Elasticsearch.
```
$ elasticsearch
```

I can confirm the follwoing URL if it works fine.
```
http://localhost:9200 
```

## Create Flink App

* Create `ElasticsearchSink` by `ElasticsearchSink.Builder`.
* Build and add the sink to the job.

```
public class ElasticsearchConnectorJob {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        List<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost("localhost", 9200, "http"));

        DataStream<String> input = env.fromElements(ElasticsearchInputSample.DATA);

        ElasticsearchSink.Builder<String> esSinkBuilder = new ElasticsearchSink.Builder<>(
                httpHosts,
                new ElasticsearchSinkFunction<String>() {
                    public IndexRequest createIndexRequest(String element) {
                        Map<String, Object> json = new HashMap<>();
                        try {
                            json = MAPPER.readValue(element, new TypeReference<Map<String, Object>>() {
                            });
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                        return Requests.indexRequest()
                                .index("my-index")
                                .source(json);
                    }

                    @Override
                    public void process(String element, RuntimeContext ctx, RequestIndexer indexer) {
                        indexer.add(createIndexRequest(element));
                    }
                }
        );

        esSinkBuilder.setBulkFlushMaxActions(1);
        input.addSink(esSinkBuilder.build());

        env.execute("ElasticsearchConnectorJob");
    }
}
```

##  Ran Flink job
This is input json.
```
{
      "name" : "wataru",
      "age" : 32,
      "subjects" : [
        "math",
        "english"
      ]
}
```

I confirmed that json data was indexed by `my-index` index.
```
$ curl http://localhost:9200/my-index/_doc/_search?pretty
{
  "took" : 64,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "my-index",
        "_type" : "_doc",
        "_id" : "tUOal3QByvzOPAXKhPAz",
        "_score" : 1.0,
        "_source" : {
          "name" : "wataru",
          "age" : 32,
          "subjects" : [
            "math",
            "english"
          ]
        }
      }
    ]
  }
}
```
