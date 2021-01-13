## [What is Idiomatic Kotlin Code?](https://phauer.com/2018/best-practices-unit-testing-kotlin/#recap-what-is-idiomatic-kotlin-code)

- Immutability, should use immutable references with `val` instead of `var`
- Non-Nullability, should favor non-nullable types `String` over nullable types `String?`
- No `static` access,  It impedes proper object-oriented design, because static access harms testability (can’t exchange objects easily) and obfuscates dependencies and side-effects. Kotlin strongly encourages us to avoid static access by simply not providing an easy way to create static members.

```kotlin
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MongoDAOTestJUnit5Constructor {
    private val mongo: KGenericContainer
    private val mongoDAO: MongoDAO

    init {
        mongo = startMongoContainer().apply {
            configure()
        }
        mongoDAO = MongoDAO(mongo.host, mongo.port)
    }
}
```

## Change the Lifecycle Default for Every Test Class

Writing `@TestInstance(TestInstance.Lifecycle.PER_CLASS)` on every test class explicitly is cumbersome and easy to forget. Fortunately, we can set the [default lifecycle for the whole project](https://junit.org/junit5/docs/current/user-guide/#writing-tests-test-instance-lifecycle-changing-default) by creating the file `src/test/resources/junit-platform.properties` with the content:

```yaml
junit.jupiter.testinstance.lifecycle.default = per_class
```

## Use Backticks and `@Nested` Inner Classes

- Put the test method name in backticks. This allows spaces in the method name which highly improves readability. 
  This way, we don’t need an additional `@DisplayName` annotation.
    
-  JUnit5’s `@Nested` is useful to group the test methods. Reasonable groups can be certain types of tests 
   (like `InputIsXY`, `ErrorCases`) or one group for each method under test (`GetDesign` and `UpdateDesign`).

```kotlin
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DesignControllerTest {
    @Nested
    inner class GetDesigns {
        @Test
        fun `all fields are included`() {}
        @Test
        fun `limit parameter`() {}
        @Test
        fun `filter parameter`() {}
    }
    @Nested
    inner class DeleteDesign {
        @Test
        fun `design is removed from db`() {}
        @Test
        fun `return 404 on invalid id parameter`() {}
        @Test
        fun `return 401 if not authorized`() {}
    }
}
```

## Kotlin Test Libraries

Due to the variety of available Kotlin test libraries we are spoilt for choice. Here is an incomplete overview of some Kotlin-native and Java libraries for testing, mocking and assertions (note that some libraries fit into multiple categories):

|        | Test Frameworks | Mocking   | Assertion |
| :---   |    :----:   |          ---: |          ---: |
| Kotlin | [Kotest](https://github.com/kotest/kotest), [Spek](http://spekframework.org/)       | [mockito-kotlin](https://github.com/nhaarman/mockito-kotlin), [MockK](https://mockk.io/)   | [Kotest](https://github.com/kotest/kotest), [Kluent](https://markusamshove.github.io/Kluent/), [Strikt](https://strikt.io/), [Atrium](https://www.kotlinresources.com/library/atrium/), [HamKrest](https://github.com/npryce/hamkrest), [Expekt](http://winterbe.github.io/expekt/), [AssertK](https://www.kotlinresources.com/library/assertk/) |
| Java   | [JUnit5](https://junit.org/junit5/)        |      | [AsserJ](https://joel-costigliola.github.io/assertj/)|

Recommend: Junit5, MockK and Kotest’s matchers.

## Mock Handling

### Final By Default

Classes and therefore methods are final by default in Kotlin. Unfortunately, some libraries like Mockito are relying on subclassing which fails in these cases. What are the solutions to this?

- Use interfaces 
- `open` the class and methods explicitly for subclassing
- Enable the [incubating feature of Mockito to mock final classes](https://github.com/mockito/mockito/wiki/What%27s-new-in-Mockito-2#mock-the-unmockable-opt-in-mocking-of-final-classesmethods). For this, create a file with the name `org.mockito.plugins.MockMaker` in `test/resources/mockito-extensions` with the content mock-maker-inline.
- Use [MockK](https://mockk.io/) instead of Mockito/Mockito-Kotlin. It supports mocking final classes by default.

### Use MockK

When using MockK:
- Don't worry about final classes or additional interfaces
- Provides a convenient and idiomatic API for writing mocks and verifications.

```kotlin
val clientMock: UserClient = mockk()
val user = User(id = 1, name = "Ben")
every { clientMock.getUser(any()) } returns user
val daoMock: UserDAO = mockk(relaxed = true)

val scheduler = UserScheduler(clientMock, daoMock)
scheduler.start(1)

verifySequence {
    clientMock.getUser(1)
    daoMock.saveUser(user)
}
```
ote the usage of lambdas in `verifySequence { }` to nicely group verifications. Moreover, MockK provides reasonable error messages containing all tracked calls if verification fails. I also like that MockK fails with an exception if an unspecified method is called on a mock (strict mocking by default). So you don’t run into mysterious NullPointerExceptions known from Mockito.

Besides, MockK’s relaxed mocks are useful if the class under test uses a certain object, but you don’t want to define the behavior of this mock because it’s not relevant for the test. A relaxed mock returns dummy objects containing empty values.

```kotlin
val clientMock: UserClient = mockk(relaxed = true)
// we don't have to mock clientMock.getUser()
println(clientMock.getUser(1).age) // 0
```

But mind, that relaxed mocks can also lead to tricky errors, when you forget to mock a required method. I recommend using strict mocks by default and relaxed ones only if you really need it.

### Create Mocks Once
Recreating mocks before every test is slow and requires the usage of lateinit var. So the variable can be reassigned which can harm the independence of each test.

```kotlin
//Don't
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DesignControllerTest_RecreatingMocks {

    private lateinit var dao: DesignDAO
    private lateinit var mapper: DesignMapper
    private lateinit var controller: DesignController

    @BeforeEach
    fun init() {
        dao = mockk()
        mapper = mockk()
        controller = DesignController(dao, mapper)
    }

    // takes 2 s!
    @RepeatedTest(300)
    fun foo() {
        controller.doSomething()
    }
}
```

Instead, create the mock instance once and reset them before or after each test. It’s significantly faster (2 s vs 250 ms in the example) and allows using immutable fields with val.

```kotlin
// Do:
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DesignControllerTest {

    private val dao: DesignDAO = mockk()
    private val mapper: DesignMapper = mockk()
    private val controller = DesignController(dao, mapper)

    @BeforeEach
    fun init() {
        clearAllMocks()
    }

    // takes 250 ms
    @RepeatedTest(300)
    fun foo() {
        controller.doSomething()
    }
}
```

## Handle Classes with State

The presented create-once-approach for the test fixture and the classes under test only works if they don’t have any state or can be reset easily (like mocks). In other cases, re-creation before each test is inevitable.

```kotlin
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DesignViewTest {

    private val dao: DesignDAO = mockk()
    private lateinit var view: DesignView // the class under test has state

    @BeforeEach
    fun init() {
        clearAllMocks()
        view = DesignView(dao) // re-creation is required
    }

    @Test
    fun changeButton() {
        view.button.caption shouldBe "Hi"
        view.changeButton()
        view.button.caption shouldBe "Hallo"
    }
}
```

## Utilize Data Classes
### Data Classes for Assertions
#### Single Objects

If possible don’t compare each property for your object with a dedicated assertion. This bloats your code and - even more important - leads to an unclear failure message.
```kotlin
// Don't
val actualDesign = client.requestDesign(id = 1)

actualDesign.id shouldBe 2 // ComparisonFailure
actualDesign.userId shouldBe 9
actualDesign.name shouldBe "Cat"
```
This leads to poor failure messages:
```shell
org.opentest4j.AssertionFailedError: expected:<2> but was:<1>
Expected :2
Actual   :1
```
`Expected: 2. Actual: 1?`What is the semantics of the numbers? Design id or User id? What is the context/the containing class? Hard to say.

Instead, create an instance of the data classes with the expected values and use it directly in a single equality assertion.

```kotlin
// Do
val actualDesign = client.requestDesign(id = 1)

val expectedDesign = Design(id = 2, userId = 9, name = "Cat")
actualDesign shouldBe expectedDesign
```
This way, we get a nice and descriptive failure message:
```shell
data class diff for com.phauer.Design
└ id: expected:<2> but was:<1>

expected:<Design(id=2, userId=9, name=Cat)> but was:<Design(id=1, userId=9, name=Cat)>
Expected :Design(id=2, userId=9, name=Cat)
Actual   :Design(id=1, userId=9, name=Cat)
```
We take advantage of Kotlin’s data classes. They implement `equals()` and `toString()` out of the box. 
So the equals check works and we get a really nice failure message. Plus, Kotest detects data classes and outputs 
the property that is different. Moreover, by using named arguments, the code for creating the expected object becomes very readable.

#### Lists
We can take this approach even further and apply it to lists. Here, Kotest’s powerful list assertions are shining.

```kotlin
// Do
val actualDesigns = client.getAllDesigns()

actualDesigns.shouldContainExactly(
    Design(id = 1, userId = 9, name = "Cat"),
    Design(id = 2, userId = 4, name = "Dog")
)
```

```shell
java.lang.AssertionError: Expecting: [
    Design(id=1, userId=9, name=Cat),
    Design(id=2, userId=4, name=Dog)
] but was: [
    Design(id=1, userId=9, name=Cat),
    Design(id=2, userId=4, name=Dogggg)
]
Some elements were missing: [
    Design(id=2, userId=4, name=Dog)
] and some elements were unexpected: [
    Design(id=2, userId=4, name=Dogggg)
]
```

#### Other Useful Kotests Assertions
Usually, comparing all properties of a data class is what you need in the test. But from time to time, 
it’s useful to ignore some properties or to compare only some properties.
```kotlin
actualDesign.shouldBeEqualToIgnoringFields(expectedDesign, Design::id)
actualDesign.shouldBeEqualToUsingFields(expectedDesign, Design::userId, Design::name)
```

#### Group Assertions With Kotest’s `asClue`
If you really want to assert only a few properties of a data class, consider grouping the assertions 
using Kotest’s `asClue{}` function. First, it nicely groups the code. 
Second, failure messages will also print the object that `asClue` was invoked on. That provides very useful context.

```kotlin
actualDesign.asClue {
    it.id shouldBe 2
    it.name shouldBe "Cat"
}
```

```shell
org.opentest4j.AssertionFailedError: Design(id=1, userId=9, name=Cat) expected:<2> but was:<1>
Expected :2
Actual   :1
```

#### Data Classes for Parameterized Tests

Data classes can also be used for parameterized tests. Due to the automatic `toString()` implementation, 
we get a readable test result output in IDEA and the build.

```kotlin
@ParameterizedTest
@MethodSource("validTokenProvider")
fun `parse valid tokens`(data: TestData) {
    parse(data.input) shouldBe data.expected
}

private fun validTokenProvider() = Stream.of(
    TestData(input = "1511443755_2", expected = Token(1511443755, "2")),
    TestData(input = "151175_13521", expected = Token(151175, "13521")),
    TestData(input = "151144375_id", expected = Token(151144375, "id")),
    TestData(input = "15114437599_1", expected = Token(15114437599, "1")),
    TestData(input = null, expected = null)
)

data class TestData(
    val input: String?,
    val expected: Token?
)
```

### Helper Functions
#### Use Helper Functions with Default Arguments to Ease Object Creation

In reality, data structures are complex and nested. Creating those objects again and again in the tests can be cumbersome. 
In those cases, it’s handy to write a utility function that simplifies the creation of the data objects. 
Kotlin’s default arguments are really nice here as they allow every test to set only the relevant properties 
and don’t have to care about the remaining ones.

```kotlin
fun createDesign(
    id: Int = 1,
    name: String = "Cat",
    date: Instant = Instant.ofEpochSecond(1518278198),
    tags: Map<Locale, List<Tag>> = mapOf(
        Locale.US to listOf(Tag(value = "$name in English")),
        Locale.GERMANY to listOf(Tag(value = "$name in German"))
    )
) = Design(
    id = id,
    userId = 9,
    name = name,
    fileName = name,
    dateCreated = date,
    dateModified = date,
    tags = tags
)

// Usage
// only set the properties that are relevant for the current test
val testDesign = createDesign()
val testDesign2 = createDesign(id = 1, name = "Fox") 
val testDesign3 = createDesign(id = 1, name = "Fox", tags = mapOf())
```

This leads to concise and readable object creation code.

- Don’t add default arguments to the data classes in the production code just to make your tests easier. 
  If they are used only for the tests, they should be located in the test folder. 
  So use helper functions like the one above and set the default arguments there.
  
- Don’t use `copy()` just to ease object creation. Extensive `copy()` usage is hard to read; 
  especially with nested structures. Prefer the helper functions.
  
- Locate all helper functions in a single file like `CreationUtils.kt`. 
  This way, we can reuse the functions like lego bricks for each test.

#### Helper Extension Functions for Frequently Used Values

I prefer to use fixed values instead of randomized or changing values. 
But writing code like `Instant.ofEpochSecond(1525420010L)` again and again is annoying and blows the code.

```kotlin
// Don't
val date1 = Instant.ofEpochSecond(1L)
val date2 = Instant.ofEpochSecond(2L)
val date3 = Instant.ofEpochSecond(3L)
val uuid1 = UUID.fromString("00000000-000-0000-0000-000000000001");
val uuid2 = UUID.fromString("00000000-000-0000-0000-000000000002");
```

Fortunately, we can write extension functions for frequently used objects like `Instant`, `UUID` or MongoDB’s `ObjectId`.

```kotlin
fun Int.toInstant(): Instant 
    = Instant.ofEpochSecond(this.toLong())

fun Int.toUUID(): UUID 
    = UUID.fromString("00000000-0000-0000-a000-${this.toString().padStart(11, '0')}")

fun String.toObjectId(): ObjectId 
    = ObjectId(this.padStart(24, '0'))

val date1 = 1.toInstant()
val date2 = 2.toInstant()
val date3 = 3.toInstant()
val uuid1 = 1.toUUID()
val uuid2 = 2.toUUID()
```
The failure messages can be easily traced back to the test code:
```shell
org.junit.ComparisonFailure:
Expected :00000000-0000-0000-a000-00000000001
Actual   :00000000-0000-0000-a000-00000000002
```

#### Test-Specific Extension Functions

Extension Functions can be useful to extend an existing library in a natural way. 
Here is an example: Kotest’s `plusOrMinus()` for floating comparison requires a tolerance parameter. 
This leads to a lot of code duplication, bloated code, and distraction while reading the tests.

```kotlin
// Don't
taxRate1 shouldBe 0.3f.plusOrMinus(0.001f)
taxRate2 shouldBe 0.2f.plusOrMinus(0.001f)
taxRate3 shouldBe 0.5f.plusOrMinus(0.001f)
```
Instead, define the extension function `shouldBeCloseTo()` on Float which delegates to a `plusOrMinus()` invocation 
with a fixed tolerance. This method is specific for the current test class and can be placed there.
```kotlin
private infix fun Float.shouldBeCloseTo(expected: Float) 
    = this shouldBe expected.plusOrMinus(0.001f)
```
The essential test logic becomes clearer and the code is still idiomatic as we stick to the pattern of Kotest.

```kotlin
// Usage:
taxRate1 shouldBeCloseTo 0.3f
taxRate2 shouldBeCloseTo 0.2f
taxRate3 shouldBeCloseTo 0.5f
```

The same technique can be used for all libraries with a fluent API.

### Testcontainers: Reuse a Single Container

[Testcontainers](https://www.testcontainers.org/) is a Java API to control containers within your test code. That’s great for executing your tests 
against a real database instead of an in-memory database. [I highly recommend this approach](https://phauer.com/2017/dont-use-in-memory-databases-tests-h2/). However, starting a new 
container for each test is usually a big waste of time. It’s better to create the container once and reuse it for 
every test. Kotlin’s `object` singletons and lazy initialized properties (`by lazy { }`) are very helpful here.

```kotlin
object MongoContainer {
    val instance by lazy { startMongoContainer() }

    private fun startMongoContainer() = KGenericContainer("mongo:4.0.2").apply {
        withExposedPorts(27017)
        setWaitStrategy(Wait.forListeningPort())
        start()
    }
}

// Usage:
class DesignDAOTest {
    private val container = MongoContainer.instance
    private val dao = DesignDAO(container.host, container.port) // pseudo-code
    @Test
    fun foo() { }
}
```
- This way, we don’t need any test framework integration (like a JUnit5 extension for Testcontainers). 
- Don’t forget to clean up the database before each test method using a `@BeforeEach` method.
- Be careful when you are running the [tests in parallel](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution).
  Consider using different “database names” (or “schema” in case of RDBs) for each test class to avoid side-effects.
