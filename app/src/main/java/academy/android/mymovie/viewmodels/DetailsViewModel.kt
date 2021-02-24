package academy.android.mymovie.viewmodels

import academy.android.mymovie.data.Repository
import academy.android.mymovie.models.Actor
import academy.android.mymovie.models.Movie
import academy.android.mymovie.utils.ConfigurationService
import academy.android.mymovie.utils.Constants
import academy.android.mymovie.utils.Constants.KEY_BACKDROP
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_POSTER
import academy.android.mymovie.utils.Constants.KEY_PROFILE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class DetailsViewModel(
    repository: Repository,
    movieId: Int,
    private val configurationService: ConfigurationService
) : ViewModel() {

    private val _currentMovie = MutableLiveData<Movie>()
    private val _actorsList = MutableLiveData<List<Actor>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isLoadingActors = MutableLiveData<Boolean>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val currentMovie: LiveData<Movie> = _currentMovie
    val actorsList: LiveData<List<Actor>> = _actorsList
    val isLoading: LiveData<Boolean> = _isLoading
    val isLoadingActors: LiveData<Boolean> = _isLoadingActors

    init {
        coroutineScope.launch {

            coroutineScope.launch {
                _isLoading.postValue(true)
                _currentMovie.postValue(repository.getMovieById(movieId))
                _isLoading.postValue((false))
            }

            coroutineScope.launch {
                _isLoadingActors.postValue(true)
                _actorsList.postValue(repository.getCastByMovieId(movieId))
                _isLoadingActors.postValue(false)
            }
        }
    }

    fun getProfileUrl() = configurationService.getProfileUrl()

    fun getBackdropUrl() = configurationService.getBackdropUrl()
}