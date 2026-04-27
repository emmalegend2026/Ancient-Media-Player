## 2024-04-26 - Regex parsing in RecyclerView
**Learning:** In Android RecyclerViews, executing regex inside utility functions called during `onBindViewHolder` (like `ArtistSeparator.split()`) is a hidden bottleneck since it executes on the main thread for every scrolled item.
**Action:** Always add a fast path (e.g., `String.contains()`) to avoid compiling/executing regex when strings don't contain the target delimiters, especially inside hot loops like adapters.
