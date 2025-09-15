package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(4)
    val s5 = singletonSet(5)

    // 추가 테스트 집합들
    val evenSet = (x: Int) => x % 2 == 0  // 짝수 집합
    val oddSet = (x: Int) => x % 2 == 1   // 홀수 집합
    val positiveSet = (x: Int) => x > 0   // 양수 집합
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("singletonSet tests") {
    new TestSets {
      assert(contains(s1, 1), "s1 contains 1")
      assert(!contains(s1, 2), "s1 does not contain 2")
      assert(contains(s2, 2), "s2 contains 2")
      assert(!contains(s2, 1), "s2 does not contain 1")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("union additional tests") {
    new TestSets {
      val s12 = union(s1, s2)  // {1, 2}
      val s34 = union(s3, s4)  // {3, 4}
      val s1234 = union(s12, s34)  // {1, 2, 3, 4}

      assert(contains(s1234, 1), "Union contains 1")
      assert(contains(s1234, 2), "Union contains 2")
      assert(contains(s1234, 3), "Union contains 3")
      assert(contains(s1234, 4), "Union contains 4")
      assert(!contains(s1234, 5), "Union does not contain 5")

      // 같은 집합과의 합집합
      val s1_union_s1 = union(s1, s1)
      assert(contains(s1_union_s1, 1), "Union with itself")
      assert(!contains(s1_union_s1, 2), "Union with itself - no extra elements")
    }
  }

  test("intersect tests") {
    new TestSets {
      val s12 = union(s1, s2)  // {1, 2}
      val s23 = union(s2, s3)  // {2, 3}
      val intersection = intersect(s12, s23)  // {2}

      assert(!contains(intersection, 1), "Intersection does not contain 1")
      assert(contains(intersection, 2), "Intersection contains 2")
      assert(!contains(intersection, 3), "Intersection does not contain 3")

      // 교집합이 공집합인 경우
      val emptyIntersection = intersect(s1, s3)  // {}
      assert(!contains(emptyIntersection, 1), "Empty intersection - no 1")
      assert(!contains(emptyIntersection, 3), "Empty intersection - no 3")

      // 같은 집합과의 교집합
      val s1_intersect_s1 = intersect(s1, s1)
      assert(contains(s1_intersect_s1, 1), "Intersection with itself")
    }
  }

  test("diff tests") {
    new TestSets {
      val s12 = union(s1, s2)  // {1, 2}
      val s23 = union(s2, s3)  // {2, 3}
      val difference = diff(s12, s23)  // {1}

      assert(contains(difference, 1), "Difference contains 1")
      assert(!contains(difference, 2), "Difference does not contain 2")
      assert(!contains(difference, 3), "Difference does not contain 3")

      // 반대 방향 차집합
      val reverseDifference = diff(s23, s12)  // {3}
      assert(!contains(reverseDifference, 1), "Reverse difference does not contain 1")
      assert(!contains(reverseDifference, 2), "Reverse difference does not contain 2")
      assert(contains(reverseDifference, 3), "Reverse difference contains 3")

      // 자기 자신과의 차집합 (공집합)
      val emptyDiff = diff(s1, s1)
      assert(!contains(emptyDiff, 1), "Self difference is empty")
    }
  }

  test("filter tests") {
    new TestSets {
      val s1234 = union(union(s1, s2), union(s3, s4))  // {1, 2, 3, 4}

      // 짝수만 필터링
      val evenFiltered = filter(s1234, x => x % 2 == 0)  // {2, 4}
      assert(!contains(evenFiltered, 1), "Even filter does not contain 1")
      assert(contains(evenFiltered, 2), "Even filter contains 2")
      assert(!contains(evenFiltered, 3), "Even filter does not contain 3")
      assert(contains(evenFiltered, 4), "Even filter contains 4")

      // 홀수만 필터링
      val oddFiltered = filter(s1234, x => x % 2 == 1)  // {1, 3}
      assert(contains(oddFiltered, 1), "Odd filter contains 1")
      assert(!contains(oddFiltered, 2), "Odd filter does not contain 2")
      assert(contains(oddFiltered, 3), "Odd filter contains 3")
      assert(!contains(oddFiltered, 4), "Odd filter does not contain 4")

      // 2보다 큰 수만 필터링
      val greaterThanTwo = filter(s1234, x => x > 2)  // {3, 4}
      assert(!contains(greaterThanTwo, 1), "Greater than 2 filter does not contain 1")
      assert(!contains(greaterThanTwo, 2), "Greater than 2 filter does not contain 2")
      assert(contains(greaterThanTwo, 3), "Greater than 2 filter contains 3")
      assert(contains(greaterThanTwo, 4), "Greater than 2 filter contains 4")
    }
  }

  test("complex combinations") {
    new TestSets {
      // 복잡한 조합 테스트
      val s12 = union(s1, s2)  // {1, 2}
      val s34 = union(s3, s4)  // {3, 4}
      val s1234 = union(s12, s34)  // {1, 2, 3, 4}

      // 짝수를 필터링한 후 {5}와 합집합
      val evenThenUnion = union(filter(s1234, x => x % 2 == 0), s5)  // {2, 4, 5}
      assert(!contains(evenThenUnion, 1), "Complex: no 1")
      assert(contains(evenThenUnion, 2), "Complex: has 2")
      assert(!contains(evenThenUnion, 3), "Complex: no 3")
      assert(contains(evenThenUnion, 4), "Complex: has 4")
      assert(contains(evenThenUnion, 5), "Complex: has 5")
    }
  }

}
