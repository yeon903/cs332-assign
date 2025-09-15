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

  test("forall tests") {
    new TestSets {
      val s1234 = union(union(s1, s2), union(s3, s4))  // {1, 2, 3, 4}
      val s24 = union(s2, s4)  // {2, 4}

      // 모든 원소가 양수인가?
      assert(forall(s1234, x => x > 0), "All elements are positive")
      assert(!forall(s1234, x => x > 2), "Not all elements are > 2")

      // 모든 원소가 짝수인가?
      assert(forall(s24, x => x % 2 == 0), "All elements in {2,4} are even")
      assert(!forall(s1234, x => x % 2 == 0), "Not all elements in {1,2,3,4} are even")

      // 모든 원소가 10보다 작은가?
      assert(forall(s1234, x => x < 10), "All elements are < 10")
      assert(!forall(s1234, x => x < 3), "Not all elements are < 3")

      // 빈 집합에서는 항상 true (공허한 참)
      val emptySet = intersect(s1, s2)  // 공집합
      assert(forall(emptySet, x => x > 1000), "Empty set: vacuous truth")
      assert(forall(emptySet, x => x < -1000), "Empty set: vacuous truth 2")
    }
  }

  test("exists tests") {
    new TestSets {
      val s1234 = union(union(s1, s2), union(s3, s4))  // {1, 2, 3, 4}
      val s13 = union(s1, s3)  // {1, 3}

      // 짝수가 존재하는가?
      assert(exists(s1234, x => x % 2 == 0), "Even number exists in {1,2,3,4}")
      assert(!exists(s13, x => x % 2 == 0), "No even number in {1,3}")

      // 2보다 큰 수가 존재하는가?
      assert(exists(s1234, x => x > 2), "Number > 2 exists in {1,2,3,4}")
      assert(!exists(s1, x => x > 2), "No number > 2 in {1}")

      // 특정 값이 존재하는가?
      assert(exists(s1234, x => x == 3), "3 exists in {1,2,3,4}")
      assert(!exists(s1234, x => x == 5), "5 does not exist in {1,2,3,4}")

      // 빈 집합에서는 항상 false
      val emptySet = intersect(s1, s2)  // 공집합
      assert(!exists(emptySet, x => x > 0), "Empty set: nothing exists")
      assert(!exists(emptySet, x => true), "Empty set: even trivial condition false")
    }
  }

  test("map tests") {
    new TestSets {
      val s12 = union(s1, s2)  // {1, 2}
      val s123 = union(s12, s3)  // {1, 2, 3}

      // 2배 변환
      val doubled = map(s12, x => x * 2)  // {2, 4}
      assert(contains(doubled, 2), "Doubled set contains 2")
      assert(contains(doubled, 4), "Doubled set contains 4")
      assert(!contains(doubled, 1), "Doubled set does not contain 1")
      assert(!contains(doubled, 3), "Doubled set does not contain 3")

      // 제곱 변환
      val squared = map(s123, x => x * x)  // {1, 4, 9}
      assert(contains(squared, 1), "Squared set contains 1")
      assert(contains(squared, 4), "Squared set contains 4")
      assert(contains(squared, 9), "Squared set contains 9")
      assert(!contains(squared, 2), "Squared set does not contain 2")
      assert(!contains(squared, 3), "Squared set does not contain 3")

      // 상수 변환 (모든 원소를 같은 값으로)
      val constant = map(s123, x => 5)  // {5}
      assert(contains(constant, 5), "Constant map contains 5")
      assert(!contains(constant, 1), "Constant map does not contain original elements")

      // +1 변환
      val incremented = map(s12, x => x + 1)  // {2, 3}
      assert(contains(incremented, 2), "Incremented set contains 2")
      assert(contains(incremented, 3), "Incremented set contains 3")
      assert(!contains(incremented, 1), "Incremented set does not contain 1")

      // 중복 제거 테스트 (여러 원소가 같은 값으로 매핑)
      val s_minus1_1 = union(singletonSet(-1), s1)  // {-1, 1}
      val absValue = map(s_minus1_1, x => if (x < 0) -x else x)  // {1}
      assert(contains(absValue, 1), "Absolute value map contains 1")
      assert(!contains(absValue, -1), "Absolute value map does not contain -1")
    }
  }

  test("forall and exists relationship") {
    new TestSets {
      val s1234 = union(union(s1, s2), union(s3, s4))  // {1, 2, 3, 4}

      // De Morgan's laws: ¬∃x.P(x) ≡ ∀x.¬P(x)
      val condition = (x: Int) => x > 5
      assert(
        !exists(s1234, condition) == forall(s1234, x => !condition(x)),
        "De Morgan's law: not exists ≡ forall not"
      )

      // ¬∀x.P(x) ≡ ∃x.¬P(x)
      val condition2 = (x: Int) => x % 2 == 0
      assert(
        !forall(s1234, condition2) == exists(s1234, x => !condition2(x)),
        "De Morgan's law: not forall ≡ exists not"
      )
    }
  }

  test("complex combinations2") {
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

      // map과 filter 조합
      val mapped = map(s12, x => x * 2)  // {2, 4}
      val filtered = filter(s1234, x => x % 2 == 0)  // {2, 4}
      val combined = union(mapped, filtered)  // {2, 4}

      assert(forall(combined, x => x % 2 == 0), "Combined set has only even numbers")
      assert(exists(combined, x => x == 2), "Combined set contains 2")
      assert(exists(combined, x => x == 4), "Combined set contains 4")

      // 복잡한 조건 테스트
      val complexSet = map(filter(s1234, x => x > 2), x => x - 1)  // filter: {3,4}, map: {2,3}
      assert(contains(complexSet, 2), "Complex operation result contains 2")
      assert(contains(complexSet, 3), "Complex operation result contains 3")
      assert(!contains(complexSet, 1), "Complex operation result does not contain 1")
      assert(!contains(complexSet, 4), "Complex operation result does not contain 4")
    }
  }

  test("edge cases and boundary conditions") {
    new TestSets {
      // 경계값 테스트 (-1000, 1000)
      val boundarySet = union(singletonSet(-1000), singletonSet(1000))

      assert(contains(boundarySet, -1000), "Boundary set contains -1000")
      assert(contains(boundarySet, 1000), "Boundary set contains 1000")
      assert(forall(boundarySet, x => x >= -1000 && x <= 1000), "All elements within bounds")
      assert(exists(boundarySet, x => x == -1000), "Minimum boundary exists")
      assert(exists(boundarySet, x => x == 1000), "Maximum boundary exists")

      // 매우 큰 집합에서의 테스트
      val largeRangeSet = (x: Int) => x >= -100 && x <= 100  // 201개 원소
      assert(forall(largeRangeSet, x => x >= -100), "Large range: all >= -100")
      assert(exists(largeRangeSet, x => x == 0), "Large range: contains 0")

      val mappedLarge = map(largeRangeSet, x => x / 10)  // 매핑 후 중복 많음
      assert(exists(mappedLarge, x => x == 0), "Mapped large set contains 0")
      assert(exists(mappedLarge, x => x == 5), "Mapped large set contains 5")
    }
  }

}
