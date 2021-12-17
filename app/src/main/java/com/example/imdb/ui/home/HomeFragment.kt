package com.example.imdb.ui.home

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdb.R
import com.example.imdb.model.Movie
import com.example.imdb.model.Pagination

class HomeFragment : Fragment(R.layout.fragment_home), RecyclerViewClickInterface {


    private lateinit var homeViewModel: HomeViewModel
    val viewModel: HomeViewModel by viewModels()
    var movieAdapter = Adapter()
    var isFirstStart = true
    var isItSearch = false
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page = 1
    var searchMovie = ""
    var movieList = ArrayList<Movie>()
    lateinit var recyclerView: RecyclerView
    internal lateinit var callback: OnHeadlineSelectedListener

    fun setOnHeadlineSelectedListener(callback: OnHeadlineSelectedListener) {
        this.callback = callback
    }


    interface OnHeadlineSelectedListener {
        fun onArticleSelected()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.text.observe(viewLifecycleOwner, {

        })


        var button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            searchMovie = view.findViewById<EditText>(R.id.searchMovie).text.toString()
            isFirstStart = true
            isItSearch = true
            viewModel.showPage.value = 1
            viewModel.getSearchMovies(query = searchMovie)
            viewModel.showPage.value = 2
            page=2
        }

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = movieAdapter
        movieAdapter.recyclerViewClickInterface = this
        movieAdapter.context = context

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Movie>("unique_key")?.observe(
                viewLifecycleOwner) { result ->
            Log.d("sa", result.id.toString())
            val movie: Movie? = movieAdapter.movies.find { it.id == result.id }

            var index = movieAdapter.movies.indexOf(movie)
            if (index != -1)
                movieAdapter.movies.set(index, result)
            movieAdapter.notifyDataSetChanged()
        }

        if (!movieAdapter.isLinearLayout)
            recyclerView.layoutManager = LinearLayoutManager(activity)
        else
            recyclerView.layoutManager = GridLayoutManager(activity, 2)
        controlScroll()

        if (page < 2) {
            viewModel.showPage.value = 1
            viewModel.getPopularMovies()
            observeViewModel()
            page++
            viewModel.showPage.value = 2
        }
    }

    fun controlScroll() {

        recyclerView.addOnScrollListener(object : Pagination(recyclerView.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }
            override fun isLoading(): Boolean {
                return isLoading
            }
            override fun loadMoreItems() {
                isLoading = true
                if (isItSearch == false)
                    getMoreItems()
                if (isItSearch == true)
                    getMoreSearchItems()
            }
        })
    }

    fun getMoreItems() {
        isLoading = false
        viewModel.getPopularMovies()
        page++
        viewModel.showPage.value = viewModel.showPage.value?.plus(1)
    }

    fun getMoreSearchItems() {
        Log.d("sayi", viewModel.showPage.value.toString())
        isLoading = false
        viewModel.getSearchMovies(query = searchMovie)
        page++
        viewModel.showPage.value = viewModel.showPage.value?.plus(1)
    }

    fun observeViewModel() {

        viewModel.showLiveData.observe(requireActivity()) {
            if (isFirstStart == true) {
                isFirstStart = false
                movieAdapter.apply {
                    movieList = it
                    movies = it
                    notifyDataSetChanged()
                }
            } else {
                movieAdapter.addData(it)
            }
        }

        viewModel.showError.observe(requireActivity()) {
            if (it == true) {
                page--
                Log.e("hata", page.toString())
                if (context != null)
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(movie: Movie) {

        val navController = activity?.findNavController(R.id.nav_host_fragment)
        if (navController != null) {

            val bundle = Bundle()
            bundle.putInt("id", movie.id!!)
            navController.navigate(R.id.action_navigation_home_to_detailFragment, bundle)

        }
    }
}