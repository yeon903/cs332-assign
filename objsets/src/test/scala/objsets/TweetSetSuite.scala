package objsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TweetSetSuite extends FunSuite {
  trait TestSets {
    val set1 = new Empty
    val set2 = set1.incl(new Tweet("a", "a body", 20))
    val set3 = set2.incl(new Tweet("b", "b body", 20))
    val c = new Tweet("c", "c body", 7)
    val d = new Tweet("d", "d body", 9)
    val set4c = set3.incl(c)
    val set4d = set3.incl(d)
    val set5 = set4c.incl(d)
  }

  def asSet(tweets: TweetSet): Set[Tweet] = {
    var res = Set[Tweet]()
    tweets.foreach(res += _)
    res
  }

  def size(set: TweetSet): Int = asSet(set).size

  test("filter: on empty set") {
    new TestSets {
      assert(size(set1.filter(tw => tw.user == "a")) === 0)
    }
  }

  test("filter: a on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.user == "a")) === 1)
    }
  }

  test("filter: 20 on set5") {
    new TestSets {
      assert(size(set5.filter(tw => tw.retweets == 20)) === 2)
    }
  }

  test("union: set4c and set4d") {
    new TestSets {
      assert(size(set4c.union(set4d)) === 4)
    }
  }

  test("union: with empty set (1)") {
    new TestSets {
      assert(size(set5.union(set1)) === 4)
    }
  }

  test("union: with empty set (2)") {
    new TestSets {
      assert(size(set1.union(set5)) === 4)
    }
  }

  test("descending: set5") {
    new TestSets {
      val trends = set5.descendingByRetweet
      assert(!trends.isEmpty)
      assert(trends.head.user == "a" || trends.head.user == "b")
    }
  }

  test("descending: empty set") {
    new TestSets {
      val trends = set1.descendingByRetweet
      assert(trends.isEmpty)
    }
  }

  test("descending: single element") {
    new TestSets {
      val trends = set2.descendingByRetweet
      assert(!trends.isEmpty)
      assert(trends.head.user == "a")
      assert(trends.head.retweets == 20)
      assert(trends.tail.isEmpty)
    }
  }

  test("descending: set5 order verification") {
    new TestSets {
      val trends = set5.descendingByRetweet

      assert(trends.head.retweets == 20)
      assert {
        trends.tail.head.retweets >= trends.tail.tail.head.retweets
      }
      assert(trends.tail.tail.head.retweets >= trends.tail.tail.tail.head.retweets)
      assert(trends.tail.tail.tail.tail.isEmpty)
    }
  }

  test("descending: verify all elements present") {
    new TestSets {
      val trends = set5.descendingByRetweet
      val trendSet = Set(trends.head, trends.tail.head, trends.tail.tail.head, trends.tail.tail.tail.head)
      val originalSet = asSet(set5)
      assert(trendSet == originalSet)
    }
  }

  test("descending: retweet counts in correct order") {
    new TestSets {
      val trends = set5.descendingByRetweet
      val retweetCounts = List(
        trends.head.retweets,
        trends.tail.head.retweets,
        trends.tail.tail.head.retweets,
        trends.tail.tail.tail.head.retweets
      )

      assert(retweetCounts(0) >= retweetCounts(1))
      assert(retweetCounts(1) >= retweetCounts(2))
      assert(retweetCounts(2) >= retweetCounts(3))
      assert(retweetCounts.contains(20))
      assert(retweetCounts.contains(9))
      assert(retweetCounts.contains(7))
    }
  }

  test("descending: with duplicate retweet counts") {
    new TestSets {
      val e = new Tweet("e", "e body", 20)
      val set6 = set5.incl(e)
      val trends = set6.descendingByRetweet

      assert(trends.head.retweets == 20)
      assert(trends.tail.head.retweets == 20)
      assert(trends.tail.tail.head.retweets == 20)

      var count = 0
      var current = trends
      while (!current.isEmpty) {
        count += 1
        current = current.tail
      }
      assert(count == 5)
    }
  }

  }
