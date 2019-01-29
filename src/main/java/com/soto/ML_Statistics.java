package com.soto;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.DoubleFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;
import org.apache.spark.mllib.stat.Statistics;

import java.util.Arrays;

public class ML_Statistics {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("Statistics")
                .setMaster("local");

        JavaSparkContext jsc = new JavaSparkContext(conf);

        JavaRDD<String> lines = jsc.textFile("/home/sotowang/user/aur/ide/idea/idea-IU-182.3684.101/workspace/Spark_MLlib/src/resources/sample_stat.txt");

        JavaRDD<Vector> vectorRDD = lines.map(new Function<String, Vector>() {
            @Override
            public Vector call(String v1) throws Exception {
                String[] splited = v1.split("\t");
                return Vectors.dense(Double.valueOf(splited[0]), Double.valueOf(splited[1]), Double.valueOf(splited[2]), Double.valueOf(splited[3]), Double.valueOf(splited[4]));
            }

        });

        //计算每列最大值,最小值,平均值,方差值,L1范数,L2范数
        MultivariateStatisticalSummary summary = Statistics.colStats(vectorRDD.rdd());
        System.out.println(summary.max());
        System.out.println(summary.min());
        System.out.println(summary.mean());
        System.out.println(summary.variance());
        System.out.println(summary.normL1());
        System.out.println(summary.normL2());


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

        Double correlation = Statistics.corr(seriesX.srdd(), seriesY.srdd(), "pearson");

        System.out.println(pearsonMatrix);
        System.out.println("-----------------------------");
        System.out.println(spearmanMatrix);
        System.out.println("-----------------------------");

        System.out.println(correlation);


        jsc.close();
    }



}
