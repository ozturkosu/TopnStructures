# Summary Structures For The Frequent Items Problem
I was assigned to implement a count-min sketch extension of PostgreSQL for top-n queries during my internship at [Citus Data](https://www.citusdata.com/). [This extension](https://github.com/citusdata/cms_topn) had to provide mergeable summaries to find frequent items of data sets and union of them. At the beginning of my task, I realized there are different alternatives but we decided to follow the customer's request so we used count-min sketch structure to create summaries and simple list to track most frequent items in our implementation.

##Idea
After the internship, I came up with a hybrid idea of counter-based and sketch-based method for this problem or it can be seen as an improvement on general implementation of the count-min sketch for the frequent items problem. The idea is simply keeping counters for the frequent items like counter-based methods and the difference is using sketch if new arrival occurs to find associating count for this arriving item. For example, lets have 3 counters for the top-3 items:

First, we only use counters to keep track of the frequent items and three counters show that:

item a - 15, item b - 11, item c - 5 ( items and their frequencies)

If the counters are full with items, we use the sketch for new items like normal count-min sketch approach. For each new arrival, we update the counters if the item is there or the sketch and get the item's approximate frequency. If the item is not in the sketch and its approximate frequency is smaller than the minimum frequency in the counters which is 5, the decision is not to store this element in the counters.

In other case, if the item is not in the sketch and its approximate frequency is bigger than the minimum frequency in the counters, the new item and its approximate frequency is extracted from the sketch and this item is stored in the counters with its appoximate frequency. Also, the old minimum item is extracted from the counters and inserted to the sketch with its frequency.

##Goal
My aim with this method is to provide additional information to counter-based methods with the sketch. This information can be used for better policy of storing elements in the counter and their frequencies. Also, the new summary contains required information to merge datasets because the missing in the counter-based methods is the frequencies of rare elements.

In addition to the first aim, this method can decrease collision effect of the hashing in the sketch-based methods because the top items are counted in the counters and the collisions don't affect this counting. This especially seems important for the items which collide in most of the hash functions. Here, one additional parameter can be defined as a reliability parameter for the information from the sketch and update this parameter according to dirtiness of the sketch.

I created this repository for the implementation of three ideas which are SpaceSaving (a counter-based methods), Count-Min Sketch Top-n and Hybrid Top-n. I continuously update my codes for these ideas and tests of them for the data sets which have different distributions and their performance in terms of mergeability. After having enough number of tests, I also want to present the results graphically.
