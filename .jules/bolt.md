
## 2024-05-18 - Safe Projection Injection in Android MediaStore
**Learning:** Injecting SQL subqueries into `ContentResolver` projections (e.g. `(SELECT COUNT(audio_id)...) AS song_count`) is a valid optimization on older Android versions but strictly forbidden and throws `IllegalArgumentException` on Android 10+ due to `MediaProvider` strict column checking.
**Action:** Always wrap optimization subqueries in `ContentResolver` projections inside a try-catch block catching `Exception`, and provide a graceful fallback executing a standard projection to maintain crash-free compatibility across all OS versions.
