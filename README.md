# Spark MLlib基础

参考资料: <<SPARK MLLIB机器学习_黄美灵>>

Spark MLlib底层的向量,矩阵运算使用了Breeze库,Breeze库提供了Vector/Matrix的实现以及相应计算的接口(Linalg),但是在MLlib里面同时也
提供了Vector和Linalg等的实现

## Breeze库API总结(Spark线性代数库)

[Breeze库API总结(Spark线性代数库)](https://blog.csdn.net/u012102306/article/details/53463388)

[Spark MLlib之使用Breeze操作矩阵向量](https://www.cnblogs.com/nucdy/p/7988959.html)

### Breeze创建函数:	 	Breeze_test1.scala
 
![深度截图_选择区域_20190130153100.png](https://i.loli.net/2019/01/30/5c5152c15173b.png)

 
### Breeze元素访问

![深度截图_选择区域_20190130153145.png](https://i.loli.net/2019/01/30/5c5152fea23a5.png)


### Breeze元素操作

![深度截图_选择区域_20190130153311.png](https://i.loli.net/2019/01/30/5c5153437900a.png)

### Breeze数组计算函数

![深度截图_选择区域_20190130153346.png](https://i.loli.net/2019/01/30/5c51538b2c636.png)
    

### Breeze求和函数


![深度截图_选择区域_20190130153524.png](https://i.loli.net/2019/01/30/5c5153c272068.png)


### Breeze 布尔函数

![深度截图_选择区域_20190130153615.png](https://i.loli.net/2019/01/30/5c5153f8d2ed3.png)

### Breeze 线性代数函数


![深度截图_选择区域_20190130153714.png](https://i.loli.net/2019/01/30/5c5154327b825.png)


### Breeze 取整函数

![深度截图_选择区域_20190130153823.png](https://i.loli.net/2019/01/30/5c51548a3a30f.png)

## BLAS介绍

![深度截图_选择区域_20190130163244.png](https://i.loli.net/2019/01/30/5c51617b5dbf3.png)
![深度截图_选择区域_20190130163256.png](https://i.loli.net/2019/01/30/5c51617be2326.png)
![深度截图_选择区域_20190130163302.png](https://i.loli.net/2019/01/30/5c51617c0beae.png)
![深度截图_选择区域_20190130163317.png](https://i.loli.net/2019/01/30/5c51617c5c69b.png)

## 机器学习类别:

* 监督学习
    
输入数据被称为训练数据,有已知的标签或结果,比如垃圾邮件/非垃圾邮件或者某段时间的股票价格.

模型参数的确定需要一个训练过程

常见的监督学习算法包括: 回归分析和统计分类

* 无监督学习

输入数据不带标签或没有一个已知结果,通过推测输入数据中存在的结构来建立模型
    
常见算法: 聚类

* 半监督学习

输入数据由带标签和不带标签组成,合适的预测模型虽已存在,但模型在预测的同时还必须通过发现潜在的结构来组织数据,这类问题包括分类和回归


* 强化学习

例如,和纽约州和机器人控制

常见算法:Q学习,时序差分学习

## 常见机器学习算法:

```markdown

分类与回归: 线性回归,逻辑回归,贝叶斯分类,决策树分类
聚类: KMeans聚类,LDA主题,KNN
关联规则: Apriori,FPGrowth
推荐:协同过滤,ALS
神经网络:BP,RBF,SVM
深度神经网络等算法
```

## MLlib Statistic统计操作 ML_Statistics.java

MLlib Statistics是基础统计模块,是对RDD格式数据进行统计,
包括:

```markdown

汇总统计,相关系数,分层抽样,假设检验,随机数据生成等 
```

### 列统计汇总(Summary statistics )

[Data Types - MLlib](http://spark.apache.org/docs/1.6.0/mllib-data-types.html#data-types-mllib )

Statistics的colStats函数是列统计方法,该方法可以计算每列最大值,最小值,平均值,方差值,L1范数,L2范数

[L1，L2范数](https://www.jianshu.com/p/73748dc4dea1)

> L1范数是指向量中各个元素的绝对值之和

> L2范数是指向量各元素的平方和然后开方

总结：

```
L1范式趋向于产生较少特征，在特征选择时很有用；

L2会选择更多特征，但对应权值会接近0

```

#### Local vector

> A local vector has integer-typed and 0-based indices and double-typed values, 
stored on a single machine. MLlib supports two types of local vectors: dense and sparse.
 A dense vector is backed by a double array representing its entry values, 
 while a sparse vector is backed by two parallel arrays: indices and values. 
 For example, a vector (1.0, 0.0, 3.0) can be represented in dense format as [1.0, 0.0, 3.0] or in sparse format as (3, [0, 2], [1.0, 3.0]), 
 where 3 is the size of the vector.

The base class of local vectors is Vector, and we provide two implementations: 
DenseVector and SparseVector. We recommend using the factory methods implemented in Vectors to create local vectors.


#### Stastics.colStats()方法


colStats() returns an instance of MultivariateStatisticalSummary, 
which contains the column-wise max, min, mean, variance, and number of nonzeros, as well as the total count.

```markdown
JavaRDD<String> lines = jsc.textFile("/home/sotowang/user/aur/ide/idea/idea-IU-182.3684.101/workspace/Spark_MLlib/src/resources/sample_stat.txt");

JavaRDD<Vector> vectorRDD = lines.map(new Function<String, Vector>() {
    @Override
    public Vector call(String v1) throws Exception {
        String[] splited = v1.split("\t");
        return Vectors.dense(Double.valueOf(splited[0]), Double.valueOf(splited[1]), Double.valueOf(splited[2]), Double.valueOf(splited[3]), Double.valueOf(splited[4]));
    }

});

MultivariateStatisticalSummary summary = Statistics.colStats(vectorRDD.rdd());
System.out.println(summary.max());
System.out.println(summary.min());
System.out.println(summary.mean());
System.out.println(summary.variance());
System.out.println(summary.normL1());
System.out.println(summary.normL2());
```

### 相关系数(Correlations)

>Calculating the correlation between two series of data is a common operation in Statistics. In spark.mllib we provide the flexibility to calculate pairwise correlations among many series. The supported correlation methods are currently Pearson’s and Spearman’s correlation.

Pearson 相关系数表达的是两个数值变量的线性相关性,它一般适用于正态分布,其聚会范围为[-1,1],取值为0表示不相关,取值为(0~-1]表示
正相关


Spearman相关系数也用来表达两个变量的收到性,但是它没有Pearson相关系数驿变量的分布要求那么严格,另外Spearman相关系数可以更好地用于测度变量的排序关系

```markdown
//计算Pearson系数,Spearman相关系数
Matrix pearsonMatrix = Statistics.corr(vectorRDD.rdd(), "pearson");
Matrix spearmanMatrix = Statistics.corr(vectorRDD.rdd(), "spearman");

JavaDoubleRDD seriesX = jsc.parallelize(Arrays.asList(1.0, 2.0, 3.0, 4.0)).mapToDouble(new DoubleFunction<Double>() {
    @Override
    public double call(Double aDouble) throws Exception {
        return aDouble;
    }
});
JavaDoubleRDD seriesY = jsc.parallelize(Arrays.asList(5.0, 6.0, 6.0, 6.0)).mapToDouble(new DoubleFunction<Double>() {
    @Override
    public double call(Double aDouble) throws Exception {
        return aDouble;
    }
});

JavaRDD<Double> a = jsc.parallelize(Arrays.asList(1.0, 2.0, 3.0, 4.0));
JavaRDD<Double> b = jsc.parallelize(Arrays.asList(5.0, 6.0, 6.0, 6.0));
Double correlation1 = Statistics.corr(a,b, "pearson");

Double correlation2 = Statistics.corr(seriesX.srdd(), seriesY.srdd(), "pearson");
```


### 假设检验

MLlib当前支持用于判断拟合度或者独立性的Pearson卡方检验.不同的输入类型决定了是做拟合度检验还是独立性检验.

拟合度检验要求输入为Vector,独立性检验要求输入为Matrix

```markdown
//卡方检验
Vector v1 = Vectors.dense(43.0, 9.0);
Vector v2 = Vectors.dense(44.0, 4.0);
ChiSqTestResult c1 = Statistics.chiSqTest(v1, v2);

System.out.println(c1.toString());
```

结果: 统计量为pearson,自由度为1,值为5.48,概率为0.019

```markdown
Chi squared test summary:
method: pearson
degrees of freedom = 1 
statistic = 5.482517482517483 
pValue = 0.01920757707591003 
Strong presumption against null hypothesis: observed follows the same distribution as expected..
```

## 线性回归算法

[MLlib回归算法（线性回归、决策树）实战演练--Spark学习（机器学习）](https://blog.csdn.net/u010824591/article/details/50374904)

想必初学机器学习的同学都是从这个算法开始的吧，其实就是给一些特征然后预测一个值例如预测房价、股票等。 

在spark的MLlib中回归算法与分类算法的用法其实相似，主要区别就是在LabeledPoint中的label不同，分类算法是具体的类别，而回归算法是一个不定的值。

### 数据源
本次回归算法用到的数据源为自行车租车数据，

下载地址为：

http://archive.ics.uci.edu/ml/datasets/Bike+Sharing+Dataset，        

主要用hour.csv数据。 

数据描述：此数据是根据一系列的特征预测每小时自行车租车次数。

数据中字段分别为： 

```markdown

instant：记录ID 
dteday：时间 


season：四季信息 
yr：年份（2011或2012） 
mnth：月份 
hr：当天时刻 
holiday：是否节假日 
weekday：周几 
workingday：当天是否工作日 
weathersit：表示天气类型的参数 


temp：气温 
atemp：体感温度 
hum：湿度 
windspeed：风速 


cnt：目标变量，每小时的自行车租车量 
```

一共15个字段，主要目的是预测最后一个字段的租车量。研究数据发现头两个字段对于预测结果没什么用处，所以这里不考虑。

### 特征提取

观察数据发现从第3到第10个字段是类型变量（类型变量就是取值一直就只有那么几种，例如星期几、天气状况等），

11到14字段是实数变量（实数变量就是一个无法确定的值）这里的值已经进行过归一化处理了，如果没归一化处理需先归一处理，可以提高准确度。

#### 类别特征

主要方法就是将类别数据转换为二元向量，就是用一个向量来表示类别。

举个例子就是如果水果有三类分别为苹果、香蕉、梨。一般在类别数据中记录的形式为1：苹果，2：香蕉，3：梨。
将其转换为二元向量之后就是001：苹果，010：香蕉，100：梨。如果不转换为二元特征算出来会有错误，本数据我试过如果不转换预测结果是负数。

#### 实数特征

实数特征一般就不用处理可以直接使用了，但是如前面提到的一般要归一处理

























----


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

[什么是用户画像呢？一般用户画像的作用是什么？ - 知乎](https://www.zhihu.com/question/19853605)

[基于大数据的用户画像构建（理论篇)](https://www.jianshu.com/p/0d77238771ef)

## 什么是用户画像？

简而言之，用户画像是根据用户社会属性、生活习惯和消费行为等信息而抽象出的一个标签化的用户模型。构建用户画像的核心工作即是给用户贴“标签”，而标签是通过对用户信息分析而来的高度精炼的特征标识。

> 举例来说，如果你经常购买一些玩偶玩具，那么电商网站即可根据玩具购买的情况替你打上标签“有孩子”，甚至还可以判断出你孩子大概的年龄，贴上“有 5-10 岁的孩子”这样更为具体的标签，
而这些所有给你贴的标签统在一次，就成了你的用户画像，因此，也可以说用户画像就是判断一个人是什么样的人。
  
除去“标签化”，用户画像还具有的特点是“低交叉率”，当两组画像除了权重较小的标签外其余标签几乎一致，那就可以将二者合并，弱化低权重标签的差异。         


## 用户画像的作用

罗振宇在《时间的朋友》跨年演讲上举了这样一个例子：

>当一个坏商家掌握了你的购买数据，他就可以根据你平常购买商品的偏好来决定是给你发正品还是假货以提高利润。
且不说是否存在这情况，但这也说明了利用用户画像可以做到“精准销售”，当然了，这是极其错误的用法。

其作用大体不离以下几个方面：

```markdown

1. 精准营销，分析产品潜在用户，针对特定群体利用短信邮件等方式进行营销；

2. 用户统计，比如中国大学购买书籍人数 TOP10，全国分城市奶爸指数；

3. 数据挖掘，构建智能推荐系统，利用关联规则计算，喜欢红酒的人通常喜欢什么运动品牌，利用聚类算法分析，喜欢红酒的人年龄段分布情况；

4. 进行效果评估，完善产品运营，提升服务质量，其实这也就相当于市场调研、用户调研，迅速下定位服务群体，提供高水平的服务；

5. 对服务或产品进行私人定制，即个性化的服务某类群体甚至每一位用户（个人认为这是目前的发展趋势，未来的消费主流）。比如，某公司想推出一款面向 5－10 岁儿童的玩具，通过用户画像进行分析，发现形象＝“喜羊羊”、价格区间＝“中等”的偏好比重最大，那么就给新产品提供类非常客观有效的决策依据。

6. 业务经营分析以及竞争分析，影响企业发展战略

```

![](https://upload-images.jianshu.io/upload_images/1402989-1ecddc2bc10bfd88.png?imageMogr2/auto-orient/)


## 数据收集

数据收集大致分为网络行为数据、服务内行为数据、用户内容偏好数据、用户交易数据这四类。

```markdown
网络行为数据：活跃人数、页面浏览量、访问时长、激活率、外部触点、社交数据等
服务内行为数据：浏览路径、页面停留时间、访问深度、唯一页面浏览次数等
用户内容便好数据：浏览／收藏内容、评论内容、互动内容、生活形态偏好、品牌偏好等
用户交易数据（交易类服务）：贡献率、客单价、连带率、回头率、流失率等

```
当然，收集到的数据不会是 100% 准确的，都具有不确定性，这就需要在后面的阶段中建模来再判断，
比如某用户在性别一栏填的男，但通过其行为偏好可判断其性别为“女”的概率为 80%。

还得一提的是，储存用户行为数据时最好同时储存下发生该行为的场景，以便更好地进行数据分析。
   
## 行为建模

该阶段是对上阶段收集到数据的处理，进行行为建模，以抽象出用户的标签，这个阶段注重的应是大概率事件，通过数学算法模型尽可能地排除用户的偶然行为。
这时也要用到机器学习，对用户的行为、偏好进行猜测，好比一个 y＝kx＋b 的算法，X 代表已知信息，Y 是用户偏好，通过不断的精确 k 和 b 来精确 Y。
在这个阶段，需要用到很多模型来给用户贴标签。

* 用户汽车模型

根据用户对“汽车”话题的关注或购买相关产品的情况来判断用户是否有车、是否准备买车

* 用户忠诚度模型

通过判断＋聚类算法判断用户的忠诚度

* 身高体型模型

根据用户购买服装鞋帽等用品判断

* 文艺青年模型

根据用户发言、评论等行为判断用户是否为文艺青年

* 用户价值模型

判断用户对于网站的价值，对于提高用户留存率非常有用（电商网站一般使用 RFM 实现）

还有消费能力、违约概率、流失概率等等诸多模型。
   
## 用户画像基本成型

该阶段可以说是二阶段的一个深入，要把用户的基本属性（年龄、性别、地域）、购买能力、行为特征、兴趣爱好、心理特征、社交网络大致地标签化。
为什么说是基本成型？
因为用户画像永远也无法 100％ 地描述一个人，只能做到不断地去逼近一个人，因此，用户画像既应根据变化的基础数据不断修正，
又要根据已知数据来抽象出新的标签使用户画像越来越立体。

关于“标签化”，一般采用多级标签、多级分类，

比如第一级标签是基本信息（姓名、性别），第二级是消费习惯、用户行为；
第一级分类有人口属性，人口属性又有基本信息、地理位置等二级分类，
地理位置又分工作地址和家庭地址的三级分类。


## 数据可视化分析

这是把用户画像真正利用起来的一步，在此步骤中一般是针对群体的分析，
比如可以根据用户价值来细分出核心用户、评估某一群体的潜在价值空间，以作出针对性的运营。

如图：
![](https://upload-images.jianshu.io/upload_images/1402989-d1eb46d6c9841c76.png?imageMogr2/auto-orient/)











