# 推荐系统设计

* 需求分析与用户调研

用户 

 ```markdown
 新用户-> 兴趣未知--->着重促销,多样性,新颖性
 老用户-> 兴趣书籍--->着重个性化
 主流用户->
 小众群体
 ```

推荐什么

```markdown
1. 价格一致,用户经常购买的类别    
    书,电影,音乐,文章-->以用户内容主题的兴趣为主
2. 价格不一致,用户经常购买的类别
    服饰,日常百货-->视觉,品牌,价格,内容
3. 用户很少购买的类别
    房,车,装修材料,家具-->专家推荐,互补推荐
4. 新品促销/库存清理-->考虑整体系统的获利
```

何时

```markdown
1. Email VS 手机短信
2. 短期,长期,周期(节假日)

```

* 功能设计

个性化首页

```markdown
个性化促销,关注信息推送
```

Item页面

```markdown
1. 关联商品
2. 基于浏览历史的推荐
3. 基于购买历史的推荐

```

User页面

购物车页面

Community页面

```markdown
其他:收集,email
```


* 界面设计

如何将结果呈现给用户?

如何收集用户信息和反馈数据

目的:

```markdown
1. 提高用户满意度,达到推荐目的
2. 更多更好地收集高质量的用户反馈
3, 准确评测推荐算法的效果

```

* 架构设计

```markdown
1. 硬件资源的限制
2. 用户数,item数
3. 存储,接口
4. 实时响应的要求
```


* 算法设计

```markdown
1. 优化准则
2. 数据预处理
3. 离线算法
4. 在线算法
5. 功能实现策略
6. 推荐解释
```

* 系统评测

```markdown
1. 用户反馈
    点击率,转换率,单次使用时长,重复使用率
2.A/B测试

```

---

### 用户界面的重要性

### 用户数据采集的重要性


---

# 大数据Lambda架构

[技术|深入浅出解析大数据Lambda架构](https://ask.hellobi.com/blog/transwarp/5107)

Lambda架构整合离线计算和实时计算，融合不可变（Immutability，读写分离和隔离 一系列构原则，可集成Hadoop，Kafka，Storm，Spark,HBase等各类大数据组件。

Lambda架构的主要思想就是将大数据系统构建为多个层次，三层架构：

```
批处理层、
实时处理层、
服务层 ，
```
如下图

![](https://ask.hellobi.com/uploads/article/20161011/da1d48c86510a64e8092cade6cfbf2f7.png)

```
批处理层：批量处理数据，生成离线结果
实时处理层：实时处理在线数据，生成增量结果
服务层：结合离线、在线计算结果，推送上层

```

* a. Batch Layer  

> 既然对全体数据集进行在线查询，计算代价会很高，那么如果对查询事先进行预计算，生成对应的Views，并且对Views建立索引，这样，查询的速度会提高很多，这就是Batch Layer所做的事。

>Batch Layer层采用不可变模型对所有数据进行了存储，并且根据不同的业务需求，对数据进行了不同的预查询，生成对应的Batch Views，
这些Batch Views提供给上层的Serving Layer进行进一步的查询。另外，每隔一段时间都会进行一次预查询，对Batch Views进行更新，Batch Views更新完成后，会立即更新到Serving Layer中去。

预查询的过程是一个批处理的过程，因此，这一层可以选择诸如Hadoop这样的组件。
Batch Layer层的结构图如图2所示：

![](https://ask.hellobi.com/uploads/article/20161011/8bc9da97a005124a7a837db631274f1f.png)

* b. Speed Layer

如上一节中提到，预查询的过程是一个批处理的过程，该过程花费的时间会比较长，在该过程中，Serving Layer使用的仍然是旧版本的Batch Views，
那么仅仅依靠Batch Layer这一层，新进入系统的数据将无法参与最后结果的计算，因此，Marz为Lambda设计了Speed Layer层来处理增量的实时数据。

Speed Layer和Batch Layer比较类似，对数据计算生成Realtime Views，其主要的区别是：

```markdown
第一，Speed Layer处理的数据是最近的增量数据流，Batch Layer处理的是全体数据集。

第二，Speed Layer为了效率，接收到新数据时，就更新Realtime Views，并且采用的是Incremental Updates（增量计算模型），
而Batch Layer则是根据全体离线数据集得到Batch Views，采用的是Recomputation Updates（重新计算模型）。

```

* c. Serving Layer
  
Serving Layer用于响应用户的查询请求，它将Batch Views和Realtime Views的结果进行了合并，得到最后的结果，返回给用户，图3给出了Lambda的整体架构图：

![](https://ask.hellobi.com/uploads/article/20161011/23d51f9a6147f18ef28d6a4730f77979.png)

概括起来，Lambda架构通过Batch Layer和Speed Layer的分层设计来实现在一个系统内同时支持实时和批处理业务，
并且通过Serving Layer在逻辑上统一了两种数据源的接口，让应用能够以一个统一的数据视图来开发和部署，从而达到数据和应用的融合。

在每个Layer的实际设计中，开发人员可以根据自身的需求来选择合适的组件或者产品来构建相应的系统，
目前有很多开源组件可以用于构建此类系统，如Storm/Spark Streaming/Flink可以用来构建Speed Layer，
Spark/MapReduce可以用于构建Batch Layer，HBase/Redis/MongoDB可以用于存储。

由于一套系统需要同时处理实时业务和批处理业务，并且两批业务之间有比较明确的数据耦合，
Lambda系统本身的技术复杂度非常高，选择方案的时候需要充分考虑系统构建成本以及稳定性。

---

# 用户画像系统




























