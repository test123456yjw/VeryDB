package StoreEngine.index.TreeIndex.BTree

import StoreEngine.index.page.Page
import StoreEngine.row.Row

/**
  * Created by jianwei.yang on 2017/6/5.
  */

class BtreeNode extends Btree {
  var storePage: Page = _


  def this(storePage: Page) {
    this()
    this.storePage = storePage
  }


  override def update(): Unit = ???

  override def isLeaf(page: Page): Boolean = ???

  override def remove(): Unit = ???

  def binary_serach(serarchKey: Int, allKeys: Seq[Int]): Int = {
    1
  }

  /**
    * 该方法在分裂的时候被调用
    * @param gonnaInsertKey
    * @param gonnaInsertPageRef                      
    * @return
    */
  def insert(gonnaInsertKey: Int, gonnaInsertPageRef: Int): Option[Page] = {
    //当前节点的keys和values
    val currentNodeKeys: Seq[Int] = storePage.keys
    val currentNodeValues: Seq[Int] = storePage.childs

    //待插入的key和index
    val insertIndex = binary_serach(gonnaInsertKey, currentNodeKeys)

    //以index为边界,将index的左右key分别存储到两个不同的临时数组中
    val leftKeys = currentNodeKeys.take(insertIndex)
    val rightKeys = currentNodeKeys.takeRight(insertIndex)

    //以index为边界,将index的左右key分别存储到两个不同的临时数组中
    val leftPageReference: Seq[Int] = currentNodeValues.take(insertIndex)
    val rightPageReference: Seq[Int] = currentNodeValues.takeRight(insertIndex)

    //将待插入的key和value插入到临时数组中,组成新节点的key和value
    val newCurrentNodeKeys: Seq[Int] = (leftKeys :+ gonnaInsertKey) ++ rightKeys
    val newCurrentNodeValues: Seq[Int] = (leftPageReference :+ gonnaInsertPageRef) ++ rightPageReference

    //插入完之后,判断是否需要分裂
    if (getSize(newCurrentNodeKeys, newCurrentNodeValues) > Page.pageSize) {
      val splitPage: Page = split()
      //new出新的节点
      return Some(splitPage)
    }
    return  None

  }


    /**
      * 获取该节点的size,在判断是否需要分裂的时候调用
      * @param keys
      * @param pageRef
      * @return
      */
    def getSize(keys: Seq[Int], pageRef: Seq[Int]): Int = {
    val size = keys.size * 4 + pageRef.size * 4 + Page.PageHeadSize
    size
  }


  def searchInsertPage(row:Row):Int={
    val gonnaInsertKey=row.key.get()
    //当前节点的keys和values
    val currentNodeKeys: Seq[Int] = storePage.keys
    val currentNodeValues: Seq[Int] = storePage.childs
    //待插入的key和index
    val insertIndex = binary_serach(gonnaInsertKey, currentNodeKeys)
    insertIndex
  }


  /**
    * 分裂叶节点
    */
  def split(): Page = {

    //确定切割点,默认中中间节点
    val currentNodeLength = storePage.keys.size / 2

    //获取切割节点的key和value
    val key = storePage.keys(currentNodeLength)
    val childPageRefs = storePage.childs(currentNodeLength)

    //该节点所有的key和value
    val keys = storePage.keys
    val pageRefs = storePage.childs

    //从切割点切割,左边key和右边key分别放到两个临时数组,切割点的key放在右边数组中
    var leftKeys: Seq[Int] = null
    System.arraycopy(keys, 0, leftKeys, 0, currentNodeLength)
    var rightKey: Seq[Int] = null
    System.arraycopy(keys, currentNodeLength, rightKey, 0, currentNodeLength)

    //从切割点切割,左边key和右边key分别放到两个临时数组,切割点的PageRef放在右边数组中
    var leftChildPageReference: Seq[Row] = null
    System.arraycopy(pageRefs, 0, leftChildPageReference, 0, currentNodeLength)
    var rightChildPageReference: Seq[Row] = null
    System.arraycopy(pageRefs, currentNodeLength, rightChildPageReference, 0, currentNodeLength)

    //右边的keys和values组成一个新的page,左边的keys和value还在原页中
    val newPage = new Page(1, 1,  rightKey, rightChildPageReference, null)

    newPage
  }


}

object BtreeNode{
  def apply()=new BtreeNode()
  def apply(storePage: Page): BtreeNode = new BtreeNode(storePage: Page)
}
