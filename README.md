# CorotineRequester

<img src="https://github.com/ShabanKamell/CoroutineRequester/blob/master/blob/raw/logo.png" height="200">

A simple wrapper for Kotlin Coroutines that helps you:
- [ ] Make clean RxJava requests.
- [ ] Inline & Global error handling.
- [ ] Resume the current request after errors like token expired error.
- [ ] Easy control of loading indicators.
CorotineRequester does all the dirty work for you!

### Before CorotineRequester

``` kotlin
    try {
      toggleLoading(show = true)
      val result = dm.restaurantsRepo.all()
    } catch (error: Exception) {
       // handle exception
       toggleLoading(show = false)
    } finally {
       toggleLoading(show = false)
    }
```

### After CorotineRequester

``` kotlin
coroutinesRequester.request { val result = restaurantsRepo.all() }
```

#### Gradle:
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

dependencies {
        implementation 'com.github.ShabanKamell:CoroutineRequester:x.y.z'
}

```
(Please replace x, y and y with the latest version numbers:  [![](https://jitpack.io/v/ShabanKamell/CoroutineRequester.svg)](https://jitpack.io/#ShabanKamell/CoroutineRequester)

### Setup

``` kotlin
val presentable = object: Presentable {
            override fun showError(error: String) { showError.value = error }
            override fun showError(error: Int) { showErrorRes.value = error }
            override fun showLoading() { toggleLoading.value = true }
            override fun hideLoading() { toggleLoading.value = false }
            override fun onHandleErrorFailed() { showErrorRes.value = R.string.oops_something_went_wrong }
        }

       val requester = CorotineRequester.create(ErrorContract::class.java, presentable)
```
## Error Handling
There're 2 types of error handlers in the library

##### 1- Retrofit Http Handler
Handles Retrofit's HttpException

``` kotlin
class ServerErrorHandler : HttpExceptionHandler() {

    override fun supportedErrors(): List<Int> {
        return listOf(500)
    }

    override fun handle(info: HttpExceptionInfo) {
        info.presentable.showError(R.string.oops_something_went_wrong)
    }
}
```
##### 2- Throwable Handler
handles generic Throwables

``` kotin
class OutOfMemoryErrorHandler: ThrowableHandler<OutOfMemoryError>() {

    override fun supportedErrors(): List<Class<OutOfMemoryError>> {
        return listOf(OutOfMemoryError::class.java)
    }

    override fun handle(info: ThrowableInfo) {
        info.presentable.showError(R.string.no_memory_free_up_space)
    }
}
```

## How to provide handlers?

```kotlin
      CorotineRequester.httpHandlers =      listOf(ServerErrorHandler())
      CorotineRequester.throwableHandlers = listOf(OutOfMemoryErrorHandler())
```

## Error Handlers Priority
The library handles errors according to this priority
##### 1- HTTP Handlers
##### 2- Throwable Handlers

#### Server Error Contract
CoroutineRequester optionally parsers server error for you and shows the error automatically. Just implement `ErrorMessage`
interface in your server error model and return the error message.

``` kotlin
data class ErrorContract(private val message: String): ErrorMessage {
    override fun errorMessage(): String {
        return message
    }
}
// Pass the contract
val requester = CorotineRequester.create(ErrorContract::class.java, presentable)
```

#### Customizing Requests
CorotineRequester gives you the full controll over any request
- [ ] Inline error handling
- [ ] Enable/Disable loading indicators
- [ ] Set Coroutine dispatcher

``` kotlin
        val requestOptions = RequestOptions().apply { 
                inlineHandling = { false }
                showLoading = true
            }
        /*
        val requestOptions = RequestOptions.Builder()
                .inlineErrorHandling { false }
                .showLoading(true)
                .dispatcher(Dispatchers.Main)
                .build()
        */
        requester.request(requestOptions) { restaurantsRepo.all() }
```

Here're all request options and default values

| **Option** | **Type** | **Default** |
| ------------- | ------------- | ------------- |
| **inlineHandling**           | Lambda       | null |
| **showLoading**              | Boolean      | true |
| **subscribeOnScheduler**     | CoroutineDispatcher    | Dispatchers.Main |

### Retrying The Request
There're cases where you want to handle the error and resume the current request as normal. CoroutineRequester makes it easy to retry the current request, just call `HttpExceptionInfo.retryRequest().
Imagine you received `401 token expired` error and you want to refresh the token then resume the original request. This can be done as easy as like this!

``` kotlin
class TokenExpiredHandler : HttpExceptionHandler() {

    override fun supportedErrors(): List<Int> {
        return listOf(401)
    }

    override fun handle(info: HttpExceptionInfo) {
//        refreshTokenApit()
//        info.retryRequest()
    }
}
```

### Best Practices
- [ ] Setup `CorotineRequester` only once in `BaseViewModel` and reuse in the whole app.
- [ ] Initialize error handlers only once.
- [ ] Use a scope for runnig coroutine. i.e. viewModelScope.

#### Look at 'sample' module for the full code. For more advanced example.

### License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
