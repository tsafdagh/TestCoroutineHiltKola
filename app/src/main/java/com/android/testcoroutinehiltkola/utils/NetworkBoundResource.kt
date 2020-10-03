import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.android.testcoroutinehiltkola.utils.AppExecutors
import com.android.testcoroutinehiltkola.utils.FirebaseResponseType
import com.android.testcoroutinehiltkola.utils.Resource
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

//https://medium.com/dev-genius/android-networking-and-database-caching-in-2020-mvvm-retrofit-room-flow-35b4f897d46a
//https://www.reddit.com/r/androiddev/comments/dnmgv5/networkboundresource_flow_api/

@InternalCoroutinesApi
inline fun <DB, REMOTE> NetworkBoundResource(
    crossinline fetchFromLocal: () -> Flow<DB>, //It fecth data from local database
    crossinline shouldFetchFromRemote: (DB?) -> Boolean = { true }, //It decide whether network request should be made or use local persistent data if available (Optional)
    crossinline fetchFromRemote: () -> Flow<FirebaseResponseType<REMOTE>>, //It perform network request operation
    crossinline processRemoteResponse: (response: FirebaseResponseType<REMOTE>) -> REMOTE, //It process result of network response before saving model class in database
    crossinline saveRemoteData: (REMOTE) -> Unit = { Unit }, //It saves result of network request to local persistent database
    crossinline onFetchFailed: (errorBody: String?, statusCode: Int) -> Unit = { _: String?, _: Int -> Unit } // It handle network request failure scenario (Non HTTP 200..300 response, exceptions etc)
) = flow<Resource<DB>> {

    emit(Resource.Loading(null))

    val localData = fetchFromLocal().first()

    if (shouldFetchFromRemote(localData)) {

        emit(Resource.Loading(localData))

        fetchFromRemote().collect { apiResponse ->
            when (apiResponse) {

                is FirebaseResponseType.FirebaseSuccesResponse<*> -> {

                    saveRemoteData(processRemoteResponse(apiResponse))
                    emitAll(fetchFromLocal().map { dbData ->
                        Resource.Success(dbData)
                    })
                }

                is FirebaseResponseType.FirebaseErrorResponse<*> -> {

                    onFetchFailed(apiResponse.throwable?.message, 0)
                    emitAll(fetchFromLocal().map {
                        Resource.Failure(
                            apiResponse.throwable,
                            it
                        )
                    })
                }
            }
        }
    } else {
        emitAll(fetchFromLocal().map { Resource.Success(it) })
    }
}


/*// CachedObject: Type for the Resource data.(database cache)
// RequestObject: Type for the API response.(network request)
abstract class NetworkBoundResource2<CachedObject, RequestObject>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val TAG = "NetworkBoundResource"
    private var results = MediatorLiveData<Resource<CachedObject>>()

    init {
        //update liveData for loading status
        results.value = Resource.Loading(null)

        //observe liveData source from local db
        val dbSource: LiveData<CachedObject> = loadFromDb()
        results.addSource(dbSource, Observer { cacheObject: CachedObject ->
            results.removeSource(dbSource)

            if (shouldFetch(cacheObject)) {
                fetchFromNetwork(dbSource)
            } else {
                results.addSource(dbSource, Observer { cacheObject: CachedObject ->
                    setValue(Resource.Success(cacheObject))
                })
            }
        })

    }

    *//**
     * Fetch from network
     * 1) observe local db
     * 2) if<condition/> query the network
     * 3) stop observing the local db
     * 4) insert new data into local db
     * 5) begin observing local db again to see the refreshed data from network
     * @param dbSource
     *//*
    private fun fetchFromNetwork(dbSource: LiveData<CachedObject>) {
        Log.d(TAG, "fetchFromNetwork: called.")

        //update LiveData for loading status
        results.addSource(dbSource, Observer { cacheObject: CachedObject ->
            setValue(Resource.Loading(cacheObject))
        })

        val apiResponse: LiveData<FirebaseResponseType<RequestObject>> =
            createCall();//the API is firebase

        results.addSource(
            apiResponse,
            Observer { requestObjectApiResponse: FirebaseResponseType<RequestObject> ->
                results.removeSource(dbSource);
                results.removeSource(apiResponse)

                *//*
                    3 cases:
                        1) ApiSuccessResponse
                        2) ApiErrorResponse
                        3) ApiEmptyResponse
                 *//*
                when (requestObjectApiResponse) {

                    is FirebaseResponseType.FirebaseSuccesResponse<*> -> {

                        appExecutors.diskIO().execute {
                            //save result on Room DB
                            saveCallResult(processResponse(requestObjectApiResponse))

                            appExecutors.mainThread().execute {
                                results.addSource(
                                    loadFromDb(),
                                    Observer { cachedObject: CachedObject ->
                                        setValue(Resource.Success(cachedObject))
                                    })
                            }
                        }
                    }
                    is FirebaseResponseType.FirebaseEmptyResponse<*> -> {
                        Log.d(TAG, "fetchFromNetwork: API Empty response")
                        results.addSource(dbSource, Observer { cachedObject: CachedObject ->
                            setValue(Resource.Success(cachedObject))
                        })
                    }
                    is FirebaseResponseType.FirebaseErrorResponse<*> -> {
                        Log.d(TAG, "fetchFromNetwork: API faillure response")
                        results.addSource(dbSource, Observer { cachedObject: CachedObject ->
                            setValue(
                                Resource.Failure(
                                    requestObjectApiResponse.throwable,
                                    cachedObject
                                )
                            )
                        })
                    }
                }

            })
    }

    fun processResponse(response: FirebaseResponseType<RequestObject>): RequestObject {
        val sucessResponse = (response as FirebaseResponseType.FirebaseSuccesResponse)
        return sucessResponse.body
    }

    private fun setValue(newValue: Resource<CachedObject>) {
        if (results.value != newValue) {
            results.value = newValue
        }
    }

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestObject)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: CachedObject?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<CachedObject>

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<FirebaseResponseType<RequestObject>>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    fun asLiveData(): LiveData<Resource<CachedObject>> = results
}*/
