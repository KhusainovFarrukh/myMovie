package academy.android.mymovie.ui

import academy.android.mymovie.R
import academy.android.mymovie.adapter.MovieAdapter
import academy.android.mymovie.clickinterface.MovieClickInterface
import academy.android.mymovie.databinding.FragmentMoviesListBinding
import academy.android.mymovie.decorator.MovieItemDecoration
import academy.android.mymovie.utils.Constants.DEFAULT_IMAGE_URL
import academy.android.mymovie.utils.Constants.DEFAULT_SEARCH
import academy.android.mymovie.utils.Constants.DEFAULT_SIZE
import academy.android.mymovie.utils.Constants.KEY_BASE_URL
import academy.android.mymovie.utils.Constants.KEY_POSTER
import academy.android.mymovie.utils.Constants.KEY_SEARCH
import academy.android.mymovie.utils.Constants.KEY_SHARED_PREF
import academy.android.mymovie.viewmodel.SearchViewModel
import academy.android.mymovie.viewmodelfactory.SearchViewModelFactory
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

class FragmentSearch : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MovieAdapter
    private lateinit var searchViewModel: SearchViewModel
    private var movieClickInterface: MovieClickInterface? = null
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.rvMovies.addItemDecoration(
            MovieItemDecoration(
                resources.getDimension(R.dimen.dp8).toInt(),
                resources.getDimension(R.dimen.dp18).toInt()
            )
        )

        sharedPrefs = requireActivity().getSharedPreferences(KEY_SHARED_PREF, Context.MODE_PRIVATE)
        editor = sharedPrefs.edit()

        val imageUrl = sharedPrefs.getString(KEY_BASE_URL, DEFAULT_IMAGE_URL) +
                sharedPrefs.getString(KEY_POSTER, DEFAULT_SIZE)

        binding.rvMovies.setHasFixedSize(true)
        binding.rvMovies.layoutManager = GridLayoutManager(requireActivity(), 2)
        adapter = MovieAdapter(movieClickInterface!!, imageUrl)
        binding.rvMovies.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        searchViewModel = ViewModelProvider(
            this, SearchViewModelFactory(
                arguments?.getString(KEY_SEARCH, DEFAULT_SEARCH) ?: DEFAULT_SEARCH
            )
        ).get(SearchViewModel::class.java)

        searchViewModel.isLoading.observe(this.viewLifecycleOwner, this::setLoading)
        searchViewModel.moviesList.observe(this.viewLifecycleOwner, adapter::submitList)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MovieClickInterface) {
            movieClickInterface = context
        } else {
            throw IllegalArgumentException("Activity is not MovieClickInterface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        movieClickInterface = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLoading(isLoading: Boolean) {
        binding.prbLoading.isVisible = isLoading
    }
}