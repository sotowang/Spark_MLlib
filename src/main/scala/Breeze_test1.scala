import breeze.linalg.{DenseMatrix, DenseVector, diag}
import breeze.numerics._

object Breeze_test1 {
  def main(args: Array[String]): Unit = {

    //全0矩阵
    val m1 = DenseMatrix.zeros[Double](2, 3)
    println(m1)

    //全0向量
    val testVector: DenseVector[Double] = DenseVector.zeros[Double](2)
    println(testVector)

    //全1向量
    val v1 = DenseVector.ones[Double](2)
    println(v1)

    //按数值填充向量
    val haveNumberFill = DenseVector.fill[Double](4, 2)
    println(haveNumberFill)


    println("----" * 10)


    //生成随机向量
    val rangeNUm = DenseVector.range(1, 10, 2)
    //DenseVector(1, 3, 5, 7, 9)
    val rangeNUmD = DenseVector.rangeD(1, 9, 2)
    //DenseVector(1.0, 3.0, 5.0, 7.0)
    val rangeNUmF = DenseVector.rangeF(1, 7, 2) //DenseVector(1.0, 3.0, 5.0)
    println(rangeNUm)
    println(rangeNUmD)
    println(rangeNUmF)


    println("----" * 10)


    //单位矩阵
    val unitMatrix = DenseMatrix.eye[Double](4)
    println(unitMatrix)

    println("----" * 10)


    //对角矩阵
    val doubleVecoter = diag(DenseVector(3.0, 4.0, 5.0))
    println(doubleVecoter)


    println("----" * 10)


    //按照行创建矩阵
    val byRowCreateMatrix = DenseMatrix((4.0, 5.0, 6.0), (7.0, 8.0, 9.0))
    println(byRowCreateMatrix)

    println("----" * 10)


    //按照行创建向量
    val denseCreateVector = DenseVector((4.0, 5.0, 6.0, 7.0, 8.0, 9.0))
    println(denseCreateVector)

    println("----" * 10)


    //向量转置
    val vectorTranspostion = DenseVector((4.0, 5.0, 6.0, 7.0, 8.0, 9.0)).t
    println(vectorTranspostion)

    println("----" * 10)

    //从函数创建向量
    val funCreateVector = DenseVector.tabulate(5)(i => i * i)
    println(funCreateVector) //DenseVector(0, 1, 4, 9, 16)
    println("----" * 10)

    //从函数创建矩阵
    val createFuncMatrix= DenseMatrix.tabulate(3, 4) {
      case (i ,j ) => i*i + j*j
    }
    println(createFuncMatrix)
    println("----" * 10)

    //从数组创建矩阵
    val createFunctionMatrix = new DenseMatrix[Double](3, 2, Array(1.0, 4.0, 7.0, 3.0, 6.0, 9.0))
    println(createFunctionMatrix)

    println("----" * 10)





  }
}
