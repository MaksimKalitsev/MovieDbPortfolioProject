package ua.zp.gardendirectory.ui.plantsList_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.zp.gardendirectory.R
import ua.zp.gardendirectory.data.models.MovieData
import ua.zp.gardendirectory.databinding.FragmentPlantsListBinding
import ua.zp.gardendirectory.ui.PlantAdapter
import ua.zp.gardendirectory.ui.view_custom.SearchView

enum class RequestState {
    LOADING, SUCCESS, ERROR;
}

class PlantsListFragment : Fragment() {
    private var _binding: FragmentPlantsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PlantsListViewModel>()

    private lateinit var adapter: PlantAdapter

    private val searchCallback = object : SearchView.Callback {
        override fun onQueryChanged(query: String) {
            viewModel.setSearchBy(query)
        }
    }
    private val navCallback = object : PlantAdapter.PlantHolder.NavCallback{
        override fun onItemRecyclerViewClicked(item: MovieData) {
            val direction =
                PlantsListFragmentDirections.actionPlantsListFragmentToDetailsFragment(plantData = item)
            findNavController().navigate(direction)
        }
    }

    private val diffUtilItemCallback = object : DiffUtil.ItemCallback<MovieData>() {
        override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isInitialized.not())
            viewModel.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PlantAdapter(diffUtilItemCallback, navCallback)
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.searchView.setCallback(searchCallback)

        setupSwipeToRefresh()

        lifecycleScope.launch {
            viewModel.plantsFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    //    private val stateObserver = Observer<PlantsListState>{
//        when(it.requestState){
//            RequestState.LOADING->{
//                binding.progressBar.visibility = View.VISIBLE
//            }
//            RequestState.SUCCESS->{
//                binding.progressBar.visibility = View.GONE
//                adapter.submitData(lifecycle, plantsFlow)
//            }
//            RequestState.ERROR->{
//                binding.progressBar.visibility = View.GONE
//                showSnackbar()
//            }
//        }
//    }
    private fun showSnackbar() {
        val mySnackbar =
            Snackbar.make(
                binding.plantListLayout,
                R.string.error_snackbar,
                Snackbar.LENGTH_INDEFINITE
            )
        mySnackbar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
        mySnackbar.show()
    }
}
