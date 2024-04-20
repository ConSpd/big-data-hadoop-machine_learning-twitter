# Big Data manipulation from Twitter posts using Hadoop

An App that uses a complete Hadoop pipeline to collects Tweets on any 3 keywords the user enters, store them, analyze them and provide conclusions using Mahout's
KMeans Machine Learning Algorithm.


### Collection & Storing of Data
The collection of Tweets is carried out by **Kafka**. After that a microprocessing takes place where Tweets without location 
are disposed, the ones that provide location are used to extract the Device, the Location and the Tweet that was shared.
Using **Avro** the Data is getting formated into binary which will be sent to **Flume** that will finally store the formated data to **HDFS**.

<img width="600" src="https://github.com/ConSpd/big-data-hadoop-machine_learning-twitter/assets/74179715/ea8b6db1-ae3f-473d-9a0e-3e83ed0f3c2f">

### Analyzation of Data & Preproccesing for ML processing
The App retrieves the Data from HDFS and applies **Map Reduce** on them that sums the commonalities between them and stores the results again on HDFS.
Using the sum we extract the percentages of Location, Device etc. Finally the Data is being transformed to arithmetic format so that the KMeans algorithm
can understand them.

<img width=600 src="https://github.com/ConSpd/big-data-hadoop-machine_learning-twitter/assets/74179715/66d933f1-2fbb-4ebf-a8bc-6b70f9d674f8">


### Mahout ML processing
`KmeansData.dat` file gets thrown as input to Mahout's KMeans algorithm. 

<img width="600" src="https://github.com/ConSpd/big-data-hadoop-machine_learning-twitter/assets/74179715/5e2619b1-3ebd-4e26-86a4-a7c0d26d7293">


## Examples of the Application
**Main Page**

<img width="300" alt="image" src="https://github.com/ConSpd/big-data-hadoop-machine_learning-twitter/assets/74179715/0cda7f2c-6f01-45d6-864e-2d7fa6befafc">

**Extracted Tweets**

<img width="600" alt="image" src="https://github.com/ConSpd/big-data-hadoop-machine_learning-twitter/assets/74179715/1cdb053d-9464-4249-a68c-68c6b78189f1">

**Statistics of Tweets and storing at HDFS**

<img width="150" alt="image" src="https://github.com/ConSpd/big-data-hadoop-machine_learning-twitter/assets/74179715/e3c470fc-1314-4532-a067-abe2657c4b0d">
<img width="600" alt="image" src="https://github.com/ConSpd/big-data-hadoop-machine_learning-twitter/assets/74179715/0354fd79-b6fa-4de5-a29e-9d804c06ee36">

**Mahout's KMeans Results**

<img width="300" alt="image" src="https://github.com/ConSpd/big-data-hadoop-machine_learning-twitter/assets/74179715/89adc14d-9f3f-4ba3-bebe-54940e308f0d">
