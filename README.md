# Build Caches and Cacheable Tasks

## How many kinds of caches when you do have when you build android?
1. Build directory “cache” aka incremental builds, 
   it's build level caches for you project level incremental tasks.
   
2. Build cache($HOME/.gradle/caches/build-cache-1), Beyond incremental builds described in up-to-date checks, 
   Task outputs can be reused between builds on one computer or even between 
   builds running on different computers via a build cache.
   
3. Android’s build cache(<user-home>/.android/build-cache/), even remove all the previous 2 caches,
   There are still some task from cache  
```bash
# delete project-specific cache
$ ./gradlew clean 

# delete build cache
$ rm -rf $GRADLE_HOME/caches/build-cache-* 

# build once to generate build cache
$ ./gradlew --build-cache --scan app:assembleDebug 
238 actionable tasks: 175 executed, 49 from cache, 14 up-to-date
``` 
