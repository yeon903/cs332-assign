import scala.annotation.tailrec

object Main {

  def main(args: Array[String]): Unit = {
    println(pascal(0,2)) // 1
    println(pascal(1,2)) // 2
    println(pascal(1,3)) // 3

    println(balance(":-)".toList)) // false
    println(balance("())(".toList)) // false
    println(balance("(())()()".toList)) // true
  }

  def pascal(c: Int, r: Int): Int = {
    require(c >= 0 && r >= 0 && c <= r, "Invalid input")
    if (c == 0 || c == r) {
      1
    } else {
      pascal(c-1, r-1) + pascal(c, r-1)
    }
  }

  def balance(chars: List[Char]): Boolean = {
    @tailrec
    def balanceHelper(chars: List[Char], count: Int): Boolean = {
      if (count < 0) false
      else if (chars.isEmpty) count == 0
      else if (chars.head == '(') {
        balanceHelper(chars.tail, count + 1)
      } else if (chars.head == ')') {
        balanceHelper(chars.tail, count - 1)
      } else {
        balanceHelper(chars.tail, count)
      }
    }

    balanceHelper(chars, 0)
  }

  def countChange(money: Int, coins: List[Int]): Int = {
    0
  }

}