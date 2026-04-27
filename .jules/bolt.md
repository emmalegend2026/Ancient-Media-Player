## 2024-04-26 - Regex parsing in RecyclerView
**Learning:** In Android RecyclerViews, executing regex inside utility functions called during `onBindViewHolder` (like `ArtistSeparator.split()`) is a hidden bottleneck since it executes on the main thread for every scrolled item.
**Action:** Always add a fast path (e.g., `String.contains()`) to avoid compiling/executing regex when strings don't contain the target delimiters, especially inside hot loops like adapters.
## 2024-04-27 - Test Coroutines Void Return Type
**Learning:** When using JUnit4 and `runBlocking` to test Kotlin suspend functions, the test method must use a block body (`fun myTest() { runBlocking { ... } }`) instead of an expression body (`fun myTest() = runBlocking { ... }`) to prevent `org.junit.runners.model.InvalidTestClassError: Method should be void`.
**Action:** Always wrap `runBlocking` calls inside a void-returning block when writing JUnit4 tests in Kotlin.
