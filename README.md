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

## There are lots of concepts:

1. Up to date check/Incremental Build/Incremental task: Specify Input & Output and let gradle do left
> As part of incremental build, Gradle tests whether any of the task inputs or outputs has changed since the last build.
> If they haven’t, Gradle can consider the task up to date and therefore skip executing its actions.
> Also note that incremental build won’t work unless a task has at least one task output, 
> although tasks usually have at least one input as well.
  
> What this means for build authors is simple: you need to tell Gradle which task properties are inputs and which are 
> outputs. If a task property affects the output, be sure to register it as an input, 
> otherwise the task will be considered up to date when it’s not. Conversely, 
> don’t register properties as inputs if they don’t affect the output, 
> otherwise the task will potentially execute when it doesn’t need to. 
> Also be careful of non-deterministic tasks that may generate different output for exactly the same inputs: 
> these should not be configured for incremental build as the up-to-date checks won’t work.

2. Cacheable task(Used for global caches that cross builds/projects)
