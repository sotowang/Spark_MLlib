import breeze.linalg.sum
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 线性回归
  */
object ML_Regression {
  def main(args: Array[String]) {

    val conf=new SparkConf().setAppName("regression").setMaster("local[1]")
    val sc=new SparkContext(conf)

    val records=sc.textFile("/home/sotowang/user/aur/ide/idea/idea-IU-182.3684.101/workspace/Spark_MLlib/src/resources/hour.csv").map(_.split(",")).cache()


    val mappings=for(i<-Range(2,10))yield get_mapping(records,i)

    val cat_len=sum(mappings.map(_.size))
    val num_len=records.first().slice(10,14).size
    val total_len=cat_len+num_len

    //linear regression data 此部分代码最重要，主要用于产生训练数据集，按照前文所述处理类别特征和实数特征。
    val data=records.map{record=>

      val cat_vec=Array.ofDim[Double](cat_len)
      var i=0
      var step=0
      for(filed<-record.slice(2,10))
      {

        val m=mappings(i)
        val idx=m(filed)
        cat_vec(idx.toInt+step)=1.0
        i=i+1
        step=step+m.size
      }

      val num_vec=record.slice(10,14).map(x=>x.toDouble)

      val features=cat_vec++num_vec
      val label=record(record.size-1).toInt


      LabeledPoint(label,Vectors.dense(features))
    }

    // val categoricalFeaturesInfo = Map[Int, Int]()
    //val linear_model=DecisionTree.trainRegressor(data,categoricalFeaturesInfo,"variance",5,32)
    val linear_model=LinearRegressionWithSGD.train(data,10,0.1)
    val true_vs_predicted=data.map(p=>(p.label,linear_model.predict(p.features)))
    //输出前五个真实值与预测值
    println( true_vs_predicted.take(5).toVector.toString())
  }

  def get_mapping(rdd:RDD[Array[String]], idx:Int)=
  {
    rdd.map(filed=>filed(idx)).distinct().zipWithIndex().collectAsMap()
  }

}